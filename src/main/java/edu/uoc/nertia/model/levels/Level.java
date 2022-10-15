package edu.uoc.nertia.model.levels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import edu.uoc.nertia.model.exceptions.PositionException;
import edu.uoc.nertia.model.cells.Cell;
import edu.uoc.nertia.model.cells.CellFactory;
import edu.uoc.nertia.model.cells.Element;
import edu.uoc.nertia.model.exceptions.LevelException;
import edu.uoc.nertia.model.stack.StackItem;
import edu.uoc.nertia.model.stack.UndoStack;
import edu.uoc.nertia.model.utils.Position;

/**
 * Level class.
 * @author David García Solórzano
 * @version 1.0
 */
public class Level {

    /**
     * Minimum size of the board in one direction, the board will be sizexsize
     */
    private static final int MIN_SIZE = 3;

    /**
     * Number representing unlimited number of lives for a player.
     */
    private static final int UNLIMITED_LIVES = -1;

    /**
     * Number of rows and columns in the game board. A board is a square of size x size.
     */
    private final int size;

    /**
     * Difficulty of the level
     */
    private LevelDifficulty difficulty;

    /**
     * 2D array representing each cell in the game board.
     */
    private Cell[][] board;

    /**
     * The number of moves performed by the player (excluding invalid moves).
     */
    private int numMoves = 0;

    /**
     * The number of lives the player has.
     */
    private int numLives;

    /**
     * The number of gems the player has got.
     */
    private int numGemsGot = 0;

    /**
     * The number of gems initially on the game board when a {@link Level} instance was created.
     */
    private final int numGemsInit;

    /**
     * Data structure that allows us to undo moves and manage its information.
     */
    private final UndoStack undoStack;

    /**
     * Constructor
     *
     * @param fileName Name of the file that contains level's data.
     * @throws LevelException When there is any error while parsing the file.
     */
    public Level(String fileName) throws LevelException{
        size = parse(fileName);
        numGemsInit = (int)
                Arrays.stream(getBoard()).flatMap(Arrays::stream)
                        .filter(cell -> cell.getElement() == Element.GEM)
                        .count();
        undoStack = new UndoStack();
    }

    /**
     * Parses/Reads level's data from the given file.<br/>
     * It also checks which the board's requirements are met.
     *
     * @param fileName Name of the file that contains level's data.
     * @return The size of the board in one direction (i.e. row or column). The board is {@code size x size}.
     * @throws LevelException When there is any error while parsing the file
     * or some board's requirement is not satisfied.     *
     */
    private int parse(String fileName) throws LevelException{
        String line;
        int size = 0;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(fileName));

        try(InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader)){

            line = getFirstNonEmptyLine(reader);

            if (line != null) {
                setNumLives(Integer.parseInt(line));
            }

            line = getFirstNonEmptyLine(reader);

            if (line  != null) {
                size = Integer.parseInt(line);
                if(size < MIN_SIZE){
                    throw new LevelException(LevelException.SIZE_ERROR);
                }
            }

            line = getFirstNonEmptyLine(reader);

            if (line != null) {
                setDifficulty(LevelDifficulty.valueOf(line));
            }

            board = new Cell[size][size];

            for (int row = 0; row < size; row++) {
                char[] rowChar = Objects.requireNonNull(getFirstNonEmptyLine(reader)).toCharArray();
                for (int column = 0; column < size; column++) {
                    board[row][column] = CellFactory.getCellInstance(row, column,rowChar[column]);
                }
            }

            //Checks if there are more than one finish cell
            if(Stream.of(board).flatMap(Arrays::stream).filter(x -> x.getElement() == Element.PLAYER).count()!=1){
                throw new LevelException(LevelException.PLAYER_LEVEL_FILE_ERROR);
            }

            //Checks if there are one gem at least.
            if(Stream.of(board).flatMap(Arrays::stream).filter(x -> x.getElement() == Element.GEM).count()<1){
                throw new LevelException(LevelException.MIN_GEMS_ERROR);
            }

        }catch (IllegalArgumentException | IOException | PositionException e){
            throw new LevelException(LevelException.PARSING_LEVEL_FILE_ERROR);
        }

        return size;
    }

    /**
     * This is a helper method for {@link #parse(String fileName)} which returns
     * the first non-empty and non-comment line from the reader.
     *
     * @param br BufferedReader object to read from.
     * @return First line that is a parsable line, or {@code null} there are no lines to read.
     * @throws IOException if the reader fails to read a line.
     */
    private String getFirstNonEmptyLine(final BufferedReader br) throws IOException {
        do {

            String s = br.readLine();

            if (s == null) {
                return null;
            }
            if (s.isBlank() || s.startsWith("/")) {
                continue;
            }

            return s;
        } while (true);
    }

    public int getSize() {
        return size;
    }

    public LevelDifficulty getDifficulty() {
        return difficulty;
    }

    private void setDifficulty(LevelDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    private boolean hasUnlimitedLives() {
        return (numLives == UNLIMITED_LIVES);
    }

    public int getNumLives() {
        if(hasUnlimitedLives())
            return Integer.MAX_VALUE;
        else
            return numLives;
    }

    private void setNumLives(int numLives) {
        if(numLives <= 0)
            this.numLives = UNLIMITED_LIVES;
        else
            this.numLives = numLives;
    }

    public void increaseNumLives(int num) throws LevelException {
        if(num < 0)
            throw new LevelException(LevelException.INCREASE_NUM_LIVES_ERROR);
        else
            setNumLives(this.numLives + num);
    }

    public void decreaseNumLives() {
        if (!hasUnlimitedLives() && this.numLives > 0)
            this.numLives = this.numLives - 1;
    }

    public void increaseNumGemsGot(int numGemsGot) throws LevelException {
        if (numGemsGot < 0)
            throw new LevelException(LevelException.INCREASE_NUM_GEMS_GOT_ERROR);
        else
            this.numGemsGot += numGemsGot;
    }

    public void decreaseNumGemsGot() {
        if(numGemsGot > 0)
            numGemsGot = numGemsGot - 1;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public int getNumGemsGot() {
        return numGemsGot;
    }

    public int getNumGemsInit() {
        return numGemsInit;
    }

    public void increaseNumMoves() {
        numMoves = numMoves + 1;
    }

    public boolean hasWon() {
        if(getNumGemsGot() == getNumGemsInit())
            return true;
        else
            return false;
    }

    public boolean hasLost() {
        if(getNumLives() == 0)
            return true;
        else
            return false;
    }

    private Cell[][] getBoard() {
        return board;
    }

    public Cell getCell(int row, int column) throws LevelException {
        if(row < 0 || row >= size || column < 0 || column >= size)
            throw new LevelException(LevelException.INCORRECT_CELL_POSITION);
        else
            return board[row][column];
    }

    public Cell getCell(Position position) throws LevelException {
        return getCell(position.getRow(), position.getColumn());
    }

    public void setCell(Position position, Element element) throws LevelException {
        if(position.getRow() < 0 || position.getRow() > size || position.getColumn() < 0 || position.getColumn() > size)
            throw new LevelException(LevelException.INCORRECT_CELL_POSITION);
        if(element != null) {
            board[position.getRow()][position.getColumn()].setElement(element);
        }
    }

    public Position getPlayerPosition() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++)
            {
                if(board[i][j].getElement() == Element.PLAYER || board[i][j].getElement() == Element.PLAYER_STOP)
                    return board[i][j].getPosition();
            }
        }
        return null;
    }

    public int getScore() {
        return size * size + (10 * numGemsGot) - numMoves - (2 * undoStack.getNum());
    }

    public void push(StackItem item) {
        undoStack.push(item);
    }

    public boolean undo() throws LevelException {
        if (undoStack.empty())
            return false;
        StackItem stackItem = undoStack.pop();
        increaseNumLives(stackItem.collectedLives().size());
        increaseNumGemsGot(stackItem.collectedGems().size());
        if(getCell(getPlayerPosition().getRow(), getPlayerPosition().getColumn()).getElement() == Element.PLAYER_STOP)
            board[getPlayerPosition().getRow()][getPlayerPosition().getColumn()].setElement(Element.STOP);
        else
            board[getPlayerPosition().getRow()][getPlayerPosition().getColumn()].setElement(Element.EMPTY);
        Cell newCell = new Cell(stackItem.originPosition(), stackItem.originElement());
        board[stackItem.originPosition().getRow()][stackItem.originPosition().getColumn()] = newCell;
        for(Position gemPos: stackItem.collectedGems())
            board[gemPos.getRow()][gemPos.getColumn()].setElement(Element.GEM);
        for(Position livePos: stackItem.collectedLives())
            board[livePos.getRow()][livePos.getColumn()].setElement(Element.EXTRA_LIFE);
        return true;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                try {
                    str += getCell(i,j).getElement().toString();
                } catch(Exception e) {
                }
            }
            if(i+1 != size)
                str += System.lineSeparator();
        }
        return str;
    }
}
