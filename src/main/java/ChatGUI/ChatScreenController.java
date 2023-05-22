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
    @FXML
    public TextArea userText;
    @FXML
    public TextArea chatArea;

    public ChatConnection chat;

    public void initialConfig(ChatConnection con){
        chat = con;
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

    public void addToDisplay(String message) {
        chatArea.appendText(message);
    }

    public void resetChat(ActionEvent actionEvent) {
        chat.defaultSettings();
        addToDisplay("System: Chat Settings Have Been Reset to Default.");
    }

    public void downloadChat(ActionEvent actionEvent) {
        // Your code here
    }

    public void changeModel(ActionEvent actionEvent) {
        chat.MODEL = (String) modelButton.getValue();

    }

    public void sendMessage(ActionEvent actionEvent) {
        String txt = userText.getText();
        userText.setText("Waiting for Response...");
        addToDisplay("User: " + txt + "\n");
        String responseText = chat.sendToGPT(txt);
        String[] sentences = responseText.split("\\.");
        for (String sentence : sentences) {
            if (!sentence.isEmpty()) {
                addToDisplay("GPT: " + sentence.trim() + ".\n");
            }
        }
        userText.setText("Type here...");
    }
}
