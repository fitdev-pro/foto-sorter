package UserInterface;

import Application.*;
import Domain.FileResult;
import Infrastructure.DependencyContainer;
import Application.GalleryNotInitException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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

        this.tableView.setRowFactory(row -> {
            TableRow<FileResult> rowNew = new TableRow<FileResult>(){
                    @Override
                    public void updateItem(FileResult item, boolean empty){
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setStyle("");
                        }else if (item.isChecked()) {
                            setStyle("-fx-background-color: tomato;");
                        } else {
                            setStyle("");
                        }
                    }

                };

                rowNew.setOnMouseClicked(eventClick -> {
                    if (eventClick.getClickCount() == 2 && (! rowNew.isEmpty()) ) {
                        rowNew.getItem().triggerCheck();
                    }
                });
                return rowNew;
            });

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

            RenamingService renamingService = DependencyContainer.getInstance().getRenamingService();
            String result = renamingService.renameFiles(items);

            messageBox.showInfo("Udało się pozmieniać nazwy plików("+result+")");

            items.clear();
        }
    }

    @FXML
    private void checkDuplicatesAction(ActionEvent event) throws GalleryNotInitException {
        messageBox.hideMessage();
        detailsBox.hideInfo();

        DuplicatesService duplicatesService = DependencyContainer.getInstance().getDuplicatesService();
        this.updateList(duplicatesService.fetchNewFiles());


        if (this.tableView.getItems().size() > 0) {
            this.run.setDisable(false);
            this.run.setOnAction(new DeleteActionHandler(this.tableView.getItems(), messageBox, detailsBox));

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    detailsBox.showInfo(obs.getValue().getSource());
                }
            });
        }
    }

    private static class RemoveDuplicatesActionHandler implements EventHandler<ActionEvent>{
        private Collection<FileResult> items;
        private MessageBox messageBox;
        private DetailsBox detailsBox;

        public RemoveDuplicatesActionHandler(Collection<FileResult> items, MessageBox messageBox, DetailsBox detailsBox) {
            this.items = items;
            this.messageBox = messageBox;
            this.detailsBox = detailsBox;
        }

        @Override
        public void handle(ActionEvent event) {
            detailsBox.hideInfo();
            messageBox.hideMessage();

            DuplicatesService duplicatesService = DependencyContainer.getInstance().getDuplicatesService();
            String results = duplicatesService.deleteDuplicates(items);

            messageBox.showInfo("Udało się usunąć pliki ("+results+")");

            items.clear();
        }
    }

    @FXML
    private void deleteAction(ActionEvent event) throws GalleryNotInitException {
        messageBox.hideMessage();
        detailsBox.hideInfo();

        DeletingService deletingService = DependencyContainer.getInstance().getDeletingService();
        this.updateList(deletingService.fetchNewFiles());


        if (this.tableView.getItems().size() > 0) {
            this.run.setDisable(false);
            this.run.setOnAction(new DeleteActionHandler(this.tableView.getItems(), messageBox, detailsBox));

            Scene scene = newGalleryPathPane.getScene();

            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.BACK_SPACE) {
                    FileResult item = this.tableView.getSelectionModel().getSelectedItem();
                    item.triggerCheck();
                    if(item.isChecked()){
                        item.setResult("Do usunięcia");
                    }
                }
            });

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    detailsBox.showInfo(obs.getValue().getSource());
                }
            });
        }
    }

    private static class DeleteActionHandler implements EventHandler<ActionEvent>{
        private Collection<FileResult> items;
        private MessageBox messageBox;
        private DetailsBox detailsBox;

        public DeleteActionHandler(Collection<FileResult> items, MessageBox messageBox, DetailsBox detailsBox) {
            this.items = items;
            this.messageBox = messageBox;
            this.detailsBox = detailsBox;
        }

        @Override
        public void handle(ActionEvent event) {
            detailsBox.hideInfo();
            messageBox.hideMessage();

            DeletingService deletingService = DependencyContainer.getInstance().getDeletingService();
            String results = deletingService.deleteSelectedFiles(items);

            messageBox.showInfo("Udało się usunąć pliki ("+results+")");

            items.clear();
        }
    }

    @FXML
    private void moveAction(ActionEvent event) throws GalleryNotInitException {
        messageBox.hideMessage();
        detailsBox.hideInfo();

        MoveService moveService = DependencyContainer.getInstance().getMoveService();
        this.updateList(moveService.fetchNewFiles());

        if (this.tableView.getItems().size() > 0) {
            this.run.setDisable(false);
            this.run.setOnAction(new MoveActionHandler(this.tableView.getItems(), messageBox, detailsBox));

            this.tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (obs.getValue() != null) {
                    detailsBox.showInfo(obs.getValue().getSource());
                }
            });
        }
    }

    private static class MoveActionHandler implements EventHandler<ActionEvent>{
        private Collection<FileResult> items;
        private MessageBox messageBox;
        private DetailsBox detailsBox;

        public MoveActionHandler(Collection<FileResult> items, MessageBox messageBox, DetailsBox detailsBox) {
            this.items = items;
            this.messageBox = messageBox;
            this.detailsBox = detailsBox;
        }

        @Override
        public void handle(ActionEvent event) {
            detailsBox.hideInfo();
            messageBox.hideMessage();

            MoveService moveService = DependencyContainer.getInstance().getMoveService();
            String results = moveService.moveFiles(items);

            messageBox.showInfo("Udało się przenieść pliki ("+results+")");

            items.clear();
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


