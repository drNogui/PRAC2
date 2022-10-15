package edu.uoc.nertia.model.leaderboard;

import java.io.Serial;
import java.io.Serializable;

public record Score(String name, int points) implements Serializable, Comparable<Score>{
    @Serial
    private static final long serialVersionUID = 13L;
    @Override
    public int compareTo(Score FinalScore) {
        return (FinalScore.points() - points);
    }
    @Override
    public String toString() {
        return name.toUpperCase() + " : " + points + " pts";
    }
}
