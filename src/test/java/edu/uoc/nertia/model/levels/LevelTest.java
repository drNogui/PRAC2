package edu.uoc.nertia.model.levels;

import edu.uoc.nertia.model.cells.Element;
import edu.uoc.nertia.model.exceptions.LevelException;
import edu.uoc.nertia.model.exceptions.PositionException;
import edu.uoc.nertia.model.stack.StackItem;
import edu.uoc.nertia.model.utils.Position;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.support.ModifierSupport;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class LevelTest {

    private Level level;
    private final Class<Level> ownClass = Level.class;

    @BeforeEach
    void setUp() {
        try {
            level = new Level("levels/tests/level1.txt");
        } catch (LevelException e) {
            fail("setUp failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Class definition")
    public void checkClassSanity() {
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.nertia.model.levels",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> Modifier.isPrivate(p.getModifiers())));

        //Min 9 private fields.
        assertTrue(ownClass.getDeclaredFields().length>=9);

        try {
            int modifiers;
            Field f = ownClass.getDeclaredField("UNLIMITED_LIVES");
            modifiers = f.getModifiers();
            assertEquals("int", f.getType().getSimpleName());
            f.setAccessible(true);
            assertEquals(-1, f.get(level));
            assertTrue(Modifier.isStatic(modifiers));
            assertTrue(Modifier.isFinal(modifiers));

            f = ownClass.getDeclaredField("size");
            modifiers = f.getModifiers();
            assertEquals("int", f.getType().getSimpleName());
            assertFalse(Modifier.isStatic(modifiers));
            assertTrue(Modifier.isFinal(modifiers));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("There is some problem with the definition of Level's fields. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods definition")
    void checkMethodsSanity() {
        //Min 25 methods
        assertTrue(ownClass.getDeclaredMethods().length>=25);
        //Max 21 public methods
        assertEquals(21,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Min 6 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPrivate).count()>=6);

        try {
            //These methods must be private
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setDifficulty", LevelDifficulty.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setNumLives", int.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("hasUnlimitedLives").getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("parse",String.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("getFirstNonEmptyLine", BufferedReader.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("getBoard").getModifiers()));

            //Max 1 constructor
            assertEquals(1,ownClass.getDeclaredConstructors().length);

            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(String.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Level's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - Constructor exceptions")
    void testConstructorException(){
        LevelException ex = assertThrows(LevelException.class, () -> new Level("levels/errors/level-error-no-lifes.txt"));
        assertEquals(LevelException.PARSING_LEVEL_FILE_ERROR, ex.getMessage());

        ex = assertThrows(LevelException.class, () -> new Level("levels/errors/level-error-no-size.txt"));
        assertEquals(LevelException.PARSING_LEVEL_FILE_ERROR, ex.getMessage());

        ex = assertThrows(LevelException.class, () -> new Level("levels/errors/level-error-no-player.txt"));
        assertEquals(LevelException.PLAYER_LEVEL_FILE_ERROR, ex.getMessage());

        ex = assertThrows(LevelException.class, () -> new Level("levels/errors/level-error-two-players.txt"));
        assertEquals(LevelException.PLAYER_LEVEL_FILE_ERROR, ex.getMessage());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getSize")
    void getSize() {
        assertEquals(3, level.getSize());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getDifficulty")
    void getDifficulty() {
        assertEquals(LevelDifficulty.EASY, level.getDifficulty());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getNumLives")
    void getNumLives() {
        assertEquals(2, level.getNumLives());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getNumGemsInit")
    void getNumGemsInit(){
        assertEquals(1, level.getNumGemsInit());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - increaseNumLives")
    void increaseNumLives() {
        try{
            level.increaseNumLives(1);
            assertEquals(3, level.getNumLives());
        }catch(LevelException e){
            fail("There was a problem with increaseNumLives");
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - decreaseNumLives")
    void decreaseNumLives() {
        level.decreaseNumLives();
        assertEquals(1, level.getNumLives());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - increaseNumGemsGot")
    void increaseNumGemsGot() {
        try{
            level.increaseNumGemsGot(2);
            assertEquals(2, level.getNumGemsGot());
            level.increaseNumGemsGot(1);
            assertEquals(3, level.getNumGemsGot());
        }catch(LevelException e){
            fail("There was a problem with increaseNumGemsGot");
        }

        LevelException ex = assertThrows(LevelException.class, () -> level.increaseNumGemsGot(-1));
        assertEquals(LevelException.INCREASE_NUM_GEMS_GOT_ERROR, ex.getMessage());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - decreaseNumGemsGot")
    void decreaseNumGemsGot(){
        assertEquals(0, level.getNumGemsGot());
        level.decreaseNumGemsGot();
        assertEquals(0, level.getNumGemsGot());
        try{
            level.increaseNumGemsGot(2);
        }catch(LevelException e){
            fail("There was a problem with decreaseNumGemsGot");
        }
        level.decreaseNumGemsGot();
        assertEquals(1, level.getNumGemsGot());
        level.decreaseNumGemsGot();
        assertEquals(0, level.getNumGemsGot());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getNumGemsGot")
    void getNumGemsGot() {
        assertEquals(0, level.getNumGemsGot());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getNumMoves + increaseNumMoves")
    void getNumMoves() {
        assertEquals(0, level.getNumMoves());
        level.increaseNumMoves();
        assertEquals(1, level.getNumMoves());
        level.increaseNumMoves();
        assertEquals(2, level.getNumMoves());
    }

   @Test
   @Tag("minimum")
   @DisplayName("Minimum - hasWon")
    void hasWon() {
        try {
            assertFalse(level.hasWon());
            level.setCell(new Position(2,2), Element.EMPTY);
            level.increaseNumGemsGot(1);
            assertTrue(level.hasWon());
        } catch (IllegalArgumentException | LevelException | PositionException e) {
            fail("hasWon failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - hasLost")
    void hasLost() {
        level.decreaseNumLives();
        assertFalse(level.hasLost());
        level.decreaseNumLives();
        assertTrue(level.hasLost());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getCell")
    void getCell() {
        try {
            assertEquals(Element.WALL, level.getCell(new Position(0,0)).getElement());
            assertEquals(Element.EMPTY, level.getCell(new Position(0,1)).getElement());
            assertEquals(Element.WALL, level.getCell(new Position(0,2)).getElement());
            assertEquals(Element.PLAYER, level.getCell(new Position(1,0)).getElement());
            assertEquals(Element.STOP, level.getCell(new Position(1,1)).getElement());
            assertEquals(Element.EMPTY, level.getCell(new Position(1,2)).getElement());
            assertEquals(Element.MINE, level.getCell(new Position(2,0)).getElement());
            assertEquals(Element.EMPTY, level.getCell(new Position(2,1)).getElement());
            assertEquals(Element.GEM, level.getCell(new Position(2,2)).getElement());

            LevelException ex = assertThrows(LevelException.class, () -> level.getCell(new Position(3,4)));
            assertEquals(LevelException.INCORRECT_CELL_POSITION, ex.getMessage());

            ex = assertThrows(LevelException.class, () -> level.getCell(new Position(4,3)));
            assertEquals(LevelException.INCORRECT_CELL_POSITION, ex.getMessage());

            PositionException ex2 = assertThrows(PositionException.class, () -> level.getCell(new Position(-1,0)));
            assertEquals(PositionException.POSITION_ROW_ERROR, ex2.getMessage());

        } catch (LevelException | PositionException e) {
            fail("getCell failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getPlayerPosition + setCell")
    void getPlayerPosition() {
        try{
            assertEquals(new Position(1,0),level.getPlayerPosition());
            level.setCell(new Position(1,1), Element.PLAYER_STOP);
            level.setCell(new Position(1,0), Element.EMPTY);
            assertEquals(new Position(1,1),level.getPlayerPosition());
        }catch (LevelException | PositionException e) {
            fail("getPlayerPosition failed");
            e.printStackTrace();
         }
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - getScore + push + undo")
    void getScore() {
        assertEquals(9,level.getScore());
        try{
            level.increaseNumGemsGot(1);
            assertEquals(19,level.getScore());
            level.increaseNumMoves();
            assertEquals(18,level.getScore());
            level.push(new StackItem(new Position(0,0),Element.PLAYER,(new ArrayList<>()),new ArrayList<>()));
            level.undo();
            assertEquals(16,level.getScore());
        }catch (LevelException | PositionException e) {
            fail("getScore failed");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - toString")
    void testToString() {
        assertEquals("#-#"+System.lineSeparator()
                +"@S-"+System.lineSeparator()
                +"X-*", level.toString().trim());
        try {
            level.setCell(new Position(1,1),Element.PLAYER_STOP);
            level.setCell(new Position(1,0),Element.EMPTY);
        } catch (LevelException | PositionException e) {
            fail("testToString failed");
            e.printStackTrace();
        }
        assertEquals("#-#"+System.lineSeparator()
                +"-$-"+System.lineSeparator()
                +"X-*", level.toString().trim());
    }
}
