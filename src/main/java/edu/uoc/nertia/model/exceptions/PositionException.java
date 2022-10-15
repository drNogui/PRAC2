package edu.uoc.nertia.model.exceptions;

public class PositionException extends Exception {

    public static final String POSITION_COLUMN_ERROR = "[ERROR] Position's column cannot be a negative value.";
    public static final String POSITION_ROW_ERROR = "[ERROR] Position's row cannot be a negative value.";

    public PositionException(String msg) {
        super(msg);
    }
}
