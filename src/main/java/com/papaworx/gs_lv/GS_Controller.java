package com.papaworx.gs_lv;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GS_Controller {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}