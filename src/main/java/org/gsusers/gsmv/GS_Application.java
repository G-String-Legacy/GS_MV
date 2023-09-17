package org.gsusers.gsmv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.gsusers.gsmv.model.Nest;
import org.gsusers.gsmv.steps.GSetup;
import org.gsusers.gsmv.steps.AnaGroups;
import org.gsusers.gsmv.steps.SynthGroups;
import org.gsusers.gsmv.utilities.Filer;
import org.gsusers.gsmv.utilities.TextStack;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Main class of project
 */
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
    final private Preferences root = Preferences.userRoot();

    /**
     * <code>prefs</code> - Preference API.
     */
    final private Preferences prefs = root.node("/org/gsusers/gsmv");

    /**
     * Object <code>flr</code> handles most file input/output.
     */
    private Filer flr;

    /**
     * String containing current location of log file output.
     */
    private Logger logger;

    /**
     * controller - Object that controls the GUI.
     *
     */
    private GS_Controller controller;

    /**
     * Object <code>myNest</code> - encapsulates all experimental model descriptors (excepts
     * sample sizes, and methods to generate logical derivatives.
     */
    private static Nest myNest;

    /**
     * <code>group</code> - encapsulates the various components of a javaFX,
     * specific for each step and condition.
     */
    private Group group;

    /**
     * <code>mySteps</code> - guides the user through all the input steps for performing
     * a Generalizability Analysis.
     */
    private AnaGroups mySteps;

    /**
     * <code>mySynthSteps</code> - guides the user through all the input steps for generating
     * a synthetic dataset, on which Generalizability Analysis can be practiced.
     */
    private SynthGroups mySynthSteps;

    /**
     * flag indicating replication mode.
     */
    private Boolean bReplicate = false;

    /**
     * <code>storedScene</code> location to park current scene, when a
     * temporary scene has to be overlaid.
     */
    private Scene storedScene = null;

    /**
     * 'start' method of javafx application
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException  if FXMLLoader fails to load
     */
    @Override
    public void start(Stage stage) throws IOException {
         /*
          String containing current location of log file output.
         */
        logger = Logger.getLogger("org.gsusers.gsmv");
        FileHandler fh = new FileHandler();							// just for initialization
        logger.addHandler(fh);
        myNest = new Nest(logger, this, prefs);

        /*
            Check for presence of Brennan working directory.
            if not present, create it.
         */

        GSetup gs = new GSetup(logger, prefs);
        gs = null;

        FXMLLoader  fxmlLoader = new FXMLLoader(GS_Application.class.getResource("GS_view.fxml"));
        /*
          the rootLayout of the mainStage is formatted as a <code>BorderPane</code>
         */
        BorderPane rootLayout = fxmlLoader.load();
        primaryStage = stage;
        scene0 = new Scene(rootLayout, 900, 800);
        Image _img = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("GS_MV.png")));
        stage.setTitle("G_String");
        stage.getIcons().add(_img);
        myNest.setScene(scene0);
        flr = new Filer(myNest, prefs, logger, primaryStage);
        /*
         * controller - Object that controls the GUI.
         *
         */
        controller = fxmlLoader.getController();
        controller.setMainApp(this, logger);
        mySteps = new AnaGroups(this, myNest, logger, controller, prefs, flr); 	// object for analysis
        mySynthSteps = new SynthGroups(myNest, logger, controller, prefs, flr);	// object for synthesis

        try {
            stepUp();	// now ready for work
        } catch (Throwable e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * Obligatory main stub of javafx application
     *
     * @param args  optional string array of arguments, unused
     */
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
     * Except for very localized messages, the graphical user interface (GUI) is standardized.
     * Throughout G_String, the objects create javafx scenes called 'groups', which are handed to the 'show' subroutine.
     * User interactions with the GUI are directly fed back to appropriate methods of Main.
     * Responds to GUI 'Next' button.
     */
    public void stepUp() {

        try {
            if (!myNest.getDawdle() && !myNest.getVarianceDawdle())
                myNest.incrementSteps();
            int iStep = myNest.getStep();
            controller.setStep(iStep);
            if (!myNest.getSimulate()) // if in analysis mode
            {
                /*
                  This is the default analysis path
                 */
                group = mySteps.getGroup();
                if (group == null)
                    switch (iStep) {
                        case 1 -> freshStart();
                        case 9 -> myNest.setStep(7);
                        default -> {
                        }
                    }
                else {
                    show(group);
                }
            } else {
                /*
                  Otherwise we got for synthesis
                 */
                group = mySynthSteps.getGroup();
                show(group);
            }
        } catch (Throwable e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logger.warning(e.toString());
        }
    }

    /**
     * In response to GUI sets switches for script driven analysis
     */
    public void startOver() {
        bReplicate = false;
        myNest.setDoOver(true);			// sets the 'doOver' Boolean in 'Nest'
        myNest.setReplicate(false);
        mySteps.setReplicate(false);
        try {
            stepUp();					// and then tries going to 'stepUp'
        } catch (Throwable e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * In response to GUI initiates 'help' action.
     * This method allows context switching to help any time. The current screen
     * content (scene) is stored (only one level), and returned at end of help
     * screen watching. If necessary, this method could be expanded to use a stack to save screens,
     *
     * @param sCommand;
     */
    public void helpSwitch(String sCommand) {

        switch (sCommand) {
            case "help" -> {                                        // context specific help
                Boolean bSimulate = myNest.getSimulate();
                bReplicate = myNest.getReplicate();
                String sLocation;
                Integer iStep = myNest.getStep();
                if (storedScene == null)
                    storedScene = primaryStage.getScene();
                if (bSimulate) {        // get prose from simulation help files
                    if (iStep == 1)
                        sLocation = "HelpRep_1" + ".tf";
                    else
                        sLocation = "HelpSim_" + iStep + ".tf";
                } else {
                    if ((iStep == 1) && bReplicate)
                        sLocation = "HelpRep_1" + ".tf";
                    else
                        sLocation = "Help_" + iStep + ".tf";   // get prose from default (analysis) help files
                }
                primaryStage.setScene(helpScene("Contextual Help", sLocation));
                primaryStage.show();
            }
            case "intro" -> {                                        // serves background prose
                if (storedScene == null)
                    storedScene = primaryStage.getScene();
                primaryStage.setScene(helpScene("Background", "Background.tf"));
                primaryStage.show();
            }
            case "return" -> {                                        // restores pre-help scene
                primaryStage.setScene(storedScene);
                primaryStage.show();
            }
            case "replicate" -> {
                if (storedScene == null)
                    storedScene = primaryStage.getScene();
                primaryStage.setScene(helpScene("replicate", "Replicate.tf"));
                primaryStage.show();
            }
            default -> {
            }
        }
    }

    /**
     * In response to GUI set switches for manual simulation
     */
    public void Simulate() {

        myNest.setDoOver(false); 		// only manual input
        myNest.setSimulate(true); 		// simulate
        myNest.setReplicate(false);
        try {
            stepUp();
        } catch (Throwable e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * In response to GUI sets switches for script driven simulation.
     */
    public void Resimulate() {

        myNest.setDoOver(true); 		// to read script input file
        myNest.setSimulate(true);		// to force simulation
        myNest.setReplicate(false);
        try {
            stepUp();
        } catch (Throwable e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * primitive helper for 'ChangePreference',
     * saves or restores previous scene.
     *
     * @param bPrefs;
     */
    public void switchChangePreferences(Boolean bPrefs) {

        if (bPrefs) {
            storedScene = primaryStage.getScene();
            primaryStage.setScene(prefChanger());
            primaryStage.show();
        } else {
            primaryStage.setScene(storedScene);
            primaryStage.show();
        }
    }

    /**
     * starts G_String all over again. Resets all switches and Nest to default
     */
    public void freshStart() {

        myNest = null;
        mySteps = null;
        myNest = new Nest(logger, this, prefs);
        flr = new Filer(myNest, prefs, logger, primaryStage);
        myNest.setStage();
        group = null;
        mySteps = new AnaGroups(this, myNest, logger, controller, prefs, flr);
        controller.callForAction(true);
        try {
            stepUp();
        } catch (Throwable e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * In response GUI activates the analysis branch to save the collected results.
     */
    public void saveAll() {

        try {
            mySteps.saveAll();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * User has selected replication analysis.
     */
    public void replicate() {
        myNest.setDoOver(false);
        bReplicate = true;
        myNest.setReplicate(true);
        mySteps.setReplicate(true);
        try {
            stepUp();					// and then tries going to 'stepUp'
        } catch (Throwable e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * User has selected replication synthesis
     */
    public void replicateAgain() {
        myNest.setDoOver(true);
        bReplicate = true;
        myNest.setReplicate(true);
        mySteps.setReplicate(true);
        try {
            stepUp();					// and then tries going to 'stepUp'
        } catch (Throwable e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * In response to GUI sets switches for manual analysis
     */
    public void startFresh() {
        bReplicate = false;
        myNest.setReplicate(false);
        myNest.setDoOver(false);
        mySteps.setReplicate(false);
        try {
            stepUp();
        } catch (Throwable e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * Sets up the standard 'help' scene
     *
     * @param _sTitle  string	the Help screen title;
     * @param _sSource string	the help text file location;
     * @return Scene to be displayed;
     */
    private Scene helpScene(String _sTitle, String _sSource) {

        BorderPane helpLayout = new BorderPane();
        helpLayout.setPrefSize(800.0, 500.0);
        Label lbTitle = new Label(_sTitle);
        lbTitle.setStyle(prefs.get("StandardFormat", null));
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setStyle(prefs.get("StandardFormat", null));
        topBox.setPrefHeight(30.0);
        topBox.setStyle("-fx-border-color:chocolate;-fx-border-width:1px;");
        topBox.getChildren().add(lbTitle);
        helpLayout.setTop(topBox);
        Button closeButton = new Button("Close");
        closeButton.setOnAction((event) -> helpSwitch("return"));
        HBox bottomBox = new HBox();
        bottomBox.setStyle(prefs.get("StandardFormat", null));
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        bottomBox.setStyle("-fx-border-color:chocolate;-fx-border-width:1px;");
        bottomBox.getChildren().add(closeButton);
        helpLayout.setBottom(bottomBox);
        //
        TextStack ts = new TextStack(_sSource, prefs, logger);
        VBox vb = ts.vStack();
        vb.setStyle("-fx-background-color:beige;");
        helpLayout.setCenter(vb);
        return new Scene(helpLayout);
    }

    /**
     * In response to GUI, allows user to set program preferences that will be stored.
     * <p>
     * {@code @return,} Scene for preferences control;
     *
     * @return  javafx scene showing user preferences, interactive
     */
    public Scene prefChanger() {

        BorderPane pcLayout = new BorderPane();
        pcLayout.setPrefSize(800.0, 500.0);
        Label lbTitle = new Label("Change Preferences");
        lbTitle.setStyle(prefs.get("StandardFormat", null));
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setStyle(prefs.get("StandardFormat", null));
        topBox.getChildren().add(lbTitle);
        pcLayout.setTop(topBox);
        Button closeButton = new Button("Close");
        closeButton.setOnAction((event) -> switchChangePreferences(false));
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        bottomBox.getChildren().add(closeButton);
        pcLayout.setBottom(bottomBox);
        VBox vbPrefs = new VBox();
        // now compile preferences
        List<String> sarKeys = null;
        String sValue;
        try {
            sarKeys = Arrays.asList(prefs.keys());
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        assert sarKeys != null;
        Collections.sort(sarKeys);

        for (String sKey : sarKeys) {
            sValue = prefs.get(sKey, null);
            vbPrefs.getChildren().add(hbKeyValue(sKey, sValue));
        }
        vbPrefs.setPadding(new Insets(40, 50, 20, 50));
        pcLayout.setCenter(vbPrefs);
        return new Scene(pcLayout);
    }
    /**
     * helper in Preferences, displays individual preference.
     *
     * @param _sKey		string	the key for a particular preference item
     * @param _sValue	string	the new value for that preference item
     * @return HBox, to be displayed in <code>prefChanger</code>;
     */
    private HBox hbKeyValue(String _sKey, String _sValue) {

        HBox hbReturn = new HBox();
        Label lbKey = new Label(_sKey);
        lbKey.setPrefWidth(150.0);
        TextField tfValue = new TextField(_sValue);
        tfValue.setPrefWidth(500.0);
        tfValue.textProperty().addListener((obs, oldText, newText) -> {
            if (_sKey.equals("Default Log") && !newText.equals(oldText)) {
                if (Level.parse(newText.toUpperCase()) != null)
                    prefs.put(_sKey, newText.toUpperCase());
            }
            else
            if ((newText != null) && (!newText.trim().equals("")) && (!newText.equals(oldText)))
                prefs.put(_sKey, newText);
        });
        hbReturn.getChildren().addAll(lbKey, tfValue);
        return hbReturn;
    }
}

