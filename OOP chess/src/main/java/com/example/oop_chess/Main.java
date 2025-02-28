package com.example.oop_chess;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage Menu) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MenuGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Menu.setFullScreen(true);
        Menu.setFullScreenExitHint("");  // Optionally hide the exit hint
        Menu.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);  // Disable fullscreen exit shortcut
        Menu.setTitle("Biko Chess");
        Menu.setScene(scene);
        Menu.setOnCloseRequest(event -> {

            try {
                Game.stopTimer();
            } catch (Exception e) {
                System.out.println("timer isn't running");
            } finally {
                Save_And_Load.write();
                Platform.exit();
            }


        });
        Menu.show();

    }

    public static void main(String[] args) {
        launch();
    }
}