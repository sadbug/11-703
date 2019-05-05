package sample.Tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Control.Logic;



public class TestLogic extends Assert {

    private Logic logic;

    @Before
    public void create() {
        logic = new Logic();
    }

    @Test
    public void oneHaveNotRooms() {
        Assert.assertEquals(logic.updateRooms().size(), 0);
    }

    @Test
    public void createRoom() {
        Assert.assertEquals(logic.createRoom(), true);
    }
}