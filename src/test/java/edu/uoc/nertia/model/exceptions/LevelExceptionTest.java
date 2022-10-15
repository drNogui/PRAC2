package edu.uoc.nertia.model.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class LevelExceptionTest {

    private final Class<LevelException> ownClass = LevelException.class;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Class definition")
    void checkClassSanity() {
        //Class declaration
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertFalse(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.nertia.model.exceptions",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //All fields must meet these requirements
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> {
            return p.getType().getSimpleName().equals("String")
                    && Modifier.isPublic(p.getModifiers())
                    && Modifier.isStatic(p.getModifiers())
                    && Modifier.isFinal(p.getModifiers());
        }));

        //Max 7 fields
        assertEquals(7, ownClass.getDeclaredFields().length);
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods and Constructor definition")
    void checkMethodsSanity() {
        //Max 0 public methods
        assertEquals(0, Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPublic(p.getModifiers())).count());
        //Max 0 protected methods
        assertEquals(0, Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0, Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isNative(p.getModifiers())).count());

        //Max 1 constructor
        assertEquals(1, ownClass.getDeclaredConstructors().length);

        try {
            //Constructor
            int constructorModifiers = ownClass.getDeclaredConstructor(String.class).getModifiers();
            assertTrue(Modifier.isPublic(constructorModifiers));
            assertFalse(Modifier.isStatic(constructorModifiers));
            assertFalse(Modifier.isFinal(constructorModifiers));

        } catch (NoSuchMethodException e) {
            fail("LevelException's constructor is defined wrongly");
        }
    }
}
