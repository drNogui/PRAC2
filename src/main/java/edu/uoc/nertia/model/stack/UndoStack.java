package edu.uoc.nertia.model.stack;

import java.util.Stack;

public class UndoStack extends Stack<StackItem> {

    private int num;

    public UndoStack() {
        num = 0;
    }

    public int getNum() {
        return num;
    }

    private void incrementNumPops() {
        num += 1;
    }

    public StackItem pop() {
        incrementNumPops();
        return super.pop();
    }
}
