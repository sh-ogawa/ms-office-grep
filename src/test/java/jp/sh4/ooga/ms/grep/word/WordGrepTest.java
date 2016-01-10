package jp.sh4.ooga.ms.grep.word;

import jp.sh4.ooga.ms.grep.SearchTypes;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Word Grep Test Class.
 *
 * @author sh-ogawa
 * @since 0.1.0
 */
public class WordGrepTest {

    @Before
    public void setting() throws IOException {


    }

    @Test
    public void Wordドキュメントの検索を行う() throws IOException {

        WordGrep word = new WordGrep("Oracle", SearchTypes.FUZZY);
        word.grepOutTempFile("C:\\work\\DTEMS\\200開発環境");
        word.moveTempFile();

    }


}
