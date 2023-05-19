package ChatGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatScreen extends Application {
    public void launchApp(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ChatScreen.class.getResource("chat-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
//        stage.setTitle("GPT User Interface - NYU Engineering 2023");
//        stage.setScene(scene);
//        MainScreenController controller = fxmlLoader.getController();
//        controller.setStage(stage);
//        stage.show();

    }

}
