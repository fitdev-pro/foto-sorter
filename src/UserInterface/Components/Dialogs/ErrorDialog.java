package UserInterface.Components.Dialogs;

import javafx.scene.control.Alert;

public class ErrorDialog {
    public static void show(String title, String description){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(title);
        alert.setContentText(description);

        alert.showAndWait();
    }
}
