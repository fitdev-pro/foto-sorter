package UserInterface;

import Application.Gallery.AddNewGalleryService;
import Application.Gallery.IGalleryRepository;
import Application.Gallery.InitGalleryService;
import Infrastructure.GalleryRepository;
import UserInterface.Components.Dialogs.ExceptionDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {

    @FXML
    private HBox newGalleryPathPane;

    @FXML
    private TextField newGalleryPathTextField;
    @FXML
    private Button newGalleryChoseBtn;
    @FXML
    private Label newGalleryLabel;

    @FXML
    private ListView list;

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
                newGalleryPathTextField.setDisable(false);
                newGalleryPathTextField.setText( "" );
                newGalleryChoseBtn.setDisable(false);
                newGalleryLabel.setDisable(false);
                stage.setTitle("Foto sorter - "+dir.getAbsolutePath());
            } catch (IOException e) {
                ExceptionDialog.show(e);
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
    private void newGalleryChangeAction(ActionEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        if(dir != null){
            new AddNewGalleryService(this.repository).invoke(dir.getAbsolutePath());
            newGalleryPathTextField.setText( dir.getAbsolutePath() );
        }
    }

    private List<String> getFiles(){
        try (Stream<Path> walk = Files.walk(Paths.get(this.repository.fetch().getNewGalleryDir()))) {
            return walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
        } catch (IOException e) {
            ExceptionDialog.show(e);
            return null;
        }
    }

    private void updateList(List<String> list){
        ObservableList<String> items = FXCollections.observableArrayList(list);

        this.list.setItems(items);
    }

    @FXML
    private void checkNamesAction(ActionEvent event){
        this.updateList(this.getFiles());
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
