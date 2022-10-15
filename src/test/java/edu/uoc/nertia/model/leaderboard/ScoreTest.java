package edu.uoc.nertia.model.leaderboard;

import org.junit.jupiter.api.*;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class ScoreTest {

    private Score score;
    private final Class<Score> ownClass = Score.class;

    @BeforeAll
    void setUp(){
        score = new Score("David", 100);
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - Class definition")
    void checkClassSanity() {
        int modifiers = ownClass.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isStatic(modifiers));
        assertTrue(Modifier.isFinal(modifiers));

        assertEquals("edu.uoc.nertia.model.leaderboard",ownClass.getPackageName());
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
            int modifiers = ownClass.getDeclaredConstructor(String.class, int.class).getModifiers();
            assertTrue(Modifier.isPublic(modifiers));

        } catch (NoSuchMethodException e) {
            fail("There is some problem with the definition of Score's methods/constructors. Please read the PRAC 2 - Statement:\n");
            e.printStackTrace();
        }
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - name")
    void name() {
        assertEquals("David",score.name());
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - points")
    void points() {
        assertEquals(100,score.points());
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - toString")
    void testToString(){
        assertEquals("DAVID : 100 pts", score.toString());
    }

    @Test
    @Tag("advanced")
    @DisplayName("Advanced - toString")
    void testCompareTo(){
       Score score2 = new Score("David", 200);

       assertTrue(score.compareTo(score2)>0);
       assertTrue(score2.compareTo(score)<0);
       assertTrue(score2.compareTo(score2)==0);
    }
}
