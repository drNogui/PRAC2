package edu.uoc.nertia.model.cells;

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
class ElementTest {

    private final Class<Element> ownClass = Element.class;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Enum definition")
    void checkEnumSanity() {
        assertTrue(ownClass.isEnum());
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertEquals("edu.uoc.nertia.model.cells",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //Minimum number of fields. There is an implicit private static $VALUES field
        assertTrue(ownClass.getDeclaredFields().length >= 11);

        //Max 8 public fields
        assertEquals(8, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());
        //Minimum 3 private fields. There is an implicit private static $VALUES field
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPrivate).count() >= 3);
        //Max 8 Enum values
        assertEquals(8, Arrays.stream(ownClass.getDeclaredFields()).filter(Field::isEnumConstant).count());
        //Max 9 static values. There is an implicit private static $VALUES field
        assertEquals(9, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isStatic).count());

        //Min 1 char fields
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).anyMatch(p -> p.getType().getSimpleName().equals("char")));
        //Min 1 String fields
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).anyMatch(p -> p.getType().getSimpleName().equals("String")));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods and Constructors definition")
    void checkMethodsSanity() {
        //Min 4 methods
        assertTrue(ownClass.getDeclaredMethods().length>=4);
        //Max 4 public methods + 2 implicit public methods (values() and valueOf()).
        assertEquals(6,Arrays.stream(ownClass.getDeclaredMethods()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private methods
        assertEquals(0,Arrays.stream(ownClass.getDeclaredMethods())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());

        //Min 2 private methods
        assertTrue(Arrays.stream(ownClass.getDeclaredMethods()).filter(p -> Modifier.isPrivate(p.getModifiers())).count()>=2);

        //Max 1 constructor
        assertEquals(1,ownClass.getDeclaredConstructors().length);

        int constructorModifiers = ownClass.getDeclaredConstructors()[0].getModifiers();
        assertTrue(Modifier.isPrivate(constructorModifiers));
        assertFalse(Modifier.isStatic(constructorModifiers));
        assertFalse(Modifier.isFinal(constructorModifiers));
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - symbol2Element")
    void symbol2Element() {
        assertEquals(Element.EXTRA_LIFE, Element.symbol2Element('L'));
        assertEquals(Element.GEM, Element.symbol2Element('*'));
        assertEquals(Element.MINE, Element.symbol2Element('X'));
        assertEquals(Element.PLAYER, Element.symbol2Element('@'));
        assertEquals(Element.PLAYER_STOP, Element.symbol2Element('$'));
        assertEquals(Element.STOP, Element.symbol2Element('S'));
        assertEquals(Element.EMPTY, Element.symbol2Element('-'));
        assertEquals(Element.WALL, Element.symbol2Element('#'));
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getSymbol")
    void getSymbol() {
        assertEquals('L', Element.EXTRA_LIFE.getSymbol());
        assertEquals('*', Element.GEM.getSymbol());
        assertEquals('X', Element.MINE.getSymbol());
        assertEquals('@', Element.PLAYER.getSymbol());
        assertEquals('$', Element.PLAYER_STOP.getSymbol());
        assertEquals('S', Element.STOP.getSymbol());
        assertEquals('#', Element.WALL.getSymbol());
        assertEquals('-', Element.EMPTY.getSymbol());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - getImageSrc")
    void getImageSrc() {
        assertEquals("life.png", Element.EXTRA_LIFE.getImageSrc());
        assertEquals("gem.png", Element.GEM.getImageSrc());
        assertEquals("mine.png", Element.MINE.getImageSrc());
        assertEquals("player.png", Element.PLAYER.getImageSrc());
        assertEquals("player_stop.png", Element.PLAYER_STOP.getImageSrc());
        assertEquals("stop.png", Element.STOP.getImageSrc());
        assertEquals("wall.png", Element.WALL.getImageSrc());
        assertEquals("empty.png", Element.EMPTY.getImageSrc());
    }

    @Test
    @Tag("minimum")
    @DisplayName("Minimum - toString")
    void testToString() {
        assertEquals("L", Element.EXTRA_LIFE.toString());
        assertEquals("*", Element.GEM.toString());
        assertEquals("X", Element.MINE.toString());
        assertEquals("@", Element.PLAYER.toString());
        assertEquals("$", Element.PLAYER_STOP.toString());
        assertEquals("S", Element.STOP.toString());
        assertEquals("#", Element.WALL.toString());
        assertEquals("-", Element.EMPTY.toString());
    }
}
