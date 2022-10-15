package edu.uoc.nertia.view.cmd;

import edu.uoc.nertia.controller.Game;
import edu.uoc.nertia.model.exceptions.LevelException;
import edu.uoc.nertia.model.exceptions.PositionException;
import edu.uoc.nertia.model.utils.Direction;
import edu.uoc.nertia.model.utils.MoveResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Class that controls the interaction in the textual/command view.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class CmdApp {

    /**
     * Game object that allows to manage the game.
     */
    Game game;

    /**
     * Initializes a new game with the folder which contains
     * the levels' configuration files.
     *
     * @throws IOException When there is a problem while loading the game.
     */
    public CmdApp() throws IOException {
        this.game = new Game("levels/");
    }

    /**
     * Manages the idle process of the game.
     *
     * @throws LevelException When there is a  Level problem.
     * @throws PositionException When there is a Position problem.
     */
    public void launchGame() throws LevelException, PositionException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean quit = false;
        boolean die = false;

        while(game.nextLevel() && !game.hasLost() && !quit && !die) { //and the player is alive
            System.out.println("LEVEL "+game.getCurrentLevel()+ " - "+ game.getDifficulty());

            // print board and keep accepting moves until game is over
            while (!game.isLevelCompleted()) {
                System.out.println(game);

                try{
                    final var userInput = br.readLine();

                    if(userInput.toLowerCase().startsWith("quit")) {
                        quit = true;
                        break;
                    }

                   if(userInput.toLowerCase().startsWith("undo")){
                        if(!game.undo())
                            System.out.println("The undo stack is empty");

                        continue;
                   }

                    final var direction = parseDirection(userInput);

                    if(direction == null){
                        System.out.println("Invalid choice! Press [ENTER] to continue...");
                        br.readLine();
                        continue;
                    }

                    MoveResult result = game.movePlayer(direction);

                    if(result.equals(MoveResult.DIE)){
                        if(!game.hasLost()) {
                            System.out.println("You has lost one life!!! Press [ENTER] to continue...");
                            br.readLine();
                        }else{
                            System.out.println("GAME OVER!!!  Press [ENTER] to continue...");
                            br.readLine();
                            die = true;
                            break;
                        }
                    }else if(result.equals(MoveResult.KO)){
                        System.out.println("Invalid move. Please, try again! Press [ENTER] to continue...");
                        br.readLine();
                    }else if(game.isLevelCompleted()){
                        System.out.println("Congrats!! You have completed Level "+game.getCurrentLevel()
                                +" with "+game.getNumMoves()+" moves!!");
                        System.out.println("Press enter to continue...");
                        br.readLine();
                        break;
                    }
                }catch(IOException e){
                    System.err.println(e.getMessage());
                    System.out.println("Please, try again!");
                }
            }
        }

        if(quit) {
            System.out.println("I'm sad because you want to leave...");
        }else if(die){
            System.out.println("Oops!! You have died in Level "+game.getCurrentLevel()+"!!!");
        }else if(game.isFinished()) {
            System.out.println("Great!!! You have solved the whole game!!!");

            if (game.isInLeaderBoard()) {
                System.out.println("Fantastic!!! You are in the leaderboard. Please write your name : ");
                try {
                    String name = br.readLine();
                    game.addToLeaderBoard(name);
                } catch (IOException e) {
                    game.addToLeaderBoard("UOC");
                }
            }

            game.displayLeaderBoard();
        }else{
            System.out.println("Bye!");
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Parses the input string as a direction.
     *
     * <p>
     * This implementation only requires the input string to match the prefix of a direction. For example, all following
     * strings will match with {@link Direction#UP}:
     *
     * <ul>
     *     <li>{@code U}</li>
     *     <li>{@code UP}</li>
     *     <li>{@code u}</li>
     *     <li>{@code up}</li>
     * </ul>
     * <p>
     * The matching is also case-insensitive.
     * </p>
     *
     * @param input The string input to parse as a direction.
     * @return The parsed {@link Direction}, or {@code null} if the direction cannot be determined from the input
     * string.
     */
    private static Direction parseDirection( final String input) {
        Objects.requireNonNull(input);

        if (input.isBlank()) {
            return null;
        }

        final var inputUpperCase = input.toUpperCase();

        for (var dir : Direction.values()) {
            if (dir.toString().startsWith(inputUpperCase)) {
                return dir;
            }
        }

        return null;
    }

    /**
     * Main method: entry point of the program when Gradle's "runCmdVersion" is used.
     *
     * @param args This parameter is not needed.
     */
    public static void main(String[] args) {
        System.out.println("Starting...");
        try {
            CmdApp cmd = new CmdApp();
            cmd.launchGame();
        } catch (IOException | LevelException | PositionException e) {
            e.printStackTrace();
        }

        System.out.println("Finishing... bye!!");
    }
}
