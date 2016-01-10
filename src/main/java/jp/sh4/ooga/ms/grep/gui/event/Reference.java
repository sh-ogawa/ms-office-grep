package jp.sh4.ooga.ms.grep.gui.event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import jp.sh4.ooga.ms.grep.gui.Controller;

import java.io.File;

/**
 * Created by sh-ogawa on 2015/09/13.
 */
public class Reference extends ActionEvent{

    public Reference(){}

    @FXML
    public void reference(final ActionEvent envent){

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("ディレクトリ選択");

        File f = chooser.showDialog(null);
        if(f == null) return;

        Controller.searchDir = f.getAbsolutePath();


    }




}
