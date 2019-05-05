package sample.Views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Control.Logic;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;

public class PlayView extends Stage{


    private BufferedReader reader;
    private PrintWriter writer;
    private Group root;
    private static VBox front;
    public static FieldView fv;
    private Logic logic;


    public PlayView() {
        super();
        this.reader = null;
        this.writer = null;
        initialView();
    }



    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }


    public void initialView() {
        this.setTitle("Game");
        root = new Group();

        Scene scene = new Scene(root, 400, 350);
        this.setScene(scene);
    }

    public Scene chooseRoom(List<String> rooms) {
        root = new Group();
        Label title = new Label("Комнаты");
        final VBox[] vBox = {new VBox()};
        final VBox[] roomsView = {updateRooms(rooms)};
        Button createRoom = new Button("Создать комнату");
        Button updateRooms = new Button("Обновить список комнат");
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(65, 1);
        HBox hBox = new HBox();
        vBox[0].setPadding(new Insets(5, 0, 5, 5));
        hBox.getChildren().addAll(createRoom, spacer, updateRooms);
        hBox.setPadding(new Insets(10, 0 ,10 ,0));
        vBox[0].getChildren().addAll(title, hBox, roomsView[0]);

        updateRooms.setOnAction(event -> {
            vBox[0].getChildren().remove(roomsView[0]);
            roomsView[0] = updateRooms(logic.updateRooms());
            vBox[0].getChildren().add(roomsView[0]);
        });
        createRoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                logic.createRoom();
//                writer.println("create");
//                updateView(waitingOpponent());
            }
        });

        root.getChildren().add(vBox[0]);

        return new Scene(root, 400, 350);
    }

    public VBox updateRooms(List<String> rooms) {
        VBox vBox = new VBox();
        for (int i = 0; i < rooms.size(); i++) {
            HBox hBox = new HBox();
            Label label = new Label(rooms.get(i));
            label.setPadding(new Insets(3, 0, 0, 0));
            label.setStyle("-fx-min-width: 100px;");
            Button button = new Button("Присоединиться");
            int finalI = i;
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    logic.connectToRoom(rooms.get(finalI));
//                    writer.println("connect " + rooms.get(finalI));
                }
            });
            hBox.getChildren().add(label);
            hBox.getChildren().add(button);
            vBox.getChildren().add(hBox);
        }

        return vBox;
    }

    public Scene waitingOpponent(String name) {
        root = new Group();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0,0,0,5));
        Label label = new Label("Ожидание соперника...");
        Label roomName = new Label("Номер вашей комнаты:" + name);
        vBox.getChildren().add(label);
        vBox.getChildren().add(roomName);
        this.setResizable(false);

        Button exit = new Button("Выйти");
        vBox.getChildren().add(exit);

        exit.setOnAction(event -> {
            logic.exit();
//            writer.println("exit");
            updateView(chooseRoom(logic.updateRooms()));
        });

        root.getChildren().add(vBox);
        return new Scene(root, 400,350);
    }

    public Scene startPlay() {
        root = new Group();
        root.getChildren().remove(front);
        front = new VBox();

        fv = new FieldView(this);
        front.getChildren().add(fv.getPane());
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 0, 0, 0));
        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(5, 1);
        Button exit = new Button("Выйти");
        hBox.getChildren().addAll(spacer, exit);
        front.getChildren().add(hBox);
        root.getChildren().add(front);


//        root.getChildren().add(exit);
        exit.setOnAction(event -> {
            logic.exit();
//            writer.println("exit");
            Logic.me = false;
//            updateView(chooseRoom(logic.updateRooms()));
        });

        return new Scene(root, 370, 410);
    }

    public Scene printResult(String result) {
        root = new Group();
        VBox vBox = new VBox();
        Label label = new Label(result);
        label.setStyle("-fx-font-size: 20px;");
        Button button = new Button("Выйти");
        button.setOnAction(event -> {
//            System.out.println("you are " + result);
            updateView(chooseRoom(logic.updateRooms()));
        });

        vBox.getChildren().add(label);
        vBox.getChildren().add(fv.getPane());
        vBox.getChildren().add(button);
        root.getChildren().add(vBox);
        vBox.setPadding(new Insets(5, 5, 10, 5));

        return new Scene(root, 370, 440);
    }

    public void updateView(Scene scene) {
        this.setScene(scene);
        this.setResizable(false);
    }

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }
}
