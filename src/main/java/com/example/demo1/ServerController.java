package com.example.demo1;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.Socket;
import java.util.Dictionary;
import java.util.concurrent.CompletableFuture;

public class ServerController {
    @FXML
    ListView<String> filesServer;

    public void initialize() {
        Reload();
    }

    public void Reload(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(
                new File("C:\\Users\\Alex\\IdeaProjects\\demo1\\src\\main\\java\\images"));

         File[] files = directoryChooser.getInitialDirectory().listFiles();

        for (int i = 0; i < files.length; i++) {
            filesServer.getItems().add(files[i].getAbsolutePath());
        }
        filesServer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String path = filesServer.getSelectionModel().getSelectedItem();

                ImageView imageView = new ImageView(path);
                StackPane panel = new StackPane();
                panel.getChildren().add(imageView);
                Scene newScene = new Scene(panel, 500, 500);
                Stage stage = new Stage();
                stage.setTitle(path);
                stage.setScene(newScene);

                stage.show();
            }
        });
    }
}
