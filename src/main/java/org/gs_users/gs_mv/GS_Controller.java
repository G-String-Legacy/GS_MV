package org.gs_users.gs_mv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import javafx.scene.layout.BorderPane;
import javafx.application.HostServices;

import org.gs_users.gs_mv.model.Nest;
import org.gs_users.gs_mv.steps.AnaGroups;
import org.gs_users.gs_mv.steps.SynthGroups;
import org.gs_users.gs_mv.utilities.Filer;
/**
 * LayoutController for JavaFX GUI
 * manage the communication between <code>Main</code> and the GUI.
 *
 * @see <a href="https://github.com/G-String-Legacy/G_String/blob/main/workbench/GS_L/src/view/rootLayout.fxml">view.rootLayoutController</a>
 * @author ralph
 * @version %v..%
 *
 */
public class GS_Controller {
    /**
     * main org.gs_users.gs_lv.GS_Application of project
     */
    private org.gs_users.gs_mv.GS_Application myMain;

    /**
     * org.gs_users.gs_lv.GS_Application logger
     */
    private Logger logger;

    /**
     * org.gs_users.gs_lv.GS_Application preferences
     */
    private Preferences prefs;
    /**
     * primary container of FXML object (GS_view)
     */
    public BorderPane bpScreen;

    /**
     * Object <code>myNest</code> - encapsulates all experimental model descriptors (excepts
     * sample sizes, and methods to generate logical derivatives.
     */
    private static Nest myNest;

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
     * <code> scene0</code> acts a container to send the javaFX code <code>group</code>
     * to <code>primaryStage</code> for display.
     */
    private Scene scene0;

    /**
     * controller - Object that controls the GUI.
     *
     */
    private org.gs_users.gs_mv.GS_Controller controller;

    /**
     * <code>group</code> - encapsulates the various components of a javaFX,
     * specific for each step and condition.
     */
    private Group group;

    /**
     * <code>storedScene</code> location to park current scene, when a
     * temporary scene has to be overlaid.
     */
    private Scene storedScene = null;

    /**
     * <code>root</code> - serves a root for current Preferences.
     */
    private Preferences root = Preferences.userRoot();


    /**
     * <code>newScene</code> - used to generate a fresh display.
     */
    private Scene newScene = null;

    /**
     * Object <code>flr</code> handles most file input/output.
     */
    private Filer flr;

    /**
     * String containing current location of log file output.
     */
    private String sLogPath = null;

    /**
     * flag indicating replication mode.
     */
    private Boolean bReplicate = false;

    /**
     * location current data directory
     */
    private String homeDir = null;

    @FXML
    private MenuItem mnuExit;
    @FXML
    private MenuItem mnuActionFresh;
    @FXML
    private MenuItem mnuActionStartOver;
    @FXML
    private MenuItem mnuSimulate;
    @FXML
    private MenuItem mnuResimulate;
    @FXML
    private MenuItem mnuSetup;
    @FXML
    private MenuItem mnuPreferences;
    @FXML
    private MenuItem mnuCHelp;
    @FXML
    private MenuItem mnuIntro;
    @FXML
    private MenuItem mnuAbout;
    @FXML
    private MenuItem mnuUHelp;
    @FXML
    private MenuItem mnuAboutB;
    @FXML
    private MenuItem mnuStart;
    @FXML
    private MenuItem mnuSaveAll;
    @FXML
    private MenuItem mnuChangePrefs;
    @FXML
    private MenuItem mnuLoadPrefs;
    @FXML
    private MenuItem mnuSavePrefs;
    @FXML
    private MenuItem mnuReplicate;
    @FXML
    private MenuItem mnuReReplicate;
    @FXML
    private Button btnStepUp;
    @FXML
    private Label lblStep;
    @FXML
    private Menu mnuAction;

    /*@FXML
    void typedBS(KeyEvent event) {
        if (event.getCode() == KeyCode.BACK_SPACE) {
            lblStep.setText(event.getText() + " typed.");
        }
    }*/

    /**
     * initialize rootLayoutController at program start.
     */
    public void initialize() {

        mnuExit.setOnAction((event) -> {
            respond();
        });
        btnStepUp.setOnAction((event) -> myMain.stepUp());

        mnuActionStartOver.setOnAction((event) -> {
            myMain.startOver();
        });
        mnuCHelp.setOnAction((event) -> {
            myMain.helpSwitch("help");
        });
        mnuIntro.setOnAction((event) -> {
            myMain.helpSwitch("intro");
        });
        mnuUHelp.setOnAction((event) -> {
            displayResource("urGENOVA_manual.pdf");
        });
        mnuSimulate.setOnAction((event) -> {
            myMain.Simulate();
        });
        mnuResimulate.setOnAction((event) -> {
            myMain.Resimulate();
        });
        mnuSetup.setOnAction((event) -> {
            myMain.doSetup();
        });
        mnuChangePrefs.setOnAction((event) -> {
            myMain.switchChangePreferences(true);
        });
        mnuLoadPrefs.setOnAction((event) -> {
            loadPreferences();
        });
        mnuSavePrefs.setOnAction((event) -> {
            savePreferences();
        });
        mnuAbout.setOnAction((event) -> {
            about();
        });
        mnuAboutB.setOnAction((event) -> {
            aboutB();
        });
        mnuStart.setOnAction((event) -> {
            myMain.freshStart();
        });
        mnuSaveAll.setOnAction((event) -> {
            myMain.saveAll();
        });
        mnuReplicate.setOnAction((event) -> {
            myMain.replicate();
        });
        mnuReReplicate.setOnAction((event) -> {
            myMain.replicateAgain();
        });
        lblStep.setAlignment(Pos.CENTER);
        lblStep.setText("Step 0");
        mnuActionFresh.setOnAction((event) -> {
            myMain.startFresh();
        });

        callForAction(true);
    }

    /**
     * method to set class variables from GS_Application
     * @param _main pointer to GS_Application
     * @param _logger pointer to system logger
     * @param _prefs pointer to system preferences
     */
    void setMainApp(org.gs_users.gs_mv.GS_Application _main, Logger _logger, Preferences _prefs){
        myMain = _main;
        logger = _logger;
        prefs = _prefs;
    }

    /**
     * switch between select action and stepping
     *
     * @param b  boolean switch  on/off
     */
    public void buttonsEnabled(boolean b) {
        btnStepUp.setDisable(!b);
        mnuActionFresh.setDisable(b);
        mnuActionStartOver.setDisable(b);
    }

    /**
     * exit program
     */
    private void respond() {
        System.exit(0);
    }

    /**
     * control stepping
     *
     * @param _iStep  control parameter
     */
    public void setStep(Integer _iStep) {
        lblStep.setText("Step " + _iStep);
        if (_iStep == 0)
            buttonsEnabled(false);
        else
            buttonsEnabled(true);
    }

    /**
     * disable setup changes during stepping operation
     */
    public void lockSetup() {
        mnuSetup.setDisable(true);
    }

    /**
     * displaye 'About G_String' info
     */
    private void about() {
        org.gs_users.gs_mv.utilities.About myAbout = new org.gs_users.gs_mv.utilities.About(myMain.getPrimaryStage(), logger, "About.txt", "About G_String_L");
        myAbout.show();
    }

    /**
     * display 'About Brennan' info
     */
    private void aboutB() {
        org.gs_users.gs_mv.utilities.About myAbout = new org.gs_users.gs_mv.utilities.About(myMain.getPrimaryStage(), logger, "AboutB.txt", "About urGenova");
        myAbout.show();
    }

    /**
     * disables/enables 'Save All' operation (Results)
     *
     * @param bDisable boolean switch true/false
     */
    public void disableSave(Boolean bDisable) {
        mnuSaveAll.setDisable(bDisable);
    }

    /**
     * load preferences from file
     */
    private void loadPreferences() {
        InputStream sIn = null;
        File selectedFile = null;
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Preferences File to load");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fc.getExtensionFilters().add(extFilter);
        fc.setSelectedExtensionFilter(extFilter);
        fc.setInitialDirectory(new File(homeDir));
        try {
            selectedFile = fc.showOpenDialog(myMain.getPrimaryStage());
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        if (selectedFile != null) {
            try {
                sIn = new FileInputStream(selectedFile);
            } catch (FileNotFoundException e) {
                logger.warning(e.getMessage());
            }
            try {
                Preferences.importPreferences(sIn);
            } catch (IOException e) {
                logger.warning(e.getMessage());
            } catch (InvalidPreferencesFormatException e) {
                logger.warning(e.getMessage());
            }
        }

    }

    /**
     * save preferences to file
     */
    private void savePreferences() {
        OutputStream sOut = null;
        File selectedFile = null;
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Preferences File to save");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fc.getExtensionFilters().add(extFilter);
        fc.setSelectedExtensionFilter(extFilter);
        fc.setInitialDirectory(new File(homeDir));
        try {
            selectedFile = fc.showSaveDialog(myMain.getPrimaryStage());
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        if (selectedFile != null) {
            try {
                sOut = new FileOutputStream(selectedFile);
            } catch (FileNotFoundException e) {
                logger.warning(e.getMessage());
            }
            try {
                prefs.exportNode(sOut);
            } catch (IOException e) {
                logger.warning(e.getMessage());
            } catch (BackingStoreException e) {
                logger.warning(e.getMessage());
            }
        }
    }

    /**
     * enables/disables stepping
     *
     * @param bEnable  boolean switch true/false
     */
    public void enableStepUp(Boolean bEnable) {
        btnStepUp.setDisable(!bEnable);
    }

    /**
     * enables/disables action menu
     *
     * @param bCall boolean switch true/false
     */
    public void callForAction(Boolean bCall) {
        if (bCall) {
            mnuAction.setStyle("-fx-border-color:chocolate;");
            mnuAction.setDisable(false);
        } else {
            mnuAction.setStyle(null);
            mnuAction.setDisable(true);
        }
    }

    /**
     * Displays pdf file (for urGENOVA manual)
     *
     * @param _sName file path
     */
    private void displayResource(String _sName) {
        File docFile = myMain.showPDF(_sName);
        HostServices hostServices = myMain.getHostServices();
        hostServices.showDocument(docFile.toURI().toString());
        docFile.deleteOnExit();
    }

}