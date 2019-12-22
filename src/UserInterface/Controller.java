package UserInterface;

import Application.InitGalleryService;
import Application.OpenGalleryService;
import Domain.Gallery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {

    @FXML
    private HBox newGalleryPathPane;

    @FXML
    private TextField newGalleryPathTextField;

    private Gallery gallery;

    @FXML
    private void rootGalleryChoseAction(ActionEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        if(dir != null){
            File[] matches = dir.listFiles((dir1, name) -> name.equals("gallerySettings.json"));

            if(matches != null && matches.length == 1){
                this.gallery = OpenGalleryService.dispath(dir.getAbsolutePath(),"");
            }else{
                this.gallery = InitGalleryService.dispath(dir.getAbsolutePath());
                // nowy plik
            }
        }
    }

    @FXML
    private void newGalleryChoseAction(ActionEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if(file != null){
            newGalleryPathTextField.setText( file.getAbsolutePath() );
        }
    }

    @FXML
    private void checkNamesAction(ActionEvent event){
        String newGalleryPathString = newGalleryPathTextField.getText();


    }

    @FXML
    private void checkDuplicatesAction(ActionEvent event){
        String newGalleryPathString = newGalleryPathTextField.getText();


    }

    @FXML
    private void checkOrderAction(ActionEvent event){
        String newGalleryPathString = newGalleryPathTextField.getText();


    }
}
