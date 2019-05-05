package sample.Views;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.Control.Logic;
import sample.Models.Field;

public class Cell extends StackPane {
    private int value;
    private int x;
    private int y;
    private Rectangle rec;
    private Field field;

    private final int WIDTH = 120;
    private final int HEIGHT = 120;
    private final int DELT = 2;


    public Cell(int value, int h, int w, Field field) {

        setWidth(WIDTH);
        setHeight(HEIGHT);
        setLayoutX(WIDTH * w);
        setLayoutY(HEIGHT * h);

        this.value = value;
        this.x = w;
        this.y = h;
        this.field = field;

        rec = new Rectangle(WIDTH - DELT, HEIGHT - DELT);
        rec.setFill(Color.WHITE);
        getChildren().add(rec);

        setOnMousePressed(event -> {
            if (Logic.me) {
                if (value == 0) {
                    field.open(y, x);
                }
            }
        });
    }





    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}