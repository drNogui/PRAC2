package edu.uoc.nertia.view.gui;

import edu.uoc.nertia.controller.Game;
import edu.uoc.nertia.model.exceptions.PositionException;
import edu.uoc.nertia.model.utils.Direction;
import edu.uoc.nertia.model.utils.MoveResult;
import edu.uoc.nertia.model.exceptions.LevelException;
import edu.uoc.nertia.model.cells.Cell;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Class that controls the interaction in the Play view.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class PlayController {

    /**
     * Game object that allows to manage the game.
     */
    private Game game;

    /**
     * Size of each cell in the board.
     */
    private static final int CELL_SIZE = 126;

    /**
     * It connects to the UI item that displays the board.
     */
    @FXML
    private Pane canvas;

    /**
     * It connects to the UI item so that the difficulty of level is displayed.
     */
    @FXML
    Label uiDifficulty;

    /**
     * It connects to the UI item so that the number of level is displayed.
     */
    @FXML
    Label uiLevel;

    /**
     * It connects to the UI item so that the number of moves the player has made is displayed.
     */
    @FXML
    Label uiMoves;

    /**
     * It connects to the UI item so that the number of lives the player has is displayed.
     */
    @FXML
    Label uiLives;

    /**
     * It allows us to manage the Alert message which displays "Congrats" when a level is solved.
     */
    private Alert alert;


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @throws IOException    When there is a problem while loading the game.
     * @throws LevelException When there is a problem while loading a new level.
     */
    @FXML
    private void initialize() throws IOException, LevelException {
        game = new Game("levels/");
        alert = new Alert(AlertType.INFORMATION);
        if (game.nextLevel()) update();
    }

    /**
     * Updates the status of the level (i.e. the flow of the game). It also paints the game in the GUI.
     *
     * @throws LevelException When there is a level exception/problem.
     */
    private void update() throws LevelException {
        if (!game.isLevelCompleted()) {
            paint();
        }else {
            //Level completed, then we show an alert (popup) window.
            paint();
            alert.setHeaderText("Congratulations!");
            alert.setContentText("You have solved Level "+game.getCurrentLevel()+"!!");
            alert.showAndWait();
            if(!game.nextLevel()) {
                try {
                    GuiApp.main.createView("GameOver");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(2);
                }
            }else {
                paint();
            }
        }
    }
    /**
     * Paints the level in the GUI.
     */
    private void paint(){
        ObservableList<Node> nodeList = FXCollections.observableArrayList();
        canvas.getChildren().clear();

        uiDifficulty.setText(game.getDifficulty().toString());

        uiLevel.setText("Level " + game.getCurrentLevel());

        uiMoves.setText(String.valueOf(game.getNumMoves()));
        uiLives.setText(String.valueOf(game.getNumLives()));

        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++){
                try{
                    Cell cell = game.getCell(i,j);
                    StackPane sprite = new StackPane();

                    ImageView spriteImage = new ImageView(new Image("/images/"+ cell.getElement().getImageSrc()));


                    spriteImage.setFitWidth(CELL_SIZE);
                    spriteImage.setFitHeight(CELL_SIZE);
                    sprite.getChildren().add(spriteImage);
                    sprite.setTranslateX(CELL_SIZE * cell.getPosition().getColumn());
                    sprite.setTranslateY(CELL_SIZE * cell.getPosition().getRow());
                    sprite.getStyleClass().add("element");

                    nodeList.addAll(sprite);
                }catch(LevelException e){
                    //Nothing to do...
                }
            }
        }

        canvas.getChildren().addAll(nodeList);

    }


    /**
     * Capture the keyboard's keys and moves the player accordingly.
     * Hint:
     * <ul>
     *    <li>KeyCode.LEFT = its ordinal number is 16</li>
     *    <li>KeyCode.UP = 17</li>
     *    <li>KeyCode.RIGHT = 18</li>
     *    <li>KeyCode.DOWN = 19</li>
     * </ul>
     *
     * @param event Object with all the data of the key has been released by the player.
     * @throws LevelException When there is a problem while updating the level.
     * @throws IOException When there is a problem while creating the GameOver view.
     */
    @FXML
    public void onKeyReleased(KeyEvent event) throws LevelException, PositionException, IOException {
        int ordinal = event.getCode().ordinal() - KeyCode.LEFT.ordinal();

        if(ordinal >= 0 && ordinal < Direction.values().length){
            Direction direction = Direction.values()[ordinal];


            if(game.movePlayer(direction).equals(MoveResult.DIE)) {
                if(game.hasLost()) {
                    alert.setHeaderText("You have lost!");
                    alert.setContentText("You don't have more lives!!");
                    alert.showAndWait();
                    GuiApp.main.createView("GameOver");
                    return;
                }
                alert.setHeaderText("Ooops!");
                alert.setContentText("You have died!!");
                alert.showAndWait();
            }
            update();
        }
    }

    public void undo(MouseEvent mouseEvent) throws LevelException {
        game.undo();
        paint();
    }


    public void reload(MouseEvent mouseEvent) throws LevelException {
        game.reload();
        paint();
    }
}
