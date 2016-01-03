package jp.sh4.ooga.grep.word;

import jp.sh4.ooga.grep.SearchTypes;
import jp.sh4.ooga.grep.comparison.StringComparison;
import jp.sh4.ooga.grep.out.GrepResultOut;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Word Grep Main Class.
 * このクラスへのパラメータの設定はコンストラクタからのみ行えます。
 *
 * @author sh-ogawa
 * @since 0.1.0
 */
public class WordGrep {

    private final String searchWord;
    private final SearchTypes searchType;
    private Path tempFile;
    private GrepResultOut out;

    /**
     * コンストラクタ
     *
     * @param searchWord 検索文字
     * @param searchType 検索タイプ
     */
    public WordGrep(final String searchWord, final SearchTypes searchType) {
        this.searchWord = searchWord;
        this.searchType = searchType;
    }

    /**
     * Grep結果を書き込んだ一時ファイルを移動します。
     */
    public void moveTempFile() {
        try {
            Files.move(tempFile, Paths.get("wordgrep-result.tsv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Grep結果を書き込んだ一時ファイルを削除します。
     */
    public void removeTempFile() {
        try {
            Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Grepしてその結果を一時ファイルへ出力します。
     * 引数で渡されたディレクトリ内に子ディレクトリに対して再起的にGrep検索を行います。
     *
     * @param searchDir     Grepを行うルートディレクトリ
     * @return boolean 処理結果 true:正常 false:異常
     * @throws IOException
     */
    public boolean grepOutTempFile(final String searchDir) throws IOException {
        tempFile = Files.createTempFile("wordgrep", ".tmp");
        System.out.println(tempFile.toFile().getAbsolutePath());
        try (OutputStream outStream = Files.newOutputStream(tempFile)) {
            out = new GrepResultOut(outStream);
            return grepOutTempFile(searchDir, false);
        }

    }

    /**
     * Grepしてその結果を一時ファイルへ出力します。
     * 引数で渡されたディレクトリ内の子ディレクトリに対しても再起的にGrep検索を行います。
     *
     * @param searchDir     Grepを行うルートディレクトリ
     * @param recursionFlag 再帰フラグ
     * @return boolean 処理結果 true:正常 false:異常
     */
    private boolean grepOutTempFile(final String searchDir, final boolean recursionFlag) {

        File file = new File(searchDir);
        File[] listFiles = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String fileName) {

                //ExcelのBOOKファイルだけを対象とするため、それ以外のファイルと隠しディレクトリは無視する。
                if (fileName.startsWith(".") || fileName.startsWith("~$")) {
                    return false;
                }

                String absPath = dir.getAbsolutePath() + File.separator + fileName;
                Path path = Paths.get(absPath);
                if (Files.isRegularFile(path)
                        && (absPath.endsWith(".doc") || absPath.endsWith(".docx"))) {
                    return true;
                } else if (Files.isDirectory(path)) {
                    return grepOutTempFile(absPath, true);
                } else {
                    return false;
                }
            }
        });

        if (listFiles == null || listFiles.length == 0) {
            return false;
        }

        // 検索を実行
        for (File f : listFiles) {
            if (f.isFile()) {
                exeSearchWord(f);
            }
        }
        return true;
    }

    /**
     * s
     * ファイルに検索対象文字がないかを検索します。
     *
     * @param targetFile 検索対象ファイル
     */
    private void exeSearchWord(final File targetFile) {

        HWPFDocument document = null;
        System.out.println("読み込み開始[" + targetFile.getAbsolutePath() + "]");
        try (NPOIFSFileSystem fs = new NPOIFSFileSystem(targetFile)) {
            document = new HWPFDocument(fs.getRoot());
            Range range = document.getRange();
            for(int i=0; i<range.numSections(); i++){
                Section section = range.getSection(i);
                for (int j = 0; j < section.numParagraphs(); j++) {
                    Paragraph paragraph = section.getParagraph(j);
                    for (int k = 0; k < paragraph.numCharacterRuns(); k++) {
                        CharacterRun run = paragraph.getCharacterRun(k);
                        String line = run.text();
                        if (StringComparison.compare(line, searchWord, searchType)) {
                            out.wirte(targetFile.getAbsolutePath(), "", "", line);
                        }
                    }
                }
            }

            System.out.println("読み込み終了[" + targetFile.getAbsolutePath() + "]");

        } catch (IOException e) {
            System.out.println("読めなかったファイル[" + targetFile.getAbsolutePath() + "]");
        }
    }


}
