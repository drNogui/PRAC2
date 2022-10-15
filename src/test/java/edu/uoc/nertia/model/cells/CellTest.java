package edu.uoc.nertia.model.cells;

import edu.uoc.nertia.model.exceptions.PositionException;
import edu.uoc.nertia.model.utils.Position;
import org.junit.jupiter.api.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class CellTest {

    private Cell c;
    private final Class<Cell> ownClass = Cell.class;

    @BeforeAll
    void setUp(){
        try{
            c = new Cell(new Position(5,4),Element.EXTRA_LIFE);
        }catch(PositionException e){
            fail("There was a problem with CellTest#setUp");
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
        assertEquals("edu.uoc.nertia.model.cells",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> Modifier.isPrivate(p.getModifiers())));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods definition")
    void checkMethodsSanity() {
        //Min 7 methods
        assertTrue(ownClass.getDeclaredMethods().length>=5);
        //Max 4 public methods
        assertEquals(4, Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());

        try{
            //Number of constructors
            assertEquals(1,ownClass.getDeclaredConstructors().length);
            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(Position.class,Element.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Cell's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getPosition")
    void getPosition() {
        assertEquals(5,c.getPosition().getRow());
        assertEquals(4,c.getPosition().getColumn());
        try{
            assertEquals(new Position(5,4),c.getPosition());
        }catch(PositionException e){
            fail("There was a problem with Cell#getPosition");
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - setElement")
    void setElement() {
        c.setElement(Element.PLAYER);
        assertEquals(Element.PLAYER,c.getElement());
        c.setElement(Element.EXTRA_LIFE);
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getElement")
    void getElement() {
        assertEquals(Element.EXTRA_LIFE,c.getElement());
        assertNotEquals(Element.STOP,c.getElement());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - toString")
    void testToString() {
        assertEquals("L",c.toString());
    }
}
