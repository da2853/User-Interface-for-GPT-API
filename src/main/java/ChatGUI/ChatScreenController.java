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
        String txt = userText.getText();
        userText.setText("");
        addToDisplay("User: " + txt + "\n");
        addToDisplay((chat.sendToGPT(txt)) + "\n");

    }
}
