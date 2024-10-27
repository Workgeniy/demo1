package com.example.demo1;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Server extends Application {

    private ServerSocket server = null;
    private static final int port = 5000;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Server.class.getResource("server.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);

        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();

        server = new ServerSocket(port);
        System.out.println("Server started");

        listen();

    }

    public void listen () throws IOException {
        CompletableFuture.runAsync(() -> {
            while (true) {
                Socket newClient = null;
                try {
                    newClient = server.accept();
                    String clientId = newClient.getInetAddress().toString();
                    System.out.println("Подключен новый клиент: " + clientId);
                    ObjectInputStream ois = new ObjectInputStream(newClient.getInputStream());
                    //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    try {
                        byte[] message = (byte[])ois.readObject();

                        try {

                            DirectoryChooser directoryChooser = new DirectoryChooser();
                            directoryChooser.setInitialDirectory(
                                    new File("C:\\Users\\Alex\\IdeaProjects\\demo1\\src\\main\\java\\images"));

                            File[] files = directoryChooser.getInitialDirectory().listFiles();
                            String name = new String("message" + files.length + ".png");
                            FileOutputStream fos = new FileOutputStream(
                                    "src\\main\\java\\images\\" + name);
                            fos.write(message);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
