package jp.sh4.ooga.ms.grep.gui.entity;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by sh-ogawa on 2015/09/13.
 */
public class SearchResult {

    private Semaphore semaphore = null;

    public SearchResult(){}

    /**
     * Grepを実行します
     * @param searchPath 検索を行うrootパス
     * @param searchWord 検索ワード
     * @return 検索結果Entityのリスト
     */
    public List<SearchResult> searchByWord(final Path searchPath, final String searchWord){

        final List<SearchResult> resultList = new LinkedList<>();


        return resultList;

    }



}
