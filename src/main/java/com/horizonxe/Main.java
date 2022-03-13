package com.horizonxe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com.horizonxe/fis-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 780, 440);
        stage.setTitle("FIS Global");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}