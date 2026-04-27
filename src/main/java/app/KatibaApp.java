package app;

import backend.DatabaseManager;
import frontend.MainWindow;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KatibaApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Eagerly initialise DB connection so we fail fast with a friendly dialog
            DatabaseManager.getInstance();
        } catch (Exception ex) {
            showDbError(primaryStage, ex);
            return;
        }

        MainWindow mainWindow = new MainWindow(primaryStage);

        Scene scene = new Scene(mainWindow.getRoot(), 1280, 780);
        String css = getClass().getResource("/styles/main.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("Katiba — Kenya Constitution 2010");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(960);
        primaryStage.setMinHeight(640);
        primaryStage.show();
    }

    @Override
    public void stop() {
        try {
            DatabaseManager.getInstance().close();
        } catch (Exception ignored) {}
        Platform.exit();
    }

    // ─── Friendly DB error dialog ──────────────────────────────────────────────

    private void showDbError(Stage owner, Exception ex) {
        Stage dialog = new Stage();
        dialog.setTitle("Database Connection Error");

        Label heading = new Label("⚠  Could not connect to MongoDB Atlas");
        heading.setStyle("-fx-font-size:16px; -fx-font-weight:700; -fx-text-fill:#ff4444;");

        Label hint = new Label(
            "Check your connection string in:\n" +
            "  src/main/resources/app/config.properties\n\n" +
            "It should look like:\n" +
            "  mongodb.uri=mongodb+srv://<user>:<pass>@<cluster>.mongodb.net/..."
        );
        hint.setStyle("-fx-font-size:13px; -fx-text-fill:#c8c6e8;");
        hint.setWrapText(true);

        TextArea detail = new TextArea(ex.getMessage());
        detail.setEditable(false);
        detail.setPrefHeight(100);
        detail.setStyle("-fx-control-inner-background:#1a1930; -fx-text-fill:#5a586e; -fx-font-size:11px;");

        Button close = new Button("Close");
        close.setStyle("-fx-background-color:#bb000030; -fx-text-fill:#ff4444; -fx-cursor:hand; -fx-padding:6 20 6 20;");
        close.setOnAction(e -> { dialog.close(); Platform.exit(); });

        VBox root = new VBox(16, heading, hint, detail, close);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color:#0e0e17;");
        root.setPrefWidth(560);

        dialog.setScene(new Scene(root));
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
