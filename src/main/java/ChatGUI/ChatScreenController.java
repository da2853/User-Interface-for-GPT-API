package ChatGUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class ChatScreenController {
    public Button ModeButton;
    public Button resetButton;
    public Button downloadButton;
    public ComboBox modelButton;
    public Button sendButton;
    public TextArea userText;
    @FXML
    private TextArea chatArea;

    public void initialConfig(){
        chatArea.appendText("Enter a Prompt for ChatGPT.");
        userText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    // When focus is gained
                    if (userText.getText().equals("Type here...")) {
                        userText.setText("");
                    }
                } else {
                    // When focus is lost
                    if (userText.getText().isEmpty()) {
                        userText.setText("Type here...");
                    }
                }
            }
        });
    }

    public void changeMode(ActionEvent actionEvent) {
        // Your code here
    }

    public void resetChat(ActionEvent actionEvent) {
        // Your code here
    }

    public void downloadChat(ActionEvent actionEvent) {
        // Your code here
    }

    public void changeModel(ActionEvent actionEvent) {
        // Your code here
    }

    public void sendMessage(ActionEvent actionEvent) {
        // Your code here
    }
}
