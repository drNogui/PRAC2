package edu.uoc.nertia.model.stack;

import edu.uoc.nertia.model.exceptions.PositionException;
import edu.uoc.nertia.model.utils.Position;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class UndoStackTest {
    private UndoStack stack;

    private final Class<UndoStack> ownClass = UndoStack.class;

    @BeforeAll
    void setUp(){
        stack = new UndoStack();
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - Class definition")
    void checkClassSanity() {
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.nertia.model.stack",ownClass.getPackageName());
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - Fields definition")
    void checkFieldsSanity() {
        //All fields must be private
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> Modifier.isPrivate(p.getModifiers())));
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - Methods definition")
    void checkMethodsSanity() {
        //Min 3 methods
        assertTrue(ownClass.getDeclaredMethods().length>=3);

        //Max 3 public methods (include pop():Object)
        assertEquals(3,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());
        //Min 1 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPrivate).count()>=1);

        try {
            //This method must be private
            assertTrue(Modifier.isPrivate(ownClass.getDeclaredMethod("incrementNumPops").getModifiers()));

            //Max 1 constructor
            assertEquals(1,ownClass.getDeclaredConstructors().length);

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Coordinate's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - pop")
    void pop() {
        try {
            stack.push(new StackItem(new Position(1,2),null,null,null));
            stack.push(new StackItem(new Position(2,3),null,null,null));
            stack.push(new StackItem(new Position(3,4),null,null,null));

            assertEquals(new Position(3,4),(stack.pop()).originPosition());
            assertEquals(new Position(2,3),(stack.pop()).originPosition());
            assertEquals(new Position(1,2),(stack.pop()).originPosition());

            stack.clear();
        }catch(PositionException e){
            fail("There was a problem with UndoStack#pop");
        }
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - getNumPops")
    void getNumPops() {
       try{
           stack.push(new StackItem(new Position(1,2),null,null,null));
            stack.push(new StackItem(new Position(2,3),null,null,null));
            stack.push(new StackItem(new Position(3,4),null,null,null));
        }catch(PositionException e){
            fail("There was a problem with UndoStack#getNumPops");
        }
        assertEquals(0,stack.getNum());
        stack.pop();
        assertEquals(1,stack.getNum());
        stack.pop();
        assertEquals(2,stack.getNum());
        stack.pop();
        assertEquals(3,stack.getNum());
    }
}
