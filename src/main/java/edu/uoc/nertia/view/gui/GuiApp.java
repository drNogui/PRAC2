package edu.uoc.nertia.view.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Main class that controls the GUI view.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class GuiApp extends Application {

    /**
     * It is the component where the GUI is displayed.
     */
    private Stage stage;

    /**
     * It is a reference to this class so that other classes related to the different views can use it.
     */
    public static GuiApp main;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        main = this;
        stage = primaryStage;
        stage.setTitle("UOCTrip");
        stage.setResizable(false);
        createView("Welcome");
    }

    /**
     * Entry point of the program when Gradle's "runGuiVersion" is used.
     *
     * @param args This parameter is not needed.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * It shows the FXML view that is indicated.
     *
     * @param view Name of the FXML file.
     * @throws IOException When there is an error while loading the FXML file.
     */
    public void createView(String view) throws IOException {
        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/"+view+".fxml"));

        loader.setBuilderFactory(new JavaFXBuilderFactory());
        Region rootLayout = loader.load();

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.show();
    }
}
