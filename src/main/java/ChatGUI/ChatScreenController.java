package ChatGUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    public void resetChat(ActionEvent actionEvent) {
        chat.defaultSettings();
        addToDisplay("System: Chat Settings Have Been Reset to Default.");
    }



    public void changeModel(ActionEvent actionEvent) {
        chat.MODEL = (String) modelButton.getValue();

    }

    public void sendMessage(ActionEvent actionEvent) {
        String txt = userText.getText();
        userText.setText("Waiting for Response...");
        addToDisplay("User: " + txt + "\n");
        String responseText = chat.sendToGPT(txt);
        String[] sentences = responseText.split("[?!.]");
        for (String sentence : sentences) {
            if (!sentence.isEmpty() && sentence.trim().length() > 50) {
                addToDisplay("GPT: " + sentence.trim() + ".\n");
            }
        }
        userText.setText("Type here...");
    }


    public void openSettings(ActionEvent actionEvent) throws IOException {
        new ChatSettings();
    }

    private final StringBuilder chatHistory = new StringBuilder();

    public void addToDisplay(String message) {
        chatArea.appendText(message);
        chatHistory.append(message);
    }

    public void downloadChat(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chat History");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(chatHistory.toString());
                addToDisplay("System: Chat history downloaded successfully.\n");
            } catch (IOException e) {
                addToDisplay("System: Failed to download chat history.\n");
                e.printStackTrace();
            }
        }
    }

    public void clearChat(ActionEvent actionEvent) {
        chatArea.clear();
        chatHistory.setLength(0);
    }

}
