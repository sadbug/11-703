package sample.Models;


import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.Control.Logic;
import sample.Views.Cell;
import sample.Views.FieldView;


public class Field {
    private static Cell[][] cells;
    private final int COUNT_HIGH = 3;
    private final int COUNT_WIDTH = 3;
    private FieldView fieldView;
    private Logic logic;

    private final String FILENAME_CROSS = "1.png";
    private final String FILENAME_ZERO = "2.png";


    public Field() {
        cells = new Cell[COUNT_HIGH][COUNT_WIDTH];
    }

    public Field(FieldView fieldView) {
        this.fieldView = fieldView;
        logic = fieldView.getPlayView().getLogic();
        cells = new Cell[COUNT_HIGH][COUNT_WIDTH];
    }

    public void createField(){
        for (int i = 0; i < COUNT_HIGH; i++) {
            for (int j = 0; j < COUNT_WIDTH; j++) {
                cells[i][j] = new Cell(0, i, j, this);
            }
        }
    }

    public boolean open(int i, int j) {
        if (cells[i][j].getValue() == 0){
            Image image = null;
            if (Logic.mark) {
                cells[i][j].setValue(1);
                image = new Image(FILENAME_CROSS);
            }
            else {
                cells[i][j].setValue(2);
                image = new Image(FILENAME_ZERO);
            }
            ImageView iv = new ImageView(image);
            iv.setFitHeight(cells[0][0].getHeight());
            iv.setFitWidth(cells[0][0].getWidth());
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    cells[i][j].getChildren().add(iv);
                }
            });

            if (Logic.me) {
                logic.sendStep(i, j, checkWin(i, j), checkAll());
            }

            Logic.mark = !Logic.mark;
            return true;
        }
        return false;
    }

    public boolean checkWin(int i, int j) {
        int mark = cells[i][j].getValue();
        boolean all = true;

        for (int m = 0; m < 3 && all; m++) {
            if (mark != cells[m][j].getValue()) {
                all = false;
            }
        }
        if (all) return true;
        all = true;
        for (int m = 0; m < 3 && all; m++) {
            if (mark != cells[i][m].getValue()) {
                all = false;
            }
        }
        if (all) return true;
        all = true;
        if ((i == 0 && j == 0) || (i == 2 && j == 2) || (i == 1 & j == 1)) {
            for (int m = 0; m < 3 && all; m++) {
                if (mark != cells[2-m][2-m].getValue()) {
                    all = false;
                }
            }
            if (all) return true;
        }

        all = true;
        if ((i == 0 && j == 2) || (i == 2 && j == 0) || (i == 1 && j == 1)) {
            for (int m = 0; m < 3 && all; m++) {
                if (mark != cells[2-m][m].getValue()) {
                    all = false;
                }
            }
            if (all) return true;
        }
        return false;

    }

    public boolean checkAll() {
        for (int i = 0; i < COUNT_HIGH; i++) {
            for (int j = 0; j < COUNT_WIDTH; j++) {
                if (cells[i][j].getValue() == 0) return false;
            }
        }
        return true;
    }

    public Cell[][] getCells() {
        return cells;
    }
}