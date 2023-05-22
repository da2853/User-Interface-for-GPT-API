package ChatGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatScreen extends Application {


    public ChatScreen(String mess, ChatConnection chat) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ChatScreenGUI/chat-screen.fxml")); // Please adjust the path to your .fxml file accordingly.
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Chat - NYU Engineering 2023");
        stage.show();
        ChatScreenController controller = fxmlLoader.getController();
        controller.initialConfig(chat);
        controller.addToDisplay("GPT: " + mess);
    }


    public void launchApp(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ChatScreenGUI/chat-screen.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("GPT User Interface - NYU Engineering 2023");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
