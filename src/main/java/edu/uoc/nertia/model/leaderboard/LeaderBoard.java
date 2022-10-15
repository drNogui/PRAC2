package edu.uoc.nertia.model.leaderboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author David García Solórzano
 * @version 1.0
 */
public class LeaderBoard{

    /**
     * Maximum scores that will be stored.
     */
    private final int maxScores;

    /**
     * List with the top highest scores.
     */
    private List<Score> scores;

    /**
     * Name of the file where the data will be stored.
     */
    private static final String FILE_LEADERBOARD = "leaderboard.ser";



    private List<Score> getScores(){
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(FILE_LEADERBOARD))){
            return (List<Score>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(this.getMaxScores());
        }
    }

    public void add(String name, int points){
        if(isInTheTop(points))
            scores.add(new Score(name, points));
        if(scores.size() > maxScores)
            scores.remove(maxScores-1);

        Collections.sort(scores);

        //DONE: write to the file. DON'T MODIFY
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(FILE_LEADERBOARD,false))){
            output.writeObject(scores);
        } catch (NotSerializableException e) {
            System.out.println("||1|"+e.getMessage());
        } catch (InvalidClassException  e) {
            System.out.println("||2|"+e.getMessage());
        } catch (IOException  e) {
            System.out.println("||3|"+e.getMessage());
        }
    }

    /**
     * Returns if the given points deserve to be in the top list.
     * @param points Score to compare
     * @return {@code true} if the given points deserve to be in the leaderboard. Otherwise, {@code false}.
     */
    public boolean isInTheTop(int points){
        if(getScores().size() < 5 || getScores().get(4).points() < points)
            return true;
        return false;
    }

    /** Constructor for LeaderBoard
     * @param maxScores Number of scores that must be stored. If {@code maxsScores}
     *                  is negative or zero, then the default value is 5.
     */
    public LeaderBoard(int maxScores) {
        if(maxScores <= 0)
            this.maxScores = 5;
        else
            this.maxScores = maxScores;
        scores = new ArrayList<Score>();
    }

    /**
     * Returns the value of the attribute {@code MAX_SCORES}.
     * @return The value of {@code MAX_SCORES}.
     */
    public int getMaxScores(){
        return maxScores;
    }

    /** Returns the top five high scores, in order from best to worst
     * @return String with the top five high scores.
     */
    @Override
    public String toString() {
        String str = "";
        for (int i=1; i<=scores.size(); i++) {
            str += i + ") " + scores.get(i-1).name().toUpperCase() + " : " + scores.get(i-1).points() + " pts";
            str += System.lineSeparator();
        }
        return str;
    }
}
