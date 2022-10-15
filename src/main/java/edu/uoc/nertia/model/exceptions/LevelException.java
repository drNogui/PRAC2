package edu.uoc.nertia.model.exceptions;

public class LevelException extends Exception {

    public static final String SIZE_ERROR = "[ERROR] Board's size must be greater than 2!!";
    public static final String INCORRECT_CELL_POSITION = "[ERROR] Either the row or the column of the cell that you want to retrieve" + " is incorrect!!";
    public static final String INCREASE_NUM_GEMS_GOT_ERROR = "[ERROR] You cannot increase the number of gems with a negative value!!";
    public static final String INCREASE_NUM_LIVES_ERROR = "[ERROR] You cannot increase the number of lives with a negative value!!";
    public static final String PARSING_LEVEL_FILE_ERROR = "[ERROR] There was an error while loading the current level file!!";
    public static final String PLAYER_LEVEL_FILE_ERROR = "[ERROR] Either there is not player or there are more than one!!";
    public static final String MIN_GEMS_ERROR = "[ERROR] This level does not contain any gems!!";

    public LevelException(String msg) {
        super(msg);
    }

}
