package ChatGUI;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChatScreenController {
    public Button ModeButton, resetButton, downloadButton, sendButton;
    public ComboBox<String> modelButton;
    public TextArea userText, chatArea;
    private ChatConnection chat;
    private final StringBuilder chatHistory = new StringBuilder();

    public void initialConfig(ChatConnection con) {
        chat = con;
    }

    public void resetChat(ActionEvent actionEvent) {
        chat.defaultSettings();
        addToDisplay("System: Chat Settings Have Been Reset to Default.");
    }

    public void changeModel(ActionEvent actionEvent) {
        chat.MODEL = modelButton.getValue();
    }

    public void sendMessage(ActionEvent actionEvent) {
        String txt = userText.getText();
        addToDisplay("User: " + txt);
        String responseText = chat.sendToGPT(txt);

        // Remove extra spaces
        responseText = responseText.replaceAll("\\s+", " ").trim();

        if (responseText.matches("(\\d+\\..*,)+\\d+\\..*")) {
            addToDisplay("GPT: " + responseText.trim());
        } else {
            String[] sentences = responseText.split("(?<=[?!.])(?!\\d)\\s+(?=\\p{Lu})");

            for (String sentence : sentences) {
                String trimmedSentence = sentence.trim();
                if (!trimmedSentence.isEmpty() && trimmedSentence.length() > 45) {
                    addToDisplay("GPT: " + trimmedSentence);
                }
            }
        }
    }

    public void openSettings(ActionEvent actionEvent) throws IOException {
        ChatSettings settings = new ChatSettings();
        settings.start(new Stage());
        settings.setChat(chat);
    }

    public void addToDisplay(String message) {
        chatArea.appendText(message + "\n");
        chatHistory.append(message).append("\n");
    }

    public void downloadChat(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chat History");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(chatHistory.toString());
                addToDisplay("System: Chat history downloaded successfully.");
            } catch (IOException e) {
                addToDisplay("System: Failed to download chat history.");
                e.printStackTrace();
            }
        }
    }

    public void clearChat(ActionEvent actionEvent) {
        chatArea.clear();
        chatHistory.setLength(0);
    }
}
