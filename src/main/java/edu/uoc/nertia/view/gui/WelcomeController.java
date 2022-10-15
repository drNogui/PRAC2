package edu.uoc.nertia.view.gui;

import java.io.IOException;

import javafx.fxml.FXML;

/**
 * Class that controls the interaction in the Welcome view.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class WelcomeController{
    /**
     * It goes to the Play view.
     */
    @FXML
    public void start(){
        try{
            GuiApp.main.createView("Play");
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
