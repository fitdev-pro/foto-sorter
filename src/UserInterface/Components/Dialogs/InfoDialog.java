package UserInterface.Components.Dialogs;

import javafx.scene.control.Alert;

public class InfoDialog {
    public static void show(String title, String description){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info Dialog");
        alert.setHeaderText(title);
        alert.setContentText(description);

        alert.showAndWait();
    }
}
