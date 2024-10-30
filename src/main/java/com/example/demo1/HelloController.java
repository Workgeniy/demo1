package com.example.demo1;

import javafx.application.Platform;
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
    Socket clientSocket = new Socket("localhost", 5000);
    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
    File path;
    Logger log = new Logger();

    @FXML
    private ImageView imageView;
    @FXML
    private MediaView mediaView;
    @FXML
    private Label label;
    @FXML
    private Label logger;

    public HelloController() throws IOException {
        log = new Logger();
    }



    @FXML
    protected void onSendButtonClick() throws IOException {
        if (imageView.getImage() == null) {
            logger.setText("файл не корректный");
            return;
        }
        CompletableFuture.runAsync(() -> {
        try {
            Platform.runLater(new Runnable() { // так как в другом потоке происходит
                @Override
                public void run() {
                    logger.setText(log.MessageLogger("чтение файла " + path.getName()));
                }
            });

            byte [] array = Files.readAllBytes(Paths.get(path.getPath()));
            out.writeObject(array);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    logger.setText(log.MessageLogger("отправка файла " + path.getName()));
                }
            });
            //out.flush();
        }

        catch (IOException e){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    logger.setText(log.MessageLogger("не удалась отправка отправка файла " + path.getName()));
                }});
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
                label.setText(path.getName());
            }
        }
    }
}
