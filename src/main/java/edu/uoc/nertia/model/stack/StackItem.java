package edu.uoc.nertia.model.stack;

import edu.uoc.nertia.model.cells.Element;
import edu.uoc.nertia.model.utils.Position;

import java.util.List;

public record StackItem(Position originPosition, Element originElement, List<Position> collectedLives, List<Position> collectedGems) {
}
