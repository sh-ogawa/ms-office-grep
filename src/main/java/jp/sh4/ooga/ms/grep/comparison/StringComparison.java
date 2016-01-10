package jp.sh4.ooga.ms.grep.comparison;

import jp.sh4.ooga.ms.grep.SearchTypes;

/**
 * Grep検索において文字列比較を行う
 *
 * @author sh-ogawa
 * @since 0.1.0
 */
public final class StringComparison {

    // 隠蔽コンストラクタ
    private StringComparison() {
    }

    ;

    /**
     * 文字列検索を行います。
     * Excelのセル種別が文字列のものについて呼び出されることを想定しています。
     *
     * @param target   検索対象文字列
     * @param findWord 検索文字列
     * @param type     検索タイプ
     * @return true:含まれる　false:含まれない
     */
    public static boolean compare(final String target, final String findWord, final SearchTypes type) {

        switch (type) {
            case FUZZY:
                return target.contains(findWord);
            case STRICTLY:
                return target.equals(findWord);
            default:
                return false;
        }
    }


}
