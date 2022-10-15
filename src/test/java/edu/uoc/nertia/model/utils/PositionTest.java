package edu.uoc.nertia.model.utils;

import edu.uoc.nertia.model.exceptions.PositionException;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class PositionTest {

    private Position p1;
    private Position p2;
    private final Class<Position> ownClass = Position.class;

    @BeforeAll
    void setUp(){
       try{
           p1 = new Position(1,2);
            p2 = new Position(10,5);
       }catch(PositionException e){
            fail("There was a problem with Positiontest#setUp");
       }
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Class definition")
    void checkClassSanity() {
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.nertia.model.utils",ownClass.getPackageName());
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
        //Min 8 methods
        assertTrue(ownClass.getDeclaredMethods().length>=8);
        //Max 6 public methods
        assertEquals(6,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Min 2 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPrivate).count()>=2);

        try {
            //These methods must be private
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setRow", int.class).getModifiers()));
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("setColumn", int.class).getModifiers()));

            //Max 1 constructor
            assertEquals(1,ownClass.getDeclaredConstructors().length);

            //Constructor
            int modifiers = ownClass.getDeclaredConstructor(int.class,int.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Coordinate's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getRow")
    void getRow() {
        assertEquals(1,p1.getRow());
        assertEquals(10,p2.getRow());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getColumn")
    void getColumn() {
        assertEquals(2,p1.getColumn());
        assertEquals(5,p2.getColumn());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - offsetBy with 2 parameters")
    void offsetBy() {
        Position o1 = p1.offsetBy(4,5);
        assertTrue(o1.getRow()==5 & o1.getColumn()==7);
        assertNull(p1.offsetBy(-3,-7));
        o1 = p1.offsetBy(1,-2);
        assertTrue(o1.getRow()==2 & o1.getColumn()==0);
        o1 = p1.offsetBy(0,2);
        assertTrue(o1.getRow()==1 & o1.getColumn()==4);
        assertNull(p1.offsetBy(-2,0));
        assertNull(p1.offsetBy(0,-3));

        Position o2 = p2.offsetBy(8,10);
        assertTrue(o2.getRow()==18 & o2.getColumn()==15);
        o2 = p2.offsetBy(-8,-5);
        assertTrue(o2.getRow()==2 & o2.getColumn()==0);
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - offsetBy with 3 parameters")
    void testOffsetBy() {
        Position o1 = p1.offsetBy(4,5,10);
        assertTrue(o1.getRow()==5 & o1.getColumn()==7);
        assertNull(p1.offsetBy(-3,-7,2));
        o1 = p1.offsetBy(1,0,3);
        assertTrue(o1.getRow()==2 & o1.getColumn()==2);
        assertNull(p1.offsetBy(2,1,3));
        assertNull(p1.offsetBy(1,0,2));
        o1 = p1.offsetBy(0,0,3);
        assertTrue(o1.getRow()==1 & o1.getColumn()==2);
        assertNull(p1.offsetBy(5,0,3));
        assertNull(p1.offsetBy(0,8,10));
    }
}
