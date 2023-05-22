package MainScreenGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreen extends Application {
    private Stage stage;
    public static String[] arg;
    public static void main(String[] args) {
        arg = args;
        launch(args);


    }
    public void launchApp(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainScreen.class.getResource("main-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 500);
        stage.setTitle("GPT User Interface - NYU Engineering 2023");
        stage.setScene(scene);
        MainScreenController controller = fxmlLoader.getController();
        stage.setResizable(false);
        controller.setStage(stage);
        stage.show();

    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
