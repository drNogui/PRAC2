package edu.uoc.nertia.model.cells;

import edu.uoc.nertia.model.utils.Position;

/**
 * Class Cell
 * @author Joan Noguera Tomàs
 */
public class Cell {
    /**
     * Element
     */
    private Element element;
    /**
     * Posició
     */
    private Position position;

    /**
     * Constructor
     * @param position posició cel·la
     * @param element element cel·la
     */
    public Cell(Position position, Element element) {
        setElement(element);
        setPosition(position);
    }

    /**
     * Getter element
     * @return element
     */
    public final Element getElement() {
        return element;
    }

    /**
     * Getter position
     * @return position
     */
    public final Position getPosition() {
        return position;
    }

    /**
     * Setter element
     * @param element element de la cel·la
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * Setter position
     * @param position posició de la cel·la
     */
    private void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.valueOf(element.getSymbol());
    }
}
