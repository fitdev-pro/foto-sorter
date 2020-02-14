package UserInterface;

import Application.Gallery.AddNewGalleryService;
import Application.Gallery.InitGalleryService;
import Application.Renaming.GetFilesRenamingDetails;
import Domain.FileResult;
import Infrastructure.DependencyContainer;
import UserInterface.Components.Dialogs.InfoDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
    private TableColumn<FileResult,String> tableColumnSource;
    @FXML
    private TableColumn<FileResult, String> tableColumnResult;

    @FXML
    private ImageView image;
    @FXML
    private Label imageLabel;

    @FXML
    private void rootGalleryChoseAction(ActionEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        if(dir != null){
            DependencyContainer.getInstance().getServiceDispatcher().dispatch(new InitGalleryService(dir.getAbsolutePath()));
            newGalleryPathTextField.setDisable(false);
            newGalleryPathTextField.setText( "" );
            newGalleryChoseBtn.setDisable(false);
            newGalleryLabel.setDisable(false);
            stage.setTitle("Foto sorter - "+dir.getAbsolutePath());
        }
    }

    @FXML
    private void newGalleryChoseAction(ActionEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        if(dir != null){
            DependencyContainer.getInstance().getServiceDispatcher().dispatch(new AddNewGalleryService(dir.getAbsolutePath()));
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
    private void checkNamesAction(ActionEvent event) {
        this.updateList(new GetFilesRenamingDetails(this.repository).invoke());
        this.tableColumnResult.setCellFactory(TextFieldTableCell.forTableColumn());
        this.tableColumnResult.setOnEditCommit((TableColumn.CellEditEvent<FileResult, String> t) -> {
            FileResult f = t.getTableView().getItems().get(t.getTablePosition().getRow());
            f.setResult(t.getNewValue());
        });

        if (this.tableView.getItems().size() > 0) {
            this.cancel.setDisable(false);
            this.run.setDisable(false);
            this.run.setOnAction(new RenameActionHandler(this.tableView.getItems()));

//        this.tableView.setEditable(true);
//        this.tableView.setRowFactory(param -> {
//            TableRow<FileResult> row = new TableRow<>();
//            row.setOnMouseClicked(eventClick -> {
//                if (eventClick.getClickCount() == 2 && (! row.isEmpty()) ) {
//                    InfoDialog.show("Hura", "Coś trzeba zrobić z tymi plikami.");
//                }
//            });
//            return row ;
//        });

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    String path = obs.getValue().getSource();
                    File file = new File(path);
                    this.image.setImage(new Image(file.toURI().toString()));
                    this.imageLabel.setText("File: " + path);
                }
            });
        }
    }

    private static class RenameActionHandler implements EventHandler<ActionEvent>{
        private Collection<FileResult> items;

        public RenameActionHandler(Collection<FileResult> items) {
            this.items = items;
        }

        @Override
        public void handle(ActionEvent event) {
            int i = 0;
            int all = items.size();

            for (FileResult item: items) {
                File oldFile = new File(item.getSource());
                File newFile = new File(item.getResult());

                if (!newFile.exists()) {
                    boolean success = oldFile.renameTo(newFile);
                    if (success) {
                        i++;
                    }
                }
            }

            InfoDialog.show("Hura", "Udało się pozmieniać nazwy plików("+i+"/"+all+")");
        }
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
        this.updateList(new GetFilesRenamingDetails(this.repository).invoke());
        this.tableColumnResult.setCellFactory(TextFieldTableCell.forTableColumn());
        this.tableColumnResult.setOnEditCommit((TableColumn.CellEditEvent<FileResult, String> t) -> {
            FileResult f = t.getTableView().getItems().get(t.getTablePosition().getRow());
            f.setResult(t.getNewValue());
        });

        if (this.tableView.getItems().size() > 0) {
            this.cancel.setDisable(false);
            this.run.setDisable(false);
            this.run.setOnAction(new RenameActionHandler(this.tableView.getItems()));

//        this.tableView.setEditable(true);
//        this.tableView.setRowFactory(param -> {
//            TableRow<FileResult> row = new TableRow<>();
//            row.setOnMouseClicked(eventClick -> {
//                if (eventClick.getClickCount() == 2 && (! row.isEmpty()) ) {
//                    InfoDialog.show("Hura", "Coś trzeba zrobić z tymi plikami.");
//                }
//            });
//            return row ;
//        });

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    String path = obs.getValue().getSource();
                    File file = new File(path);
                    this.image.setImage(new Image(file.toURI().toString()));
                    this.imageLabel.setText("File: " + path);
                }
            });
        }
    }
}


