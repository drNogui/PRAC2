package edu.uoc.nertia.view.gui;

import java.io.IOException;

import javafx.fxml.FXML;

/**
 * Class that controls the interaction in the Game Over view.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class GameOverController {

    /**
     * It goes to the Welcome view.
     */
    @FXML
    public void back() {
        try{
            GuiApp.main.createView("Welcome");
        }catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * It exits from the game.
     */
    @FXML
    public void exit(){
        System.exit(0);
    }
}
