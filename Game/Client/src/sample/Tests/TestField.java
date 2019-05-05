package sample.Tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Control.Logic;
import sample.Models.Field;


public class TestField extends Assert{

    private Field field;
    @Before
    public void full() {
//        FieldView fieldView = mock(FieldView.class);
        field = new Field();
        field.createField();
    }
    @Test
    public void newFieldShouldNotBeFall() {
        Assert.assertEquals(field.checkAll(), false);
    }

    @Test
    public void newFieldShouldNotBeWin() {
        field.getCells()[0][0].setValue(1);
        Assert.assertEquals(field.checkWin(0,0), false);
    }

    @Test
    public void notOpenWhenBusy() {
//        Logic logic = mock(Logic.class);
//        field.setLogic(logic);
        field.getCells()[1][1].setValue(2);

        Assert.assertEquals(field.open(1, 1), false);
    }

    @Test
    public void winDiagonal() {
        field.getCells()[0][0].setValue(1);
        field.getCells()[1][1].setValue(1);
        field.getCells()[2][2].setValue(1);
        Assert.assertEquals(field.checkWin(0,0), true);
    }
}