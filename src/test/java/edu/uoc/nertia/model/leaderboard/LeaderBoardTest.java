package edu.uoc.nertia.model.leaderboard;

import edu.uoc.nertia.model.levels.LevelDifficulty;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.support.ModifierSupport;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class LeaderBoardTest {

    private LeaderBoard leaderBoard;
    private final Class<LeaderBoard> ownClass = LeaderBoard.class;

    @BeforeAll
    void setUp(){
        File file = new File("leaderboard.ser");
        file.delete();
        leaderBoard = new LeaderBoard(-1);
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - Class definition")
    void checkClassSanity() {
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));
        assertEquals("edu.uoc.nertia.model.leaderboard",ownClass.getPackageName());
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> Modifier.isPrivate(p.getModifiers())));

        try {
            int modifiers;
            Field f = ownClass.getDeclaredField("maxScores");
            modifiers = f.getModifiers();
            assertEquals("int", f.getType().getSimpleName());
            assertFalse(Modifier.isStatic(modifiers));
            assertTrue(Modifier.isFinal(modifiers));
        } catch (NoSuchFieldException e) {
            fail("There is some problem with the definition of Level's fields. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - Methods definition")
    void checkMethodsSanity() {
        //Max 4 public methods (includes toString)
        assertEquals(4,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Min 1 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPrivate).count()>=1);

        try {
            //These methods must be private
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("getScores").getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of LeaderBoard's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }

    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - Constructor + getMaxScores")
    void testLeaderBoard(){
        LeaderBoard lb1 = new LeaderBoard(10);
        assertEquals(10,lb1.getMaxScores());
        lb1 = new LeaderBoard(6);
        assertEquals(6,lb1.getMaxScores());
        lb1 = new LeaderBoard(0);
        assertEquals(5,lb1.getMaxScores());
        lb1 = new LeaderBoard(-10);
        assertEquals(5,lb1.getMaxScores());
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - isInTheTop")
    void isInTheTop() {
        assertTrue(leaderBoard.isInTheTop(100));
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - add + toString")
    void add() {
        leaderBoard.add("David",100);
        assertEquals("1) DAVID : 100 pts"+System.lineSeparator(),
                leaderBoard.toString());

        leaderBoard.add("Elena",150);
        assertEquals("1) ELENA : 150 pts"+System.lineSeparator()+
                        "2) DAVID : 100 pts"+System.lineSeparator(),
                leaderBoard.toString());

        leaderBoard.add("marina",125);
        assertEquals("1) ELENA : 150 pts"+System.lineSeparator()+
                        "2) MARINA : 125 pts"+System.lineSeparator()+
                        "3) DAVID : 100 pts"+System.lineSeparator(),
                leaderBoard.toString());

        leaderBoard.add("PAU",95);
        assertEquals("1) ELENA : 150 pts"+System.lineSeparator()+
                        "2) MARINA : 125 pts"+System.lineSeparator()+
                        "3) DAVID : 100 pts"+System.lineSeparator()+
                        "4) PAU : 95 pts"+System.lineSeparator(),
                leaderBoard.toString());

        leaderBoard.add("Teresa",90);
        assertEquals("1) ELENA : 150 pts"+System.lineSeparator()+
                        "2) MARINA : 125 pts"+System.lineSeparator()+
                        "3) DAVID : 100 pts"+System.lineSeparator()+
                        "4) PAU : 95 pts"+System.lineSeparator()+
                        "5) TERESA : 90 pts"+System.lineSeparator(),
                leaderBoard.toString());

        leaderBoard.add("manuel",115);
        assertEquals("1) ELENA : 150 pts"+System.lineSeparator()+
                        "2) MARINA : 125 pts"+System.lineSeparator()+
                        "3) MANUEL : 115 pts"+System.lineSeparator()+
                        "4) DAVID : 100 pts"+System.lineSeparator()+
                        "5) PAU : 95 pts"+System.lineSeparator(),
                leaderBoard.toString());


        leaderBoard.add("Teresa",90);
        assertEquals("1) ELENA : 150 pts"+System.lineSeparator()+
                        "2) MARINA : 125 pts"+System.lineSeparator()+
                        "3) MANUEL : 115 pts"+System.lineSeparator()+
                        "4) DAVID : 100 pts"+System.lineSeparator()+
                        "5) PAU : 95 pts"+System.lineSeparator(),
                leaderBoard.toString());

        leaderBoard.add("Teresa",150);
        assertEquals("1) ELENA : 150 pts"+System.lineSeparator()+
                        "2) TERESA : 150 pts"+System.lineSeparator()+
                        "3) MARINA : 125 pts"+System.lineSeparator()+
                        "4) MANUEL : 115 pts"+System.lineSeparator()+
                        "5) DAVID : 100 pts"+System.lineSeparator(),
                leaderBoard.toString());
    }
}
