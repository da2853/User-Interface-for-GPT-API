package ChatGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChatSettingsController {
    private Stage stage;
    @FXML
    private TextField promptText;
    @FXML
    private TextField role1Text;
    @FXML
    private TextField role2Text;
    @FXML
    private TextField statusText;
    private ChatConnection chat;

    public ChatSettingsController() {
    }

    void init(Stage st, ChatConnection c){
        if (st == null || c == null) {
            throw new IllegalArgumentException("Stage and ChatConnection must not be null");
        }

        chat = c;
        stage = st;

        String[] values = chat.getSettings();
        if (values != null && values.length >= 3) {
            updateTextFields(values[0], values[1], values[2], "Ready");
        } else {
            updateTextFields(null, null, null, "Settings Error");
        }
    }

    public void handleBackButtonAction(ActionEvent actionEvent) {
        if (stage != null) {
            stage.close();
        }
    }

    public void handleSaveButtonAction(ActionEvent actionEvent) {
        if (chat != null) {
            chat.saveSettings(promptText.getText(), role1Text.getText(), role2Text.getText());
        }
    }

    private void updateTextFields(String prompt, String role1, String role2, String status) {
        if (promptText != null) {
            promptText.setText(prompt);
        }
        if (role1Text != null) {
            role1Text.setText(role1);
        }
        if (role2Text != null) {
            role2Text.setText(role2);
        }
        if (statusText != null) {
            statusText.setText(status);
        }
    }
}
