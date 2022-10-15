package edu.uoc.nertia.model.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.support.ModifierSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class DirectionTest {

    private final Class<Direction> ownClass = Direction.class;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Enum definition")
    void checkEnumSanity() {
        assertTrue(ownClass.isEnum());
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertEquals("edu.uoc.nertia.model.utils",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //Max 4 public fields
        assertEquals(4, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private fields

        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());

        //Max 3 private fields (there is "values" is implicit). There is an implicit private static $VALUES field
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPrivate).count() == 3);
        //Max 4 Enum values
        assertEquals(4, Arrays.stream(ownClass.getDeclaredFields()).filter(Field::isEnumConstant).count());
        //Max 5 static values. There is an implicit private static $VALUES field
        assertEquals(5, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isStatic).count());

    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods and Constructors definition")
    void checkMethodsSanity() {
        //Min 4 methods
        assertTrue(ownClass.getDeclaredMethods().length>=4);
        //Max 2 public methods + 2 implicit public methods (values() and valueOf()).
        assertEquals(4,Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());

        //Max 1 constructor
        assertEquals(1,ownClass.getDeclaredConstructors().length);

        int constructorModifiers = ownClass.getDeclaredConstructors()[0].getModifiers();
        assertTrue(Modifier.isPrivate(constructorModifiers));
        assertFalse(Modifier.isStatic(constructorModifiers));
        assertFalse(Modifier.isFinal(constructorModifiers));
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getRowOffset")
    void getRowOffset() {
        assertEquals(-1, Direction.UP.getRowOffset());
        assertEquals(1,Direction.DOWN.getRowOffset());
        assertEquals(0,Direction.LEFT.getRowOffset());
        assertEquals(0,Direction.RIGHT.getRowOffset());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getColumnOffset")
    void getColumnOffset() {
        assertEquals(0,Direction.UP.getColumnOffset());
        assertEquals(0,Direction.DOWN.getColumnOffset());
        assertEquals(-1,Direction.LEFT.getColumnOffset());
        assertEquals(1,Direction.RIGHT.getColumnOffset());
    }
}
