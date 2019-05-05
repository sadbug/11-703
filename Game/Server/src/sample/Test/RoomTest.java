package sample.Test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sample.Server.Room;

public class RoomTest extends Assert {

    private Room room;
    @Before
    public void create() {
        room = new Room(null, "", null);
    }

    @Test
    public void newRoomShouldNotBeFull() {
        Assert.assertEquals(room.isFull(), false);
    }
}