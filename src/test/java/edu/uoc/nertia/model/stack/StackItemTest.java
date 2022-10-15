package edu.uoc.nertia.model.stack;

import edu.uoc.nertia.model.exceptions.PositionException;
import edu.uoc.nertia.model.utils.Position;
import edu.uoc.nertia.model.cells.Element;
import org.junit.jupiter.api.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class StackItemTest {

    private StackItem si;
    private final Class<StackItem> ownClass = StackItem.class;

    @BeforeAll
    void setUp(){
        try{
            si = new StackItem(new Position(3,5),Element.PLAYER,
                    List.of(new Position[]{new Position(1, 5)}),
                    List.of(new Position[]{new Position(2, 5)})
            );
        }catch(PositionException e){
            fail("There was a problem with StackItemTest#setUp");
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Class definition")
    void checkClassSanity() {
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertTrue(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.nertia.model.stack",ownClass.getPackageName());
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
        //Max 7 public methods (includes equals, toString, hasCode)
        assertEquals(7,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());

        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Max 0 private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPrivate(p.getModifiers())).count());

        try {
            //Max 1 constructor
            assertEquals(1,ownClass.getDeclaredConstructors().length);

            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(Position.class, Element.class,
                    List.class,List.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of StackItem's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - originPosition")
    void originPosition() {
        try{
            assertEquals(new Position(3,5),si.originPosition());
        }catch(PositionException e){
            fail("There was a problem with StackItem#originPosition");
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - originElement")
    void originElement() {
        assertEquals(Element.PLAYER,si.originElement());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - collectedLives")
    void collectedLives() {
        assertEquals(1, si.collectedLives().size());
        try{
            assertEquals(new Position(1, 5), si.collectedLives().get(0));
        }catch(PositionException e){
            fail("There was a problem with StackItem#collectedLives");
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - collectedGems")
    void collectedGems() {
        assertEquals(1, si.collectedGems().size());
        try{
            assertEquals(new Position(2, 5), si.collectedGems().get(0));
        }catch(PositionException e){
        fail("There was a problem with StackItem#collectedGems");
        }
    }
}
