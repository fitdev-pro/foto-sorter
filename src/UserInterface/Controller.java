package UserInterface;

import Application.DeletingService;
import Application.GalleryService;
import Application.RenamingService;
import Domain.FileResult;
import Infrastructure.DependencyContainer;
import Infrastructure.GalleryNotInitException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
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
    private Button copiesBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button moveBtn;

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
    private Text message;
    @FXML
    private BorderPane messagePane;
    private MessageBox messageBox;
    private DetailsBox detailsBox;

    public Controller() {
        messageBox = new MessageBox();
        detailsBox = new DetailsBox();
    }

    @FXML
    private void rootGalleryChoseAction(ActionEvent event) throws GalleryNotInitException {
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        if(dir != null){
            GalleryService galleryService = DependencyContainer.getInstance().getGalleryService();
            galleryService.init(dir.getAbsolutePath());

            newGalleryPathTextField.setDisable(false);
            newGalleryPathTextField.setText( "" );
            newGalleryChoseBtn.setDisable(false);
            newGalleryLabel.setDisable(false);
            stage.setTitle("Foto sorter - "+dir.getAbsolutePath());
        }
    }

    @FXML
    private void newGalleryChoseAction(ActionEvent event) throws GalleryNotInitException {
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        Stage stage = (Stage) newGalleryPathPane.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);

        if(dir != null){
            GalleryService galleryService = DependencyContainer.getInstance().getGalleryService();
            galleryService.setNewGalleryPath(dir.getAbsolutePath());

            newGalleryPathTextField.setText( dir.getAbsolutePath() );
            changeNameBtn.setDisable(false);
            copiesBtn.setDisable(false);
            deleteBtn.setDisable(false);
            moveBtn.setDisable(false);
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
    }

    @FXML
    private void checkNamesAction(ActionEvent event) throws GalleryNotInitException {
        messageBox.hideMessage();
        detailsBox.hideInfo();

        RenamingService renamingService = DependencyContainer.getInstance().getRenamingService();
        this.updateList(renamingService.fetchNewFiles());

        if (this.tableView.getItems().size() > 0) {
            this.run.setDisable(false);
            this.run.setOnAction(new RenameActionHandler(this.tableView.getItems(), messageBox, detailsBox));

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    detailsBox.showInfo(obs.getValue().getSource());
                }
            });
        }else{
            messageBox.showInfo("Brak plików dla których trzeba zmienić nazwę.");
        }
    }

    private static class RenameActionHandler implements EventHandler<ActionEvent>{
        private Collection<FileResult> items;
        private MessageBox messageBox;
        private DetailsBox detailsBox;

        public RenameActionHandler(Collection<FileResult> items, MessageBox messageBox, DetailsBox detailsBox) {
            this.items = items;
            this.messageBox = messageBox;
            this.detailsBox = detailsBox;
        }

        @Override
        public void handle(ActionEvent event) {
            detailsBox.hideInfo();
            messageBox.hideMessage();

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

            messageBox.showInfo("Udało się pozmieniać nazwy plików("+i+"/"+all+")");

            this.items.clear();
        }
    }

    @FXML
    private void checkDuplicatesAction(ActionEvent event) throws GalleryNotInitException {
        messageBox.hideMessage();
        detailsBox.hideInfo();

        RenamingService renamingService = DependencyContainer.getInstance().getRenamingService();
        this.updateList(renamingService.fetchNewFiles());


        if (this.tableView.getItems().size() > 0) {
            this.run.setDisable(false);
//            this.run.setOnAction(new RenameActionHandler(this.tableView.getItems(), messageBox));

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    detailsBox.showInfo(obs.getValue().getSource());
                }
            });
        }
    }

    @FXML
    private void deleteAction(ActionEvent event) throws GalleryNotInitException {
        messageBox.hideMessage();
        detailsBox.hideInfo();

        DeletingService deletingService = DependencyContainer.getInstance().getDeletingService();
        this.updateList(deletingService.fetchNewFiles());


        if (this.tableView.getItems().size() > 0) {
            Scene scene = newGalleryPathPane.getScene();

            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.DELETE) {
                    FileResult item = this.tableView.getSelectionModel().getSelectedItem();
                    this.tableView.getItems().removeAll(item);
                    messageBox.showInfo("Usunięto: "+item.getSource());
                    detailsBox.hideInfo();
                }
            });

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    detailsBox.showInfo(obs.getValue().getSource());
                }
            });
        }
    }

    @FXML
    private void moveAction(ActionEvent event) throws GalleryNotInitException {
        messageBox.hideMessage();
        detailsBox.hideInfo();

        RenamingService renamingService = DependencyContainer.getInstance().getRenamingService();
        this.updateList(renamingService.fetchNewFiles());

        if (this.tableView.getItems().size() > 0) {
            this.run.setDisable(false);
//            this.run.setOnAction(new RenameActionHandler(this.tableView.getItems(), messageBox));

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    detailsBox.showInfo(obs.getValue().getSource());
                }
            });
        }
    }

    private class MessageBox{
        public void showInfo(String messageStr){
            messagePane.setStyle("-fx-background-color: lightgreen;");
            showMessage(messageStr);
        }

        public void showError(String messageStr){
            messagePane.setStyle("-fx-background-color: red;");
            showMessage(messageStr);
        }

        private void showMessage(String messageStr){
            messagePane.setVisible(true);
            message.setText(messageStr);
        }

        public void hideMessage(){
            messagePane.setVisible(false);
        }
    }

    private class DetailsBox{
        private String path;

        public void showInfo(String path){
            this.path = path;

            File file = new File(path);
            image.setImage(new Image(file.toURI().toString()));
            image.setVisible(true);
            imageLabel.setText("File: " + path);
            imageLabel.setVisible(true);
        }

        public void hideInfo(){
            image.setVisible(false);
            imageLabel.setVisible(false);
            path = null;
        }

        public String getSelectedFilePath(){
            return path;
        }
    }
}


