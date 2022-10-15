package edu.uoc.nertia.model.levels;


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
class LevelDifficultyTest {

    private final Class<LevelDifficulty> ownClass = LevelDifficulty.class;

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Enum definition")
    void checkEnumSanity() {
        assertTrue(ownClass.isEnum());
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertTrue(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.nertia.model.levels",ownClass.getPackageName());
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Fields definition")
    void checkFieldsSanity() {
        //Max 4 fields. There is an implicit private static $VALUES field
        assertEquals(4, ownClass.getDeclaredFields().length);
        //Max 5 public fields
        assertEquals(3, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isPublic).count());
        //Max 0 protected fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields()).filter(p -> Modifier.isProtected(p.getModifiers())).count());
        //Max 0 package-private fields
        assertEquals(0, Arrays.stream(ownClass.getDeclaredFields())
                .filter(p -> !Modifier.isPublic(p.getModifiers())
                        && !Modifier.isProtected(p.getModifiers())
                        && !Modifier.isPrivate(p.getModifiers())).count());
        //Max 3 Enum values
        assertEquals(3, Arrays.stream(ownClass.getDeclaredFields()).filter(Field::isEnumConstant).count());
        //Max 4 static fields. There is an implicit private static $VALUES field
        assertEquals(4, Arrays.stream(ownClass.getDeclaredFields()).filter(ModifierSupport::isStatic).count());

        //All fields must meet these requirements
        assertTrue(Arrays.stream(ownClass.getDeclaredFields()).allMatch(p -> {
            return Modifier.isStatic(p.getModifiers())
                    && Modifier.isFinal(p.getModifiers());
        }));
    }

    @Test
    @Tag("sanity")
    @DisplayName("Sanity - Methods and Constructors definition")
    void checkMethodsSanity() {
        //Max 3 methods. There are implicit methods.
        assertEquals(3, ownClass.getDeclaredMethods().length);
    }
}
