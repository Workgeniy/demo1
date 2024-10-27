package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class HelloController {
    Socket clientSocket = new Socket("192.168.1.160", 5000);
    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
    File path;

    @FXML
    ImageView imageView;
    @FXML
    MediaView mediaView;
    @FXML
    Label label;

    public HelloController() throws IOException {
    }

    @FXML
    protected void onSendButtonClick() throws IOException {
        if (imageView.getImage() != null) {
        }
        CompletableFuture.runAsync(() -> {
        try {

            byte [] array = Files.readAllBytes(Paths.get(path.getPath()));
            out.writeObject(array);
            //out.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }});
    }

    @FXML
    protected void onSelectButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text File", "*.txt"),
                new FileChooser.ExtensionFilter("Image File", "*.jpg", "*.gif", "*.png"),
                new FileChooser.ExtensionFilter("Media File", "*.mp3", "*.wav", "*.mp4"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        Stage stage = new Stage();
        File selectFile = fileChooser.showOpenDialog(stage);
        if (selectFile != null) {
             path = new File(selectFile.getPath());
            String fileStr = path.getPath().toString();
            if (fileStr.endsWith(".png") || fileStr.endsWith(".jpg") || fileStr.endsWith(".gif")) {
                Image image = new Image(selectFile.toURI().toString());
                imageView.setImage(image);
                imageView.setFitHeight(300);
                imageView.setFitWidth(300);
            } else if (fileStr.endsWith(".mp3") || fileStr.endsWith(".wav") || fileStr.endsWith(".mp4")) {
                MediaPlayer player = new MediaPlayer(new Media(fileStr));
                mediaView = new MediaView(player);
                player.play();
            } else if (fileStr.endsWith(".txt")){
                label = new Label(path.getName());
            }
        }
    }
}
