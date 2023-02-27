package org.gsusers.gsmv;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.gsusers.gsmv.utilities.About;

import java.io.File;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

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
    private GS_Application myMain;

    /**
     * org.gs_users.gs_lv.GS_Application logger
     */
    private Logger logger;

    /**
     * primary container of FXML object (GS_view)
     */
    public BorderPane bpScreen;

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
    @FXML MenuItem mnuChangePreferences;
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

        mnuExit.setOnAction((event) -> respond());
        btnStepUp.setOnAction((event) -> myMain.stepUp());

        mnuActionStartOver.setOnAction((event) -> myMain.startOver());
        mnuCHelp.setOnAction((event) -> myMain.helpSwitch("help"));
        mnuIntro.setOnAction((event) -> myMain.helpSwitch("intro"));
        mnuUHelp.setOnAction((event) -> displayResource("urGENOVA_manual.pdf"));
        mnuSimulate.setOnAction((event) -> myMain.Simulate());
        mnuResimulate.setOnAction((event) -> myMain.Resimulate());
        mnuChangePreferences.setOnAction((event) -> myMain.switchChangePreferences(true));
        mnuAbout.setOnAction((event) -> about());
        mnuAboutB.setOnAction((event) -> aboutB());
        mnuStart.setOnAction((event) -> myMain.freshStart());
        mnuSaveAll.setOnAction((event) -> myMain.saveAll());
        mnuReplicate.setOnAction((event) -> myMain.replicate());
        mnuReReplicate.setOnAction((event) -> myMain.replicateAgain());
        lblStep.setAlignment(Pos.CENTER);
        lblStep.setText("Step 0");
        mnuActionFresh.setOnAction((event) -> myMain.startFresh());

        callForAction(true);
    }

    /**
     * method to set class variables from GS_Application
     * @param _main pointer to GS_Application
     * @param _logger pointer to system logger
     */
    void setMainApp(GS_Application _main, Logger _logger){
        myMain = _main;
        logger = _logger;
        /**
         * org.gs_users.gs_lv.GS_Application preferences
         */
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
     * displaye 'About G_String' info
     */
    private void about() {
        About myAbout = new About(myMain.getPrimaryStage(), logger, "About.txt", "About G_String_L");
        myAbout.show();
    }

    /**
     * display 'About Brennan' info
     */
    private void aboutB() {
        About myAbout = new About(myMain.getPrimaryStage(), logger, "AboutB.txt", "About urGenova");
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