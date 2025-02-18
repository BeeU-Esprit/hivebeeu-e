package tn.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.util.DBConnection;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("JavaFX CRUD Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DBConnection.getConnection(); // Initialize database connection
        launch(args);
    }
}