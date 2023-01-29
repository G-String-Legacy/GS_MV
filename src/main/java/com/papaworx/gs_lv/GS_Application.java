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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;

import com.papaworx.gs_lv.utilities.Filer;

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
     * <code>root</code> - serves a root for current Preferences.
     */
    private final Preferences root = Preferences.userRoot();

    /**
     * <code>prefs</code> - Preference API.
     */
    final private Preferences prefs = root.node("/com/papaworx/gstring");

    /**
     * Object <code>flr</code> handles most file input/output.
     */
    private Filer flr;

    /**
     * String containing current location of log file output.
     */
    private Logger logger;

    @Override
    public void start(Stage stage) throws IOException {
        String sUser = System.getProperty("user.home");
        String sHomeDir = prefs.get("Home Directory","");
        File fHome = new File (sHomeDir);
        if (!fHome.exists()) {
            sHomeDir = sUser;
            prefs.put("Home Directory", sHomeDir);
        }

        /*
          String containing current location of log file output.
         */
        String sLogPath = sHomeDir + File.separator + "com.papaworx.gstring.Log";
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
        /*
         * controller - Object that controls the GUI.
         *
         */
        GS_Controller controller = fxmlLoader.getController();
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

    /**
     * getter for primary stage available by a call to 'main'
     *
     * @return primaryStage;
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * <a href="https://stackoverflow.com/questions/941754/how-to-get-a-path-to-a-resource-in-a-java-jar-file">...</a>
     * to display Brennan's original uRGENOVA manual pdf.
     * <a href="http://java-buddy.blogspot.com">see also</a>;
     *
     * @param sName;
     * @return File;
     */
    public File showPDF(String sName) {

        File docFile = null;
        InputStream input = getClass().getResourceAsStream(sName);
        try {
            docFile = File.createTempFile("urGenova", ".pdf");
            OutputStream out = new FileOutputStream(docFile);
            int read;
            byte[] bytes = new byte[8192];

            while (true) {
                assert input != null;
                if ((read = input.read(bytes)) == -1) break;
                out.write(bytes, 0, read);
            }
            out.close();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        return docFile;
    }

    /**
     * In response to GUI, initiates setup; to be done on first use.
     */
    public void doSetup() {

        com.papaworx.gs_lv.steps.GSetup setup = new com.papaworx.gs_lv.steps.GSetup(primaryStage, logger, prefs);
        try {
            setup.ask();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

}