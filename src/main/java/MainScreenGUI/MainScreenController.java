package MainScreenGUI;

import ChatGUI.ChatScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainScreenController {
    @FXML
    private Stage stage;
    @FXML
    private Button chatButton;
    private MainScreen javafxInstance;

    @FXML
    private Button textCompletionButton;

    @FXML
    private Button imageGenerationButton;

    @FXML
    private Button speechToTextButton;

    public void setStage(Stage s){
        stage = s;
    }
    @FXML
    private void handleChatButtonAction(ActionEvent event) {
        new ChatScreen().launchApp(new String[5]);
        stage.hide();
    }

    @FXML
    private void handleTextCompletionButtonAction(ActionEvent event) {
        // Handle Text Completion button action here
    }

    @FXML
    private void handleImageGenerationButtonAction(ActionEvent event) {
        // Handle Image Generation button action here
    }

    @FXML
    private void handleSpeechToTextButtonAction(ActionEvent event) {
        // Handle Speech to Text button action here
    }
}
