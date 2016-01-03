package jp.sh4.ooga.grep;

/**
 * 検索モードを列挙する
 *
 * @author sh-ogawa
 * @since 0.1.0
 */
public enum SearchTypes {
    /**
     * あいまい検索。
     */
    FUZZY,
    /**
     * 完全一致検索。
     */
    STRICTLY;
}
