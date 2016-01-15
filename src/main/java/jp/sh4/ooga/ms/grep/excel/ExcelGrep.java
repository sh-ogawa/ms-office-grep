package jp.sh4.ooga.ms.grep.excel;

import jp.sh4.ooga.ms.grep.SearchTypes;
import jp.sh4.ooga.ms.grep.comparison.NumericComparison;
import jp.sh4.ooga.ms.grep.comparison.StringComparison;
import jp.sh4.ooga.ms.grep.out.GrepResultOut;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Excel Grep jp.sh4.ooga.ms.grep.cmd.Main Class.
 * このクラスへのパラメータの設定はコンストラクタからのみ行えます。
 *
 * @author sh-ogawa
 * @since 0.1.0
 */
public class ExcelGrep {

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
    public ExcelGrep(final String searchWord, final SearchTypes searchType) {
        this.searchWord = searchWord;
        this.searchType = searchType;
    }

    /**
     * Grep結果を書き込んだ一時ファイルを移動します。
     */
    public void moveTempFile() {
        try {
            Path path = Paths.get("excelgrep-result.tsv");
            Files.deleteIfExists(path);
            Files.move(tempFile, path);
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
        tempFile = Files.createTempFile("excelgrep", ".tmp");
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
                if (fileName.startsWith(".")) {
                    return false;
                }

                String absPath = dir.getAbsolutePath() + File.separator + fileName;

                Path path = Paths.get(absPath);

                if (Files.isRegularFile(path)
                        && (absPath.endsWith(".xls") || absPath.endsWith(".xlsx"))) {
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

        Workbook workbook = null;

        try (InputStream inputStream = new FileInputStream(targetFile)) {

            workbook = WorkbookFactory.create(inputStream);
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                final Sheet sheet = workbook.getSheetAt(i);
                final String sheetName = sheet.getSheetName();

                int lastRowNum = sheet.getLastRowNum();
                for (int j = 0; j <= lastRowNum; j++) {
                    Row row = sheet.getRow(j);
                    if (row == null) continue;
                    short lastCellNum = row.getLastCellNum();
                    for (int k = 0; k < lastCellNum; k++) {
                        Cell cell = row.getCell(k);
                        if (cell == null) continue;
                        String cellRef = (new CellReference(cell)).formatAsString();

                        //セルの書式により、検索の仕方が異なるやり方で対応できるようにしておく。
                        int cellType = cell.getCellType();
                        switch (cellType) {
                            case Cell.CELL_TYPE_STRING:
                                String targetStr = cell.getStringCellValue();
                                if (StringComparison.compare(targetStr, searchWord, searchType)) {
                                    out.wirte(targetFile.getAbsolutePath(), sheetName, cellRef, targetStr);
                                }
                                break;

                            case Cell.CELL_TYPE_NUMERIC:
                                double targetNum = cell.getNumericCellValue();
                                if (NumericComparison.compare(targetNum, searchWord, searchType)) {
                                    out.wirte(targetFile.getAbsolutePath(), sheetName, cellRef
                                            , String.valueOf(targetNum));
                                }
                                break;
                            default:
                        }

                    }
                }

            }
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
    }

}
