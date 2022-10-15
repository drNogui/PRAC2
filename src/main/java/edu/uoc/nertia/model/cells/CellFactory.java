package edu.uoc.nertia.model.cells;

import edu.uoc.nertia.model.exceptions.PositionException;
import edu.uoc.nertia.model.utils.Position;

/**
 * Cell Simple Factory class.
 * @author David García Solórzano
 * @version 1.0
 */
public abstract class CellFactory {

    /**
     * Returns a new {@link Cell} object.
     *
     * @param row Row of the coordinate/position in which the cell is in the board.
     * @param column Column of the coordinate/position in which the cell is in the board.
     * @param elementSymbol String value of the {@link Element} enumeration that corresponds to the cell.
     * @return {@link Cell} object.
     * @throws edu.uoc.nertia.model.exceptions.PositionException When there is a problem with the position.
     */
    public static Cell getCellInstance(int row, int column, char elementSymbol) throws PositionException {
        Position position = new Position(row, column);
        return new Cell(position, Element.symbol2Element(elementSymbol));
    }
}
