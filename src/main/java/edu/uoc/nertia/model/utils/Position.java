package edu.uoc.nertia.model.utils;

import edu.uoc.nertia.model.exceptions.PositionException;

import java.util.Objects;

public class Position {

    private int row;
    private int column;

    public Position(int row, int column) throws PositionException {
        setRow(row);
        setColumn(column);
    }

    private void setRow(int row) throws PositionException {
        if (row < 0) {
            throw new PositionException(PositionException.POSITION_ROW_ERROR);
        }
        else this.row = row;
    }

    private void setColumn(int column) throws PositionException {
        if (column < 0) throw new PositionException(PositionException.POSITION_COLUMN_ERROR);
        else this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Position offsetBy(int row, int column) {
        if (this.row + row < 0 || this.column + column < 0) return null;
        Position position = null;
        try {
            position = new Position(this.row + row, this.column + column);
        } catch (PositionException ignored) {
        }
        return position;
    }

    public Position offsetBy(int row, int column, int size) {
        if (this.row + row < 0 || this.column + column < 0 || this.row + row >= size || this.column + column >= size) return null;
        Position position = null;
        try {
            position = new Position(this.row + row, this.column + column);
        } catch (PositionException ignored) {
        }
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
