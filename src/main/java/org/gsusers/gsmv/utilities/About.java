package org.gsusers.gsmv.utilities;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.prefs.Preferences;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.gsusers.gsmv.GS_Application;

/**
 * Class 'About': formats the two auxiliary Help screens.
 */
public class About {

	/**
	 * array list of text lines
	 */
	private ArrayList<String> salItems = new ArrayList<>();

	/**
	 * GUI window
	 */
	private final Stage myStage;

	/**
	 * title of Alert window
	 */
	private final String sTitle;

	private String sLogFileName = null;

	private final Boolean bLOGS;

	private final GS_Application myMain;

	private final gsLogger logger;
	/**
	 * Constructor
	 *
	 * @param _main  pointer to GS_Application
	 * @param _logger  pointer to org.gs_users.gs_lv.GS_Application logger
	 * @param _bLogs  boolean switch between log-, and resource-file display
	 * @param _sFileName  path to 'About file
	 * @param _sTitle  dialog title
	 */
	public About(GS_Application _main, gsLogger _logger, Boolean _bLogs, String _sFileName, String _sTitle) {
		//constructor
		bLOGS = _bLogs;
		sTitle = _sTitle;
		myStage = _main.getPrimaryStage();
		InputStream stIn = null;
		Preferences prefs = _main.getPrefs();
		myMain = _main;
		logger = _logger;

		if (bLOGS){
			String sWorking = prefs.get("Working Directory", System.getProperty("user.home"));
			File fInitial = new File(sWorking);
			File f;
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(fInitial);
			// Set extension filter
			// Set extension filter
			FileChooser.ExtensionFilter extFilter =
					new FileChooser.ExtensionFilter("log files (*.log)", "*.log*");
			fc.getExtensionFilters().add(extFilter);
			fc.setTitle("GS WORKING Directory");
			f = fc.showOpenDialog(myStage);
			sLogFileName = f.getName();
			if (f.length() == 0){
				salItems = null;
				return;
			}
			try {
				stIn = new FileInputStream(f);
			} catch (Exception e) {
				_logger.log("About", 72, "", e);
			}
		} else
			stIn = this.getClass().getResourceAsStream(_sFileName);

		BufferedReader reader = null;
		if (stIn != null) {
			reader = new BufferedReader(new InputStreamReader(stIn));
		}
		String line;
        try {
			while (true) {
				assert reader != null;
				if ((line = reader.readLine()) == null) break;
				salItems.add(line);
			}
		} catch (IOException e) {
			_logger.log("About", 68, "", e);
		}
        try {
			reader.close();
		} catch (IOException e) {
			_logger.log("About", 73, "", e);
		}
	}

	/**
	 * operates display
	 */
	public void show()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(myStage);
		double dX = myStage.getX() + 200.0;
		double dY = myStage.getY() + 75.0;
		alert.setX(dX);
		alert.setY(dY);
		alert.setWidth(250.0);
		if (bLOGS)
			alert.setTitle(sLogFileName);
		else alert.setTitle(sTitle);
		alert.setHeaderText(null);
		alert.setGraphic(null);
		GridPane Content = new GridPane();
		Content.setPrefWidth(200.00);
		Content.setHgap(20.0);
		Content.setVgap(5.0);
		int iCount = 0;
		String[] sItems;
		Text t1, t2;
		if (salItems == null){
			alert.show();
			return;
		}

		for (String s:salItems) {
			sItems = s.split("=");
			if (sItems.length == 2) {
				t1 = new Text(sItems[0] + ":");
				t1.setFont(Font.font("Serif", 16));
				t1.setFill(Color.rgb(80, 40, 13));
				t2 = new Text(sItems[1]);
				t2.setFont(Font.font("Serif", 16));
				t2.setFill(Color.rgb(80, 40, 13));
				Content.add(t1, 0, iCount);
				Content.add(t2, 1, iCount);
			} else {
				t1 = new Text(sItems[0]);
				t1.setFont(Font.font("Serif", 16));
				t1.setFill(Color.rgb(80, 40, 13));
				Content.add(t1, 0, iCount);
			}
			iCount++;
		}
		alert.setResizable(true);
		DialogPane dp = alert.getDialogPane();
		try {
			dp.getStylesheets().add(Objects.requireNonNull(myMain.getClass().getResource("myDialog.css")).toExternalForm());
		} catch (Exception e) {
			logger.log("About", 166, "", e);
		}
		dp.getStyleClass().add("myDialog");
		dp.setContent(Content);
		ButtonBar buttonBar = (ButtonBar)dp.lookup(".button-bar");
		buttonBar.getButtons().forEach(b->b.setStyle("-fx-font-size: 16;-fx-background-color: #551200;-fx-text-fill: #ffffff;-fx-font-weight: bold;"));
		alert.showAndWait();
	}


}
