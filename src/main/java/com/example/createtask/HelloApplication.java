package com.example.createtask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 505);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        HelloController controller = fxmlLoader.getController();
        Timer timer = new Timer();

        TimerTask frameCounter = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.tickTimer();
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(frameCounter, 1000, 150);


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}