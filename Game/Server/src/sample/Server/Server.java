package sample.Server;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Server extends Application{

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {

        launch(args);
    }
    final int PORT = 3456;
    private List<Connection> connections;
    private Map<String, Room> rooms;
    public static int count = 0;



    @Override
    public void start(Stage primaryStage) throws Exception {
    }


    public Server() throws IOException {
        connections = new ArrayList<>();
        rooms = new TreeMap<>();
        go();
    }

    public void go() throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(new Group(), 300, 300));
        stage.show();
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println(serverSocket.getInetAddress().getHostName());
        while (true) {
            Socket client = serverSocket.accept();
            Connection connection = new Connection(this, client);
            connection.updateRooms();
        }
    }


    public Map<String, Room> getRooms() {
        return rooms;
    }

}