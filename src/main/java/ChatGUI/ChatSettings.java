package ChatGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatSettings extends Application {
    private static final String TITLE = "Chat - NYU Engineering 2023";
    private static final String FXML_PATH = "/ChatScreenGUI/chat-settings.fxml";

    private ChatConnection chat;
    private ChatSettingsController controller;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public void setChat(ChatConnection c){
        this.chat = c;
        if (this.controller != null) {
            this.controller.init(stage, chat);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
            Parent root = fxmlLoader.load();
            this.controller = fxmlLoader.getController();
            if (this.chat != null) {
                this.controller.init(stage, chat);
            }
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(TITLE);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
