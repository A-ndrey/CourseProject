import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        BorderPane panel = FXMLLoader.load(getClass().getResource("view/main.fxml"));

        Scene scene = new Scene(panel, 800, 600);

        primaryStage.setTitle("CourseProject");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
