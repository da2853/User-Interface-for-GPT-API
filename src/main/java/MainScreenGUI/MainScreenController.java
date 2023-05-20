package MainScreenGUI;

import ChatGUI.ChatConnection;
import ChatGUI.ChatScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainScreenController {
    private static final String OPENAI_KEY_PATTERN = "^(sk|rk)-[a-zA-Z0-9]{32}$";
    public TextArea statusText;
    @FXML
    private PasswordField apiKey;
    @FXML
    private Stage stage;
    @FXML
    private Button chatButton;

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
    private void handleChatButtonAction(ActionEvent event) throws IOException {

        if (checkKey(apiKey.getText())) {
            ChatConnection chat = new ChatConnection();
            String[] connect = chat.initialize(apiKey.getText());
            if (Objects.equals(connect[0], "0")) {
                statusText.setText("Connection Established");
                new ChatScreen(connect[1], chat);
            }
            else if (Objects.equals(connect[0], "1")){
                statusText.setText(connect[1] + "\n");
            }
        }
    }


    private boolean checkKey(String key) {
        if (Objects.equals(key, "test")){
            return true;
        }

//        Pattern pattern = Pattern.compile(OPENAI_KEY_PATTERN);
//        Matcher matcher = pattern.matcher(key);
//
//        if (!matcher.matches()) {
//            statusText.setText("Status: Invalid OpenAI key format.");
//            return false;
//        }
        return true;
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
