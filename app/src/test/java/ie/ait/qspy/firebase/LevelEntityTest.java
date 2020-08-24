package ie.ait.qspy.firebase;

import org.junit.Before;
import org.junit.Test;

import ie.ait.qspy.firebase.entities.LevelEntity;

import static org.junit.Assert.*;

public class LevelEntityTest {

    private LevelEntity level;

    @Before
    public void setUp() throws Exception {
        level = new LevelEntity("Level 1", 0L, 40L, "level1");
    }

    @Test
    public void getDescription() {
        assertEquals("Level 1", level.getDescription());
    }

    @Test
    public void getRangeStart() {
        assertEquals(0L, level.getRangeStart());
    }

    @Test
    public void getRangeEnd() {
        assertEquals(40L, level.getRangeEnd());
    }

    @Test
    public void getAvatarImg() {
        assertEquals("level1", level.getAvatarImg());
    }

}