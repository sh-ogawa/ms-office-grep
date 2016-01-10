package jp.sh4.ooga.ms.grep.out;


import java.io.IOException;
import java.io.OutputStream;

/**
 * Grep結果をストリームに書き出します。
 *
 * @author sh-ogawa
 * @since 0.1.0
 */
public class GrepResultOut {

    private OutputStream out;
    private String separator;

    /**
     * コンストラクタ
     *
     * @param out Grep結果を書き出すOutputStream
     */
    public GrepResultOut(final OutputStream out) {
        this(out, "\t");
    }

    /**
     * コンストラクタ
     *
     * @param out       Grep結果を書き出すOutputStream
     * @param separator 検索結果の区切り文字
     */
    public GrepResultOut(final OutputStream out, final String separator) {
        this.out = out;
        this.separator = separator;
    }

    /**
     * 結果をストリームに書き出します。
     *
     * @param outData ストリームに書き出す文字列
     */
    public void wirte(final String... outData) throws IOException {
        //TODO:後でちゃんとフォーマット化する。
//        for (String data : outData) {
//            final String outStr = data + separator;
//            out.write(outStr.getBytes());
//        }
        //Excelのハイパーリンク使って飛べるようにしておく
        String link = "=HYPERLINK(\"[" + outData[0] + "]" + outData[1] + "!" + outData[2] + "\",\"" + outData[0] + "\")"
                + System.getProperty("line.separator");
        out.write(link.getBytes());
        out.write(outData[3].getBytes());
        out.write(System.getProperty("line.separator").getBytes());
    }
}
