package edu.uoc.nertia.model.cells;

import edu.uoc.nertia.model.exceptions.PositionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

import static java.lang.reflect.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class CellFactoryTest {

    private final Class<CellFactory> ownClass = CellFactory.class;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> isPrivate(p.getModifiers())));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods definition")
    void checkMethodsSanity() {
        assertEquals(1, ownClass.getDeclaredMethods().length);
        try {
            assertTrue(isPublic(ownClass.getDeclaredMethod("getCellInstance", int.class, int.class, char.class).getModifiers()));
            assertTrue(isStatic(ownClass.getDeclaredMethod("getCellInstance", int.class, int.class, char.class).getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Cell's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - getCellInstance definition")
    void getCellInstance() {

        Cell a;
        try{
            assertTrue((a =CellFactory.getCellInstance(0, 0, 'L')) instanceof Cell
            && a.toString().equals("L") && a.getElement().equals(Element.EXTRA_LIFE));

            assertTrue((a =CellFactory.getCellInstance(0, 0, '*')) instanceof Cell
            && a.toString().equals("*") && a.getElement().equals(Element.GEM));

            assertTrue((a =CellFactory.getCellInstance(0, 0, '-')) instanceof Cell
            && a.toString().equals("-") && a.getElement().equals(Element.EMPTY));

            assertTrue((a =CellFactory.getCellInstance(0, 0, 'X')) instanceof Cell
            && a.toString().equals("X") && a.getElement().equals(Element.MINE));

            assertTrue((a =CellFactory.getCellInstance(0, 0, '@')) instanceof Cell
            && a.toString().equals("@") && a.getElement().equals(Element.PLAYER));

            assertTrue((a =CellFactory.getCellInstance(0, 0, '$')) instanceof Cell
            && a.toString().equals("$") && a.getElement().equals(Element.PLAYER_STOP));

            assertTrue((a =CellFactory.getCellInstance(0, 0, '#')) instanceof Cell
            && a.toString().equals("#") && a.getElement().equals(Element.WALL));

        }catch(PositionException e){
            fail("There was a problem with CellFactory");
        }

    }
}
