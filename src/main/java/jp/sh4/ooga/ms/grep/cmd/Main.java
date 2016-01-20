package jp.sh4.ooga.ms.grep.cmd;

import java.io.*;

import jp.sh4.ooga.ms.grep.SearchTypes;
import jp.sh4.ooga.ms.grep.excel.ExcelGrep;
import jp.sh4.ooga.ms.grep.word.WordGrep;

/**
 * エクセルファイルのgrepツール。
 * <pre>
 * 指定したフォルダからエクセルファイルを再帰的に検索し、
 * 指定した文字列をgrepします。
 *
 * 設定する引数は以下の通り。
 * ・第一引数：検索対象のディレクトリパス
 * ・第二引数：検索対象文字列
 * ・第三引数：検索モード(あいまい検索/完全一致)
 * ・第四引数：置換文字列
 * </pre>
 *
 * @author sh-ogawa
 */
public class Main {

    /**
     * 検索対象文字列。
     */
    private static String SEARCH_WORD;

    /**
     * 検索モード。
     */
    private static SearchTypes SEARCH_MODE;

    /**
     * 置換文字列。
     */
    private static String REPLACE_WORD;

    /**
     * 検索モードのENUM。
     *
     * @author TakumiEra
     */
    enum SearchMode {
        /**
         * あいまい検索。
         */
        FUZZY,
        /**
         * 完全一致検索。
         */
        STRICTLY;
    }

    /**
     * 本処理。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws IOException {
        // 引数から情報を取得
        String searchDirPath = args[0];
        SEARCH_WORD = args[1];
        SEARCH_MODE = SearchTypes.valueOf(args[2]);
        // 置換処理は任意
        if (args.length > 3) {
            REPLACE_WORD = args[3];
        }

        // メッセージを表示
        System.out.println("以下の条件でgrep検索を実行します。");
        System.out.println("検索対象フォルダ：" + searchDirPath);
        System.out.println("検索文字列：" + SEARCH_WORD);
        System.out.println("検索方法：" + SEARCH_MODE);
        if (args.length > 3) {
            System.out.println("置換文字列：" + REPLACE_WORD);
        }

        ExcelGrep excel = new ExcelGrep(SEARCH_WORD, SEARCH_MODE, null);
        excel.grepOutTempFile(searchDirPath);
        excel.moveTempFile();

        WordGrep word = new WordGrep(SEARCH_WORD, SEARCH_MODE, null);
        word.grepOutTempFile(searchDirPath);
        word.moveTempFile();

        System.out.println("検索＼(^o^)／ｵﾜﾀｰ");

    }

}
