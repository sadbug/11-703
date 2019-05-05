package sample.Views;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import sample.Models.Field;

/**
 * Created by пользователь on 12.12.2016.
 */
public class FieldView {
    private static Field field;
    private static Pane pane;
    private PlayView playView;

    static {
        pane = new Pane();
        pane.setStyle("-fx-background-color: #000;");
    }

    public FieldView(PlayView playView) {
        this.playView = playView;
        field = new Field(this);
        field.createField();

        for (int i = 0; i < field.getCells().length; i++) {
            for (int j = 0; j <  field.getCells()[i].length; j++) {
                pane.getChildren().add(field.getCells()[i][j]);
            }
        }
    }

//    public void updateFieldView(int h, int w) {
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                pane.setMaxWidth(358);
////                pane.getChildren().remove(field.getCells()[h][w]);
////                pane.getChildren().add(field.getCells()[h][w]);
//            }
//        });
//    }

    public Pane getPane() { return pane;}

    public static Field getField() {
        return field;
    }

    public PlayView getPlayView() {
        return playView;
    }
}
