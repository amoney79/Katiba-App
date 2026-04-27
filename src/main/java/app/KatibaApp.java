package app;

import backend.DatabaseManager;
import frontend.MainWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KatibaApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainWindow mainWindow = new MainWindow(primaryStage);

        Scene scene = new Scene(mainWindow.getRoot(), 1280, 780);
        scene.getStylesheets().add(
            getClass().getResource("/styles/main.css").toExternalForm()
        );

        primaryStage.setTitle("Katiba — Kenya Constitution");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(960);
        primaryStage.setMinHeight(640);
        primaryStage.show();
    }

    @Override
    public void stop() {
        DatabaseManager.getInstance().close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
