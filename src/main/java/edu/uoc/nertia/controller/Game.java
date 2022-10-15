package edu.uoc.nertia.controller;

import edu.uoc.nertia.model.cells.Cell;
import edu.uoc.nertia.model.cells.Element;
import edu.uoc.nertia.model.exceptions.LevelException;
import edu.uoc.nertia.model.leaderboard.LeaderBoard;
import edu.uoc.nertia.model.levels.Level;
import edu.uoc.nertia.model.stack.StackItem;
import edu.uoc.nertia.model.levels.LevelDifficulty;
import edu.uoc.nertia.model.utils.Direction;
import edu.uoc.nertia.model.utils.MoveResult;
import edu.uoc.nertia.model.utils.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.*;

/**
 * Controller class of the game. It is the middleware (or bridge) between the model and view classes.
 * <br/>
 * This class is called from the view classes in order to access/modify the model data.
 *
 *  @author David García-Solórzano
 *  @version 1.0
*/
public class Game {

    /**
     * Name of the folder in which level files are
     */
    private String fileFolder;

    /**
     * Number of the current level.
     */
    private int currentLevel = 0;

    /**
     * Maximum quantity of levels that the game has.
     */
    private final int maxLevels;

    /**
     * Total score of the game, i.e. the sum of the levels' scores.
     */
    private int score;

    /**
     * Level object that contains the information of the current level.
     */
    private Level level;

    /**
     * LeaderBoard object that manages the leaderboard of the game.
     */
    private LeaderBoard leaderBoard;

    /**
     * Constructor
     *
     * @param fileFolder Folder name where the configuration/level files are.
     * @throws IOException When there is a problem while retrieving number of levels
     */
    public Game(String fileFolder) throws IOException {
        int num;

        setFileFolder(fileFolder);

        //Get the number of files that are in the fileFolder, i.e. the number of levels.
        URL url = getClass().getClassLoader().getResource(getFileFolder());

        URLConnection urlConnection = Objects.requireNonNull(url).openConnection();

        if(urlConnection instanceof JarURLConnection){
            //run in jar
            String path = null;
            try {
                path = getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            } catch (URISyntaxException e) {
                System.out.println("ERROR: Game Constructor");
                e.printStackTrace();
                System.exit(-1);
            }

            URI uri = URI.create("jar:file:"+path);

            try(FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                num = (int) Files.walk(fs.getPath(getFileFolder()))
                        .filter(Files::isRegularFile).count();
            }
        }else{
            //run in ide
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream;
            inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(getFileFolder()));

            try(InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)){
                num = (int) reader.lines().count();
            }
       }

        //We load the leaderboard
        leaderBoard = new LeaderBoard(5);

        setScore(0);

        maxLevels = num;
    }

    /**
     * Setter of the attribute {@code fileFolder}.
     *
     * @param fileFolder Folder name where the configuration/level files are.
     */
    private void setFileFolder(String fileFolder){
        this.fileFolder = fileFolder;
    }

    /**
     * Getter of the attribute {@code fileFolder}.
     *
     * @return Value of the attribute {@code fileFolder}.
     */
    private String getFileFolder(){
        return fileFolder;
    }


    /**
     * Returns the size of the board. The board is NxN.
     *
     * @return Value of the board's size.
     */
    public int getBoardSize(){
        return level.getSize();
    }

    public int getScore(){
        return score;
    }

    private void setScore(int score){
        this.score = score;
    }

    /**
     * Returns the {@link Cell} object which is in the position {@code (row,column)}.
     *
     * @param row Row in which the cell we want to retrieve is
     * @param column Column in which the cell we want to retrieve is
     * @return The cell that is in the position {@code (row,column)}.
     * @throws LevelException When either the row or the column is wrong.
     */
    public Cell getCell(int row, int column) throws LevelException{
        return level.getCell(row, column);
    }

    /**
     * Returns the difficulty of the current level.
     *
     * @return The difficulty of the current level.
     */
    public LevelDifficulty getDifficulty() {
        return level.getDifficulty();
    }

    /**
     * Returns the number of moves that have been done in the current level so far.
     *
     * @return Number of moves that the player has done so far. If level is null, then returns 0.
     */
    public int getNumMoves() {
       return level.getNumMoves();
    }

    /**
     * Returns the number of lives that the player has.
     * @return Number of lives.
     */
    public int getNumLives(){
        return level.getNumLives();
    }

    /**
     * Indicates if the game is finished ({@code true}) or not ({@code false}).
     * <p>The game is finished when the attribute {@code currentLevel} is equal to attribute {@code maxLevels}.
     *</p>
     * @return True if there are no more levels and therefore the game is finished. Otherwise, false.
     */
    public boolean isFinished() {
        if(currentLevel == maxLevels)
            return true;
        else
            return false;
    }

    /**
     * Getter of the attribute {@code currentLevel}.
     *
     * @return Value of the attribute {@code currentLevel} that indicates which level the player is playing.
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Checks if there is a new level to play and loads it.<br/>
     * If the game is finished, it returns {@code false}. Otherwise, it returns {@code true}.
     * The game score must be updated when a level is finished.
     * Thus, when the player is playing the first level, game's score is zero.
     *
     * @return True if there is a next level, and it has been loaded correctly. Otherwise, it returns false.
     * @throws LevelException When there is a level exception/problem loading the new level.
     */
    public boolean nextLevel() throws LevelException {
        if(currentLevel > 0)
            setScore(score + level.getScore());
        if(!isFinished()) {
            currentLevel++;
            loadLevel();
            return true;
        }
        return false;
    }

    /**
     * Loads a new level by using the value of the attribute {@code currentLevel}.
     *<p>
     * The pattern of the filename is: fileFolder+"level" + numberLevel + ".txt".
     * </p>
     *
     * @throws LevelException When there is a level exception/problem.
     */
    private void loadLevel() throws LevelException {
        level = new Level(fileFolder + "level" + currentLevel + ".txt");
    }

    /**
     * Checks if the level is completed, i.e. the player has collected all the gems of the board.
     *
     * @return {@code true} if this level is beaten, otherwise {@code false}.
      */
    public boolean isLevelCompleted() {
        if(level.hasLost() || level.hasWon())
            return true;
        else
            return false;
    }

    /**
     * Checks if the player has lost, i.e. the number of lives is zero.
     *
     * @return {@code true} if this the player has lost, otherwise {@code false}.
     */
    public boolean hasLost(){
        return level.hasLost();
    }

    /**
     * Undo one move from the level's stack.
     *
     * @return {@code true} if one move has been undone, otherwise {@code false} (e.g. the stack is empty).
     * @throws LevelException When either the row or the column is wrong.
     */
    public boolean undo() throws LevelException{
       return level.undo();
    }

    /**
     * Reloads the current level, i.e. load the level again.
     *
     * @throws LevelException When there is a level exception/problem.
     */
    public void reload() throws LevelException {
        loadLevel();
    }

    /**
     * Moves the player in the given direction. If the move ends in:
     * <ul>
     * <li>a mine, then it returns {@link MoveResult#DIE}</li>
     * <li>another kind of cell, then it returns {@link MoveResult#OK}</li>
     * </ul>
     * If the first cell is out of bounds, then it returns {@link MoveResult#KO} (i.e. INVALID move)
     *
     * @param direction Direction to move the player in.
     * @return MoveResult object the move is done.
     * @throws LevelException If there are any problems with increaseNumGemsGot.
     */
    public MoveResult movePlayer(Direction direction) throws LevelException {
        Position originPosition = level.getPlayerPosition();
        Element originElement = level.getCell(originPosition).getElement();
        Position currentPosition;
        Position nextPosition;
        Element nextElement;
        StackItem stackItem;
        List<Position> collectedLives = new ArrayList<Position>();
        List<Position> collectedGems = new ArrayList<Position>();
        boolean hasMoved = false;

        while(true) {
            currentPosition = level.getPlayerPosition();
            nextPosition = currentPosition.offsetBy(direction.getRowOffset(), direction.getColumnOffset(), getBoardSize());

            if(nextPosition == null) {
                level.increaseNumMoves();
                stackItem = new StackItem(originPosition, originElement, collectedLives, collectedGems);
                level.push(stackItem);
                return MoveResult.OK;
            }

            if (level.getCell(nextPosition).getElement() == Element.MINE) {
                level.decreaseNumLives();
                level.increaseNumMoves();
                level.setCell(originPosition, originElement);
                for (Position gemPos: collectedGems) {
                    level.setCell(gemPos, Element.GEM);
                    level.decreaseNumGemsGot();
                }
                for (Position livePos: collectedLives) {
                    level.setCell(livePos, Element.EXTRA_LIFE);
                }
                return MoveResult.DIE;
            }

            if (level.getCell(nextPosition).getElement() == Element.STOP) {
                level.setCell(currentPosition, Element.EMPTY);
                level.setCell(nextPosition, Element.PLAYER_STOP);
                stackItem = new StackItem(originPosition, originElement, collectedLives, collectedGems);
                level.push(stackItem);
                level.increaseNumMoves();
                return MoveResult.OK;
            }

            if(level.getCell(nextPosition).getElement() == Element.WALL) {
                if(!hasMoved)
                    return MoveResult.KO;
                else {
                    level.increaseNumMoves();
                    stackItem = new StackItem(originPosition, originElement, collectedLives, collectedGems);
                    level.push(stackItem);
                    return MoveResult.OK;
                }
            }

            if(level.getCell(nextPosition).getElement() == Element.STOP) {
                nextElement = Element.PLAYER_STOP;
            } else {
                nextElement = Element.PLAYER;
            }
            if(level.getCell(currentPosition).getElement() == Element.PLAYER_STOP)
                level.setCell(currentPosition, Element.STOP);
            else {
                level.setCell(currentPosition, Element.EMPTY);
            }
            if(level.getCell(nextPosition).getElement() == Element.GEM) {
                level.increaseNumGemsGot(1);
                collectedGems.add(nextPosition);
            }
            if(level.getCell(nextPosition).getElement() == Element.EXTRA_LIFE) {
                level.increaseNumLives(1);
                collectedLives.add(nextPosition);
            }

            hasMoved = true;
            level.setCell(nextPosition, nextElement);
        }
    }

    /**
     * Checks if the score gotten by the player deserves to be stored in the leaderboard.
     * @return {@code true} if the score can be stored in the leaderboard. Otherwise, {@code false}.
     */
    public boolean isInLeaderBoard(){
        return leaderBoard.isInTheTop(level.getScore());
    }

    /**
     * Add the score in the leaderboard.
     * @param name Player's name.
     */
    public void addToLeaderBoard(String name){
        leaderBoard.add(name, level.getScore());
    }

    /**
     * Prints the leaderboard.
     */
    public void displayLeaderBoard(){
        leaderBoard.toString();
    }

    /**
     * Returns the status of the game at that moment.
     *
     * @return Textual version of the game. This includes the board and level's status.
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        str.append(level.toString())
                .append(System.lineSeparator())
                .append("#Lives: ")
                .append(level.getNumLives())
                .append(" | #Moves: ")
                .append(level.getNumMoves())
                .append(" | #Gems: ")
                .append(level.getNumGemsGot())
                .append(" | Level Score: ")
                .append(level.getScore())
                .append(" pts")
                .append(" | Game Score: ")
                .append(getScore())
                .append(" pts")
                .append(System.lineSeparator())
                .append("Enter Your Move (UP/DOWN/LEFT/RIGHT/UNDO/QUIT): ");

        return str.toString();
    }
}
