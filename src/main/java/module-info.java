module katiba.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;

    opens app to javafx.graphics;
    opens frontend to javafx.fxml, javafx.graphics;

    exports app;
    exports frontend;
    exports backend;
    exports backend.models;
}
