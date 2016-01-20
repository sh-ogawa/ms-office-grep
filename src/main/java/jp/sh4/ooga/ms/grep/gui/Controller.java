package jp.sh4.ooga.ms.grep.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
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
    @FXML
    private CheckBox chkExcel;
    @FXML
    private CheckBox chkWord;

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
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("出力ファイル");
        File f = chooser.showDialog(null);
        if(f == null) return;
        resultOutPath.setText(f.getAbsolutePath());
    }

    /**
     * 実行ボタンクリック時のイベント
     * @param event
     */
    @FXML
    protected void searchExecute(final ActionEvent event){

        if(searchWord.getText() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFO");
            alert.setHeaderText("検索ワードを入力してください");
            alert.show();
            return;
        }

        if(!(chkExcel.isSelected() && chkWord.isSelected())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFO");
            alert.setHeaderText("チェックボックスを選択してください");
            alert.show();
            return;
        }

        if(chkExcel.isSelected()){
            ExcelGrep excel = new ExcelGrep(searchWord.getText(), SearchTypes.FUZZY, resultOutPath.getText());
            try {
                excel.grepOutTempFile(searchDir.getText());
                excel.moveTempFile();
            } catch (IOException e) {
                System.out.println("Excelファイルの検索に失敗しました");
                e.printStackTrace();
            }
        }

        if(chkWord.isSelected()){
            WordGrep word = new WordGrep(searchWord.getText(), SearchTypes.FUZZY, resultOutPath.getText());
            try {
                word.grepOutTempFile(searchDir.getText());
                word.moveTempFile();
            } catch (IOException e) {
                System.out.println("Wordファイルの検索に失敗しました");
                e.printStackTrace();
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFO");
        alert.setHeaderText("検索が終わりました");
        alert.show();

    }

    private void valide(){

    }

}
