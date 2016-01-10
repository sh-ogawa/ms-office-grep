package jp.sh4.ooga.ms.grep.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jp.sh4.ooga.ms.grep.SearchTypes;
import jp.sh4.ooga.ms.grep.excel.ExcelGrep;
import jp.sh4.ooga.ms.grep.word.WordGrep;

import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML
    private TextField searchWord;

    @FXML
    private TextField searchDir;

    @FXML
    private TextField resultOutPath;

    /**
     * 参照ボタン(検索ディレクトリ)クリック時のイベント
     * @param event
     */
    @FXML
    protected void referenceInDir(final ActionEvent event){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("検索ディレクトリ");
        File f = chooser.showDialog(null);
        if(f == null) return;
        searchDir.setText(f.getAbsolutePath());
    }

    /**
     * 参照ボタン(出力ファイル)クリック時のイベント
     * @param event
     */
    @FXML
    protected void referenceOutFile(final ActionEvent event){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("出力ファイル");
        File f = chooser.showOpenDialog(null);
        if(f == null) return;
        resultOutPath.setText(f.getAbsolutePath());
    }

    /**
     * 実行ボタンクリック時のイベント
     * @param event
     */
    @FXML
    protected void searchExecute(final ActionEvent event){

        ExcelGrep excel = new ExcelGrep(searchWord.getText(), SearchTypes.STRICTLY);
        try {
            excel.grepOutTempFile(searchDir.getText());
            excel.moveTempFile();
        } catch (IOException e) {
            System.out.println("Excelファイルの検索に失敗しました");
            e.printStackTrace();
        }

        WordGrep word = new WordGrep(searchWord.getText(), SearchTypes.STRICTLY);
        try {
            word.grepOutTempFile(searchDir.getText());
            word.moveTempFile();
        } catch (IOException e) {
            System.out.println("Wordファイルの検索に失敗しました");
            e.printStackTrace();
        }
    }
}
