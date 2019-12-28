package UserInterface;

import Application.Gallery.AddNewGalleryService;
import Application.IRepository;
import Application.Gallery.InitGalleryService;
import Application.Renaming.GetFilesRenamingDetails;
import Domain.FileResult;
import Infrastructure.GalleryRepository;
import UserInterface.Components.Dialogs.ExceptionDialog;
import UserInterface.Components.Dialogs.InfoDialog;
import com.drew.imaging.ImageProcessingException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

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
    private Button run;
    @FXML
    private Button cancel;

    @FXML
    private Button changeNameBtn;

    @FXML
    private TableView<FileResult> tableView;
    @FXML
    private TableColumn<String, FileResult> tableColumnSource;
    @FXML
    private TableColumn<String, FileResult> tableColumnResult;

    @FXML
    private ImageView image;
    @FXML
    private Label imageLabel;

    private IRepository repository;

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
            changeNameBtn.setDisable(false);
        }
    }

    private void updateList(Collection<FileResult> list){
        this.tableColumnSource.setCellValueFactory(new PropertyValueFactory<>("source"));
        this.tableColumnResult.setCellValueFactory(new PropertyValueFactory<>("result"));

        this.tableView.getItems().setAll(list);
    }

    @FXML
    private void clear(){
        this.tableView.getItems().clear();
        this.run.setDisable(true);
        this.cancel.setDisable(true);
    }

    @FXML
    private void checkNamesAction(ActionEvent event) throws IOException {
        this.updateList(new GetFilesRenamingDetails(this.repository).invoke());
        this.cancel.setDisable(false);
        this.run.setDisable(false);
        this.run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                InfoDialog.show("Hura", "Coś trzeba zrobić z tymi plikami.");
            }
        });

//        this.tableView.setRowFactory(param -> {
//            TableRow<FileResult> row = new TableRow<>();
//            row.setOnMouseClicked(eventClick -> {
//                if (eventClick.getClickCount() == 2 && (! row.isEmpty()) ) {
//                    InfoDialog.show("Hura", "Coś trzeba zrobić z tymi plikami.");
//                }
//            });
//            return row ;
//        });

        this.tableView.getSelectionModel().selectedItemProperty().addListener( (obs, oldSelection, newSelection) -> {
            if(obs.getValue() != null) {
                String path = obs.getValue().getSource();
                File file = new File(path);
                this.image.setImage(new Image(file.toURI().toString()));
                this.imageLabel.setText("File: " + path);
            }
        });
    }

    @FXML
    private void checkDuplicatesAction(ActionEvent event){
        String newGalleryPathString = newGalleryPathTextField.getText();

        this.cancel.setDisable(false);
        this.run.setDisable(false);
        this.run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    @FXML
    private void checkOrderAction(ActionEvent event){
        String newGalleryPathString = newGalleryPathTextField.getText();

        this.cancel.setDisable(false);
        this.run.setDisable(false);
        this.run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }
}


