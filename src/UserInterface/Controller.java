package UserInterface;

import Application.Gallery.AddNewGalleryService;
import Application.Gallery.IGalleryRepository;
import Application.Gallery.InitGalleryService;
import Infrastructure.GalleryRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller {

    @FXML
    private HBox newGalleryPathPane;

    @FXML
    private TextField newGalleryPathTextField;

    private IGalleryRepository repository;

    public Controller() {
        this.repository = new GalleryRepository();
    }

    @FXML
    private void rootGalleryChoseAction(ActionEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        if(dir != null){
            try {
                new InitGalleryService(this.repository).invoke(dir.getAbsolutePath());
                newGalleryPathTextField.setText( "" );
                stage.setTitle("Foto sorter - "+dir.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void newGalleryChoseAction(ActionEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        if(dir != null){
            new AddNewGalleryService(this.repository).invoke(dir.getAbsolutePath());
            newGalleryPathTextField.setText( dir.getAbsolutePath() );
        }
    }

    @FXML
    private void checkNamesAction(ActionEvent event){
        String newGalleryPathString = newGalleryPathTextField.getText();

        System.out.println(this.repository.fetch().getRootGalleryDir());
        System.out.println(this.repository.fetch().getNewGalleryDir());
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
