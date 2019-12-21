package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    @FXML
    private AnchorPane paths;

    @FXML
    private TextField rootGalleryPath;

    @FXML
    private TextField newGalleryPath;

    @FXML
    private void rootGalleryChoseAction(ActionEvent event){
        rootGalleryPath.setText(this.getPath());
    }

    @FXML
    private void newGalleryChoseAction(ActionEvent event){
        newGalleryPath.setText(this.getPath());
    }

    private String getPath(){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) paths.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if(file != null){
            return file.getAbsolutePath();
        }

        return "";
    }
}
