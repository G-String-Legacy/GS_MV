package com.papaworx.gs_lv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Group;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.File;
import java.util.prefs.Preferences;


import java.io.IOException;

public class GS_Application extends Application {

    /**
     * The primary stage of javaFX used as main window for the G_String GUI.
     */
    private Stage primaryStage;

    /**
     * <code> scene0</code> acts a container to send the javaFX code <code>group</code>
     * to <code>primaryStage</code> for display.
     */
    private Scene scene0;

    /**
     * controller - Object that controls the GUI.
     *
     */
    private com.papaworx.gs_lv.GS_Controller controller;

    /**
     * <code>root</code> - serves a root for current Preferences.
     */
    private Preferences root = Preferences.userRoot();

    /**
     * <code>prefs</code> - Preference API.
     */
    final private Preferences prefs = root.node("/com/papaworx/gstring");

    /**
     * Object <code>flr</code> handles most file input/output.
     */
    //private Filer flr;

    /**
     * String containing current location of log file output.
     */
    private Logger logger;

    /**
     * String containing current location of log file output.
     */
    private String sLogPath = null;

    @Override
    public void start(Stage stage) throws IOException {
        String sUser = System.getProperty("user.home");
        String sHomeDir = prefs.get("Home Directory","");
        File fHome = new File (sHomeDir);
        if (!fHome.exists()) {
            sHomeDir = sUser;
            prefs.put("Home Directory", sHomeDir);
        }
        sLogPath = sHomeDir + File.separator + "com.papaworx.gstring.Log";
        FileHandler fh = null;							// just for initialization
        try {
            fh = new FileHandler(sLogPath, true);		// log handler, creates append logs, rather than new ones
        } catch (IOException e1) {
            e1.printStackTrace(); 						// emergency exit
        }
        logger = Logger.getLogger(com.papaworx.gs_lv.GS_Application.class.getName());
        logger.addHandler(fh);


        FXMLLoader fxmlLoader = new FXMLLoader(GS_Application.class.getResource("GS_view.fxml"));
        primaryStage = stage;
        scene0 = new Scene(fxmlLoader.load(), 900, 800);
        stage.setTitle("G_String - GS_L.5.0");
        controller = fxmlLoader.getController();
        controller.setMainApp(this, logger, prefs);
        // temporary until working classes show GUI
        show(null);
    }

    public static void main(String[] args) {
        launch();
    }


    /**
     * Central display king pin. Receives scene (group), and passes it on to the GUI.
     *
     * @param _display;
     */
    public void show(Group _display) {

        BorderPane frame = (BorderPane) scene0.getRoot();
        frame.setCenter(_display);
        Scene newScene = scene0;
        newScene.setRoot(frame);
        primaryStage.setScene(newScene);
        primaryStage.show();
    }

}