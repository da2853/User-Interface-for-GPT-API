package ChatGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatSettings extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ChatScreenGUI/chat-settings.fxml")); // Please adjust the path to your .fxml file accordingly.
        Parent root = fxmlLoader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Chat - NYU Engineering 2023");
        primaryStage.setResizable(false);
        primaryStage.show();
        ChatSettingsController controller = fxmlLoader.getController();
    }
}
