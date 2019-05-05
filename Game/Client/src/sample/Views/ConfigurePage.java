package sample.Views;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Client;
import sample.Control.Logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConfigurePage extends Stage {
    private Client client;

    public ConfigurePage(Client client) {
        this.client = client;
        initialView();
    }

    public void initialView() {
        this.setTitle("IIIIt is cool play");
        AnchorPane root = new AnchorPane();

        TextField ipAddress = new TextField();
        ipAddress.setLayoutX(92);
        ipAddress.setLayoutY(101);
        Label title = new Label("Введите IP адрес сервера");
        title.setLayoutX(98);
        title.setLayoutY(54);

        Button button = new Button("Подключиться");
        button.setLayoutX(237);
        button.setLayoutY(187);

        root.getChildren().addAll(title, ipAddress, button);

        button.setOnAction(event -> {
            String host = ipAddress.getText();
            Logic logic = new Logic(host);
            client.setPw(logic.getPw());
            client.setIs(logic.getIs());
            this.close();
        });


        Scene scene = new Scene(root, 400, 350);
        this.setScene(scene);

    }
}