package edu.uoc.nertia.controller;

import edu.uoc.nertia.model.utils.Direction;
import edu.uoc.nertia.model.utils.MoveResult;
import edu.uoc.nertia.model.cells.Element;
import edu.uoc.nertia.model.exceptions.LevelException;
import edu.uoc.nertia.model.levels.LevelDifficulty;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.support.ModifierSupport;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class GameTest {

    private Game game;
    private final Class<Game> ownClass = Game.class;

    @BeforeEach
    void setUp() {
        try {
            game = new Game("levels/tests/");
        } catch (NullPointerException | IOException e) {
            fail("setUp failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Class definition")
    void checkClassSanity() {
        //Class declaration
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));
        assertEquals("edu.uoc.nertia.controller",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> Modifier.isPrivate(p.getModifiers())));

        try {
            int modifiers;
            Field f = ownClass.getDeclaredField("maxLevels");
            modifiers = f.getModifiers();
            assertEquals("int", f.getType().getSimpleName());
            f.setAccessible(true);
            assertFalse(Modifier.isStatic(modifiers));
            assertTrue(Modifier.isFinal(modifiers));
        } catch (NoSuchFieldException e) {
            fail("There is some problem with the definition of Game's fields. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods definition")
    void checkMethodsSanity() {
        //Min 17 methods
        assertTrue(ownClass.getDeclaredMethods().length>=17);
        //Max 18 public methods
        assertEquals(18,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Min 4 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPrivate).count()>=4);

        try {
            //These methods must be private
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("getFileFolder").getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setFileFolder", String.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("loadLevel").getModifiers()));

            //Max 1 constructor
            assertEquals(1,ownClass.getDeclaredConstructors().length);

            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(String.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Game's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - getBoardSize")
    void getBoardSize() {
        try{
            game.nextLevel();
            assertEquals(3,game.getBoardSize());
        }catch(LevelException e){
            fail("getBoardSize failed");
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - getCell")
    void getCell() {
        try {
            game.nextLevel();
            assertEquals(Element.WALL, game.getCell(0,0).getElement());
            assertEquals(Element.EMPTY, game.getCell(0,1).getElement());
            assertEquals(Element.WALL, game.getCell(0,2).getElement());
            assertEquals(Element.PLAYER, game.getCell(1,0).getElement());
            assertEquals(Element.STOP, game.getCell(1,1).getElement());
            assertEquals(Element.EMPTY, game.getCell(1,2).getElement());
            assertEquals(Element.MINE, game.getCell(2,0).getElement());
            assertEquals(Element.EMPTY, game.getCell(2,1).getElement());
            assertEquals(Element.GEM, game.getCell(2,2).getElement());

            LevelException ex = assertThrows(LevelException.class, () -> game.getCell(3,4));
            assertEquals(LevelException.INCORRECT_CELL_POSITION, ex.getMessage());

            ex = assertThrows(LevelException.class, () -> game.getCell(4,3));
            assertEquals(LevelException.INCORRECT_CELL_POSITION, ex.getMessage());

            LevelException ex2 = assertThrows(LevelException.class, () -> game.getCell(-1,0));
            assertEquals(LevelException.INCORRECT_CELL_POSITION, ex2.getMessage());
        } catch (LevelException e) {
            fail("getCell failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - getDifficulty")
    void getDifficulty() {
        try{
            game.nextLevel();
            assertEquals(LevelDifficulty.EASY,game.getDifficulty());
        }catch(LevelException e){
            fail("getDifficulty failed");
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - isFinished")
    void isFinished() {
        try{
            game.nextLevel();
            assertFalse(game.isFinished());
            game.nextLevel();
            assertTrue(game.isFinished());
        }catch(LevelException e){
            fail("isFinished failed");
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - getCurrentLevel")
    void getCurrentLevel() {
        try{
            game.nextLevel();
            assertEquals(1,game.getCurrentLevel());
        }catch(LevelException e){
            fail("getCurrentLevel failed");
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - nextLevel + getScore")
    void nextLevel() {
        try{
            assertTrue(game.nextLevel());
            assertEquals(0,game.getScore());
            assertTrue(game.nextLevel());
            assertEquals(9,game.getScore());
            assertFalse(game.nextLevel());
            assertEquals(25,game.getScore());
        }catch(LevelException e){
            fail("nextLevel failed");
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - isLevelCompleted")
    void isLevelCompleted() {
        try{
            assertTrue(game.nextLevel());
            assertFalse(game.isLevelCompleted());
        }catch(LevelException e){
            fail("isLevelCompleted failed");
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - hasLost")
    void hasLost() {
        try{
            game.nextLevel();
            assertFalse(game.hasLost());
        }catch(LevelException e){
            fail("hasLost failed");
        }
    }

    @Test
    @Tag("special")
    @DisplayName("Special - movePlayer + getScore")
    void movePlayer() {
        try{
            //Level 1
            game.nextLevel();
            assertEquals(0,game.getScore());
            assertEquals(MoveResult.KO,game.movePlayer(Direction.UP));
            assertEquals(0,game.getScore());
            assertEquals(MoveResult.DIE,game.movePlayer(Direction.DOWN));
            assertEquals(0,game.getScore());
            assertEquals(MoveResult.OK,game.movePlayer(Direction.RIGHT));
            assertEquals(0,game.getScore());
            assertEquals(Element.PLAYER_STOP,game.getCell(1,1).getElement());
            assertEquals(MoveResult.OK,game.movePlayer(Direction.UP));
            assertEquals(0,game.getScore());
            assertEquals(Element.PLAYER,game.getCell(0,1).getElement());
            assertEquals(MoveResult.OK,game.movePlayer(Direction.DOWN));
            assertEquals(0,game.getScore());
            assertEquals(Element.PLAYER_STOP,game.getCell(1,1).getElement());
            assertEquals(MoveResult.OK,game.movePlayer(Direction.RIGHT));
            assertEquals(0,game.getScore());
            assertEquals(Element.PLAYER,game.getCell(1,2).getElement());
            assertEquals(MoveResult.KO,game.movePlayer(Direction.UP));
            assertEquals(0,game.getScore());
            assertEquals(MoveResult.OK,game.movePlayer(Direction.DOWN));
            assertEquals(0,game.getScore());
            assertEquals(Element.PLAYER,game.getCell(2,2).getElement());
            assertEquals(0,game.getScore());
            assertEquals(6,game.getNumMoves());
            assertTrue(game.isLevelCompleted());

            //Level 2
            game.nextLevel();
            assertEquals(13,game.getScore());
            assertEquals(MoveResult.KO,game.movePlayer(Direction.UP));
            assertEquals(13,game.getScore());
            assertEquals(3,Character.getNumericValue(game.toString().split(":")[1].trim().charAt(0)));
            assertEquals(MoveResult.DIE,game.movePlayer(Direction.DOWN));
            assertEquals(1,game.getNumMoves());
            assertEquals(13,game.getScore());
            //Num lifes = 2
            assertEquals(2,Character.getNumericValue(game.toString().split(":")[1].trim().charAt(0)));
            assertEquals(MoveResult.OK,game.movePlayer(Direction.RIGHT));
            //Num gems = 0
            assertEquals(0,Character.getNumericValue(game.toString().split(":")[3].trim().charAt(0)));
            assertEquals(MoveResult.DIE,game.movePlayer(Direction.RIGHT));
            assertEquals(13,game.getScore());
            //Num lifes = 1
            assertEquals(1,Character.getNumericValue(game.toString().split(":")[1].trim().charAt(0)));
            //Num gems = 0
            assertEquals(0,Character.getNumericValue(game.toString().split(":")[3].trim().charAt(0)));
            assertEquals(MoveResult.OK,game.movePlayer(Direction.DOWN));
            assertEquals(13,game.getScore());
            assertEquals(MoveResult.OK,game.movePlayer(Direction.RIGHT));
            assertEquals(13,game.getScore());
            //Num gems = 1
            assertEquals(1,Character.getNumericValue(game.toString().split(":")[3].trim().charAt(0)));
            assertEquals(MoveResult.KO,game.movePlayer(Direction.RIGHT));
            assertEquals(5,game.getNumMoves());
            assertEquals(MoveResult.OK,game.movePlayer(Direction.UP));
            assertEquals(13,game.getScore());
            //Num gems = 2
            assertEquals(2,Character.getNumericValue(game.toString().split(":")[3].trim().charAt(0)));
            assertEquals(6,game.getNumMoves());
            assertEquals(13,game.getScore());
            assertFalse(game.hasLost());
            assertTrue(game.isLevelCompleted());
            assertTrue(game.isFinished());
            game.nextLevel();
            assertEquals(43,game.getScore());
        }catch(LevelException e){
            fail("movePlayer failed");
        }
    }

    @Test
    @Tag("controller")
    @DisplayName("Controller - testToString")
    void testToString() {
        try{
            game.nextLevel();
            assertEquals(game.toString(),
                    "#-#"+System.lineSeparator()+
                            "@S-"+System.lineSeparator()+
                            "X-*"+System.lineSeparator()+
                            "#Lives: 2 | #Moves: 0 | #Gems: 0 | Level Score: 9 pts | Game Score: 0 pts"+System.lineSeparator()+
                            "Enter Your Move (UP/DOWN/LEFT/RIGHT/UNDO/QUIT): ");

        }catch(LevelException e){
            fail("testToString failed");
        }
    }
}
