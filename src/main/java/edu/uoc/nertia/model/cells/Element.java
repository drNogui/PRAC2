package edu.uoc.nertia.model.cells;
/**
 * Class Element
 * @author Joan Noguera Tomàs
 */
public enum Element {

    EMPTY('-', "empty.png"),
    EXTRA_LIFE('L',"life.png"),
    GEM('*', "gem.png"),
    MINE('X', "mine.png"),
    PLAYER('@', "player.png"),
    STOP('S', "stop.png"),
    PLAYER_STOP('$', "player_stop.png"),
    WALL('#', "wall.png");

    private char symbol;
    private String imageSrc;

    Element(char symbol, String imageSrc) {
        setSymbol(symbol);
        setImageSrc(imageSrc);
    }

    private void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    private void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    /**
     * Símbol de l'element
     * @param symbol símbol de l'element
     * @return
     */
    public static Element symbol2Element(char symbol) {
        for(var aux : Element.values()) {
            if(aux.getSymbol() == symbol) return aux;
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
