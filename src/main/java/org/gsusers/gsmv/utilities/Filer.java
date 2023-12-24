package org.gsusers.gsmv.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.prefs.Preferences;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.gsusers.gsmv.model.Facet;
import org.gsusers.gsmv.model.Nest;
import org.gsusers.gsmv.model.SampleSizeTree;

/**
 * Process reading and writing of control and data files the 'read' command
 * takes the following arguments: - file: name of the file to be read -
 * sModus: a control argument with the following values: 'Control': reads
 * and processes existing control file for 'doOver'. 'Data': reads, strips
 * and stores stripped data file (with previously established format
 * parameters). 'Scan': reads, scans and displays data to establish format
 * parameters and sample sizes (auto-index)
 *
 * @author ralph
 * @version %v..%
 */
public class Filer {
	/**
	 * pointer to <code>Nest</code>
	 */
	private final Nest myNest;

	/**
	 * file path of data file
	 */
	private final String sDataFileName;

	/**
	 * fixed field width in umber of characters
	 */
	private Integer iFieldWidth;

	/**
	 * upper limit of first data columns to be highlighted, to be ignored by urGENOVA
	 */
	private Integer iHilight = 0;

	/**
	 * maximal number of columns in score data display
	 */
	private Integer iMaxColumns = 0;

	/**
	 * pointer to Preferences API
	 */
	private final Preferences prefs;

	/**
	 * integer counter to count three horizontal bars, when parsing urGENOVA output
	 */
	private Integer iOutputPointer = 0;

	/**
	 * 2 dim array of raw score data fields, organized by rows and columns
	 */
	private String[][] sRawData = null;

	/**
	 * arbitrary starting point for searching minimum value
	 */
	private Double dMin = 100.0;

	/**
	 * arbitrary starting point for searching maximal value
	 */
	private Double dMax = -100.0;

	/**
	 * pointer to <code>gsLogger</code>
	 */
	private final gsLogger logger;

	/**
	 * pointer to GUI window
	 */
	private final Stage myStage;

	/**
	 * constructor
	 *
	 * @param _nest  <code>Nest</code>
	 * @param _prefs  <code>Preferences</code>
	 * @param _logger  pointer to application gsLogger
	 * @param _stage  <code>Stage</code>
	 */
	public Filer(Nest _nest, Preferences _prefs, gsLogger _logger, Stage _stage) {
		/*
		 * tentative limit for number of data-in columns
		 */
		myNest = _nest;
		prefs = _prefs;
		myStage = _stage;
		iFieldWidth = 8;
		sDataFileName = myNest.getDataFileName();
		//dSums = new Double[iMaxColCount];
		logger = _logger;
	}

	/**
	 * reads  script file line by line, to be processed by <code>processControlLine</code>
	 *
	 * @param file  file pointer
	 */
	public void readFile(File file) {
		// reads already existing control file
		String sLine;
			myNest.setFileName();

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				sLine = scanner.nextLine();
				processControlLine(sLine); // process control file
			}
		} catch (IOException e) {
			logger.log("Filer", 168, "", e);
		}
	}

	/**
	 * script parser
	 * @param line  of text
	 */
	private void processControlLine(String line) {
		try {
			String[] words = line.split("\\s+", 100);
			String key = words[0];
			char cFacet;
			Integer length = words.length;
			String value = join(words, length);
			switch (key) {
				case "GSTUDY" -> myNest.setTitle(value);
				case "COMMENT&", "COMMENT*", "COMMENT%" -> {
					Facet nullFacet = new Facet(myNest);
					words = value.split("\\s+");
					nullFacet.setName(words[0]);
					cFacet = words[1].toCharArray()[1];
					nullFacet.setDesignation(cFacet);
					nullFacet.setNested(false);
					myNest.addFacet(nullFacet);
				}
				case "COMMENT" -> myNest.addComment(value);
				case "OPTIONS" -> prefs.put("OPTIONS", value);
				case "EFFECT" -> myNest.addEffect(value);
				case "FORMAT" -> myNest.addFormat();
				case "PROCESS" -> myNest.addProcess();
				case "REPLICATE" -> myNest.set_cRep(value.toCharArray()[0]);
				case "ANCHORS" -> {
					if (myNest.getSimulate())        // if in 'Simulate', value gets to variances
						myNest.addAnchors(value);
					else
						myNest.addComment(value);   // else to comments (in 'Analysis'
				}
				case "VARIANCES" -> {
					if (myNest.getSimulate())        // if in 'Simulate', value gets to variances
						myNest.addVariances(value);
					else
						myNest.addComment(value);   // else to comments (in 'Analysis'
				}
				default -> {
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * reverse of <code>split</code>, joins words from array, drops first word
	 *
	 * @param words array of strings
	 * @param length  number of strings
	 * @return assembled string
	 */
	private String join(String[] words, Integer length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < length; i++)
			sb.append(" ").append(words[i]);
		String result = (sb.toString());
		return result.trim();
	}

	/**
	 * highlights columns <code>iHighlight</code>
	 *
	 * @param _sColumns array of Strings for a row at a time, raw score data
	 * @return sbLine.toString  line of formatted text
	 */
	private String HTML_join(String[] _sColumns) {
		StringBuilder sbLine = new StringBuilder("<tr>");
		int iCount = 0;
		for (String sTemp : _sColumns) {
			if (iCount++ >= iHilight)
				sbLine.append("<td align=\"right\"><strong>").append(sTemp).append("</strong></td>");
			else
				sbLine.append("<td align=\"right\"><font color = \"#CCCCCC\">").append(sTemp).append("</td>");
		}
		sbLine.append("</tr>\n");
		return sbLine.toString();
	}

	/**
	 * This method displays the experimental scores, as read in from the data
	 * file. It allows to exclude index columns from being fed to urGenova.
	 * In contrast to all the other display scenes ('groups'), this one
	 * is not pure javafx, but it displays the data as html within a webview.
	 * It is more efficient than having a factory generate a javafx object for each score.
	 *
	 * @return HTML formatted text of score data via <code>WebView</code>
	 */
	public Group showTableNew() {
		StringBuilder sb = new StringBuilder("<html><body contentEditable=\"true\"><table border = \"1\">\n");
		int iCounter = 0;
		sb.append("<col width=\"50\">\n".repeat(Math.max(0, iMaxColumns)));
		for (String[] sRow : sRawData) {
			if (iCounter++ > 50)
				break;
			try {
				sb.append(HTML_join(sRow));
			} catch (Exception e) {
				logger.log("Filer", 272, "", e);
			}
		}
		sb.append("</table></body></html>");
		/*
		 * repository for HTML data section
		 */
		String sHTML = sb.toString(); // content of browser
		Group tableGroup = new Group(); // overall container
		VBox mainLayout = new VBox(0.0); // overall layout
		mainLayout.setAlignment(Pos.TOP_CENTER);
		HBox titleBox = new HBox(); // container for title
		titleBox.setPrefWidth(800);
		titleBox.setStyle(prefs.get("Format_20", null));
		titleBox.setAlignment(Pos.CENTER);
		HBox parameterBox = new HBox(); // container for spinner etc
		parameterBox.setPrefHeight(70.0);
		parameterBox.setAlignment(Pos.CENTER);
		HBox browserBox = new HBox(); // container for browser
		browserBox.setStyle(
				"-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: brown;");
		browserBox.setAlignment(Pos.TOP_CENTER);
		Label lbTitle = new Label("Select skip and field format");
		lbTitle.setStyle(
				"-fx-font-size: 20px; -fx-font-family: \"ARIAL\"; -fx-padding: 10; -fx-background-color: #801515; -fx-text-fill: #FFFFFF;");
		lbTitle.setPrefWidth(800.0);
		lbTitle.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(lbTitle);
		mainLayout.getChildren().add(titleBox);
		Label lbSkip = new Label("Skip: ");
		lbSkip.setPadding(new Insets(0, 0, 0, 10));
		Label lbWidth = new Label("     Width: ");
		Spinner<Integer> intSpinner = new Spinner<>(0, iMaxColumns - 1, 0, 1);
		intSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
		intSpinner.setPrefWidth(80.0);
		intSpinner.getValueFactory().setValue(iHilight);
		intSpinner.getEditor().textProperty().addListener((obs1, oldValue1, newValue1) -> {
			if (!oldValue1.equals(newValue1)) {
				iHilight = Integer.parseInt(newValue1);
				myNest.show(this.showTableNew());
				myNest.setHighlight(iHilight);
			}
		});
		TextField format = new TextField();
		format.setPrefWidth(55);
		format.setText(iFieldWidth.toString());
		format.textProperty().addListener((obs2, oldValue2, newValue2) -> {
			if (!Objects.equals(newValue2, oldValue2))
				iFieldWidth = Integer.parseInt(newValue2);
		});
		parameterBox.getChildren().addAll(lbSkip, intSpinner, lbWidth, format);
		mainLayout.getChildren().add(parameterBox);
		// setup of browser

		WebView browser = new WebView();
		browser.setMaxWidth(iMaxColumns * 70);
		browser.setMaxHeight(400.0);
		/*
		 * JavaFX  <code>WebEngine</code>
		 */
		WebEngine webEngine = browser.getEngine();
		webEngine.loadContent(sHTML);
		browserBox.getChildren().add(browser);
		mainLayout.getChildren().add(browserBox);
		tableGroup.getChildren().add(mainLayout);
		return tableGroup;
	}

	/**
	 * 		 * Reads and parses datafile, stores possible header in 'headers',
	 * stores data in 'sRawData' structure', i.e. an array of arrays
	 * String[][] in several passes: Pass 1: counts valid lines and counts
	 * lines with Double content Pass 2: creates outer array with
	 * appropriate number of lines, then chops lines, creates inner arrays
	 * and stores Double numbers Also, initialize summing for means and
	 * counting for N's
	 *
	 * @param _inFile score data file
	 */
	public void readDataFileNew(File _inFile) {
		int iLineCount = 0;
		//int iFalseLineCount = 0;
		int iNumberFields;
		int iLineIndex = 0;
		String[] sChoppedLine;
		String sLine;
		// check if file exists
		if (_inFile == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Data file missing in readDataFile.");
			alert.showAndWait();
			System.exit(0);
		}
		// Pass 1:
		try (Scanner scanner = new Scanner(_inFile)) {
			while (scanner.hasNextLine()) {
				sLine = scanner.nextLine();
				if (sLine != null && !sLine.replaceAll("\\s+", "").equals("") && !sLine.matches("^[a-zA-Z]*$"))
					iLineCount++;
			}
		} catch (IOException e) {
			logger.log("Filer", 373, "", e);
			System.exit(0);
		}
		// Pass 2:
		sRawData = new String[iLineCount][];
		try (Scanner scanner = new Scanner(_inFile)) {
			while (scanner.hasNextLine()) {
				sLine = scanner.nextLine();
				if (sLine != null && !sLine.replaceAll("\\s+", "").equals("") && !sLine.matches("^[a-zA-Z]*$")) {
					if (sLine.indexOf(',') >= 0) {
						// split on comma
						sChoppedLine = sLine.split(",");
					} else if (sLine.contains("\t")) {
						// split on tab
						sChoppedLine = sLine.split("\t");
					} else {
						// split on whitespace	// not recommended if there are missing data
						sChoppedLine = sLine.split("\\s+");
					}
					iNumberFields = sChoppedLine.length;
					sRawData[iLineIndex++] = sChoppedLine;

					if (iNumberFields > iMaxColumns)
						iMaxColumns = iNumberFields;
				}
			}
		} catch (IOException e) {
			logger.log("Filer", 400, "", e);
			System.exit(0);
		}
	}

	/**
	 * writes stripped datafile to a special file (~data.txt) in the special
	 * Brennan directory (home of urGenova) for running urGenova on.
	 */
	public void writeDataFileNew() {
		StringBuilder sb;
		double sum = 0.0;
		double dItem;
		Double dValue;
		int DataCount = 0;
		int iHeadCount;
		
		myNest.setHighlight(iHilight);
		for (String[] sRow1 : sRawData) {
			for (int j = iHilight; j < sRow1.length; j++) {
				if (!sRow1[j].trim().isEmpty()) {
					dItem = Double.parseDouble(sRow1[j]);
					sum += dItem;
					if (dItem < dMin)
						dMin = dItem;
					else if (dItem > dMax)
						dMax = dItem;
					DataCount++;
				} else {
					sRow1[j] = "x";
					myNest.incrementMissing();
				}
			}
		}
		/*
		 * Grand mean of score data
		 */
		double dGrandMeans = sum / DataCount;
		myNest.setGrandMeans(dGrandMeans);

		String sDataPath = prefs.get("Working Directory", null) + File.separator + sDataFileName;
		File fData = new File(sDataPath);
		PrintStream ps = null;
		try {
			ps = new PrintStream(fData);
		} catch (IOException e) {
			logger.log("Filer", 446, "", e);
		}
	    new DecimalFormat("####.##");
	    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		for (String[] dRow : sRawData) {
			sb = new StringBuilder();
			iHeadCount = iHilight;
			for (String s : dRow) {
				if (iHeadCount-- > 0)
					sb.append(padLeft(s, iFieldWidth));
				else if (!s.equals("x")) {
					dValue = Double.parseDouble(s) - dGrandMeans;
					sb.append(padLeft(df.format(dValue), iFieldWidth));
				} else
					sb.append(padLeft(df.format(0.0), iFieldWidth));
			}
			assert ps != null;
			ps.println(sb);
		}
		assert ps != null;
		ps.close();
		testSignature();
	}

	/**
	 * test synthetic data file in signal noise, so synthetic data files can
	 * be distinguished from empiric data. Synthetic data show an analytic
	 * sig LessThan 10.
	 *
	 * @return String  Signature ~
	 */
	public String testSignature() {
		double dItem;

		Lehmer Signer = new Lehmer(dMin, dMax);
		for (String[] sRow1 : sRawData) {
			for (int j = iHilight; j < sRow1.length; j++) {
				if (!sRow1[j].equals("x")) {
					dItem = Double.parseDouble(sRow1[j]);
					Signer.Test(dItem);
				}
			}
		}
		return "\nSig: " + Signer.Summarize();
	}

	/**
	 * format utility  pads String <code>s</code> to <code>n</code> characters with blanks
	 *
	 * @param s  String to be padded
	 * @param n  int number of padding blanks
	 * @return padded String
	 */
	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

	/**
	 * reads and parses the output string of urGENOVA
	 */
	public void getUrGenova() {
		/*
		 * Reads in the results of urGenova and stores them in n
		 * 'VarianceComponent array (in Nest)
		 */
		String sLine;
		String sUrOutput = prefs.get("Working Directory", null) + "/~control.txt.lis";
		File file = new File(sUrOutput);
		iOutputPointer = 0;
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				sLine = scanner.nextLine();
				processResultlLine(sLine); // process control file
			}
		} catch (IOException e) {
			logger.log("Filer", 521, "", e);
			e.printStackTrace();
		}
	}

	/**
	 * extracts variance components from urGENOVA output lines and stores them in <code>Nest</code>
	 *
	 * @param _line  line from urGENOVA output
	 */
	private void processResultlLine(String _line) {
		switch (iOutputPointer) {
			case 0 -> {
				if (_line.contains("--------------------------------"))
					iOutputPointer = 1;
			}
			case 1 -> {
				if (_line.contains("--------------------------------"))
					iOutputPointer = 2;
			}
			case 2 -> {
				if (_line.contains("--------------------------------")) {
					iOutputPointer = 3;
					break;
				}
				// Now we can read the variance components
				myNest.setVariance(_line);
			}
			default -> {
			}
		}
	}

	/**
	 * getter of <code>File</code>
	 *
	 * @param bRead  boolean flag read/write - true/false
	 * @param sTitle  String, title to be displayed in <code>FileChooser</code>
	 * @return File
	 */
	public File getFile(Boolean bRead, String sTitle) {
		String sInitial = prefs.get("Home Directory", System.getProperty("user.home"));
		File fInitial = new File(sInitial);
		File f;
		String sHome;
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(fInitial);
		fc.setTitle(sTitle);
		if (bRead)
			f = fc.showOpenDialog(myStage);
		else
			f = fc.showSaveDialog(myStage);
		sHome = f.getParent();

		if (!sHome.equals(sInitial))
			prefs.put("Home Directory", sHome);
		return f;
	}

	/**
	 * opens <code>Alert</code> dialog to solicit location for new script file
	 * to be saved to.
	 *
	 * @param sType  'Analysis'/'Synthesis'
	 * @param sQuestion  text of question to be displayed
	 */
	public void saveParametersDialog(String sType, String sQuestion) {
		// sType either 'Analysis' or 'Synthesis'
		Alert alert = new Alert(AlertType.CONFIRMATION);
		double dX = myStage.getX() + 200.0;
		double dY = myStage.getY() + 75.0;
		File fOutputFile;
		alert.setX(dX);
		alert.setY(dY);
		alert.initOwner(myStage);
		alert.setTitle("Decide!");
		alert.setHeaderText(null);
		alert.setContentText(sQuestion);
		DialogPane dialogPane = alert.getDialogPane();
		ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
		buttonBar.getButtons().forEach(b -> b.setStyle(
				"-fx-font-size: 16;-fx-background-color: #551200;-fx-text-fill: #ffffff;-fx-font-weight: bold;"));
		if (alert.showAndWait().get() != ButtonType.OK)
			return;
		switch (sType) {
			case "Analysis" -> {
				fOutputFile = getFile(false, "Save Analysis Control File to:");
				writeAnalysisControlFile(fOutputFile, false);
			}
			case "Synthesis" -> {
				fOutputFile = getFile(false, "Save Synthesis Control File to:");
				writeSynthesisControlFile(fOutputFile);
			}
		}
	}

	/**
	 * writes new analysis control file
	 *
	 * @param file pointer to <code>File</code>
	 * @param bBrennan  flag to limit output to urGENOVA conformity
	 */
	public void writeAnalysisControlFile(File file, Boolean bBrennan) {
		String sTemp;
		SampleSizeTree myTree = myNest.getTree();
		PrintStream writer = null;
		try {
			writer = new PrintStream(file);
		} catch (FileNotFoundException e) {
			logger.log("Filer", 651, "", e);
		}
		// Title
		assert writer != null;
		writer.println("GSTUDY    " + myNest.getTitle());
		// Comments
		for (String s : myNest.getComments()) {
			writer.println("COMMENT   " + s);
		}
		// Comments*
		for (Facet f : myNest.getFacets()) {
			writer.println("COMMENT*  " + f.getName() + "    (" + f.getDesignationString() + ")");
		}

		char[] cFacet = myNest.getDictionary().toCharArray();
		int iLevels = cFacet.length;
		// Options
		writer.println("OPTIONS   " + prefs.get("OPTIONS", "NREC 5 \"*.lis\" TIME NOBANNER"));
		
		// Replications
		if (myNest.getReplicate() && !bBrennan)
			writer.println("REPLICATE   " + myNest.get_cRep());
		
		// Effects
		for (int i = 0; i < iLevels; i++) {
			sTemp = myTree.getEffect(i);
			writer.println(sTemp);
		}
		writer.println("FORMAT       " + iFieldWidth * iHilight + "  " + iFieldWidth);
		// Process
		writer.println("PROCESS      \"" + sDataFileName + "\"");
		writer.close();
	}

	/**
	 * writes new synthesis control file
	 *
	 * @param fOutput  pointer to <code>File</code>
	 */
	public void writeSynthesisControlFile(File fOutput) {
		// writes new control file
		String sTemp;
		Integer iNumberVarianceCoeffients;
		SampleSizeTree myTree = myNest.getTree();
		PrintStream writer = null;
		try {
			writer = new PrintStream(fOutput);
		} catch (FileNotFoundException e) {
			logger.log("Filer", 699, "", e);
		}

		// Title
		assert writer != null;
		writer.println("GSTUDY    " + myNest.getTitle());

		// Comments
		for (String s : myNest.getComments()) {
			writer.println("COMMENT   " + s);
		}

		// Facets
		for (Facet f : myNest.getFacets()) {
			writer.println("COMMENT* " + f.getName() + "    (" + f.getDesignationString() + ")");
		}
		char[] cFacet = myNest.getDictionary().toCharArray();
		//rep
		String sp = "    ";
		char cReplicate = myNest.get_cRep();
		if (cReplicate != '-')
			writer.println("REPLICATE" + sp + cReplicate);
		// Effects
		int iLevels = cFacet.length;
		for (int i = 0; i < iLevels; i++) {
			sTemp = myTree.getEffect(i);
			writer.println(sTemp);
		}
		// Anchors
		StringBuilder sb = new StringBuilder("ANCHORS");
		sb.append(sp).append(myNest.getFloor());
		sb.append(sp).append(myNest.getMean());
		sb.append(sp).append(myNest.getCeiling());
		if ( cReplicate != '-')
			sb.append(sp).append(myNest.get_iMinRep()).append(sp).append(myNest.getRepRange());
		
		writer.println(sb);

		// Variances
		iNumberVarianceCoeffients = myNest.getVcDim();
		sb = new StringBuilder("VARIANCES");
		for (int i = 0; i < iNumberVarianceCoeffients; i++)
			sb.append("    ").append(myNest.getVarianceCoefficient(i));
		writer.println(sb);
		writer.close();
	}
	
	/**
	 * returns raw data that has been read in previously for use
	 * in replication analysis.
	 * 
	 * @return sRawData  2 dimensional array of strings
	 */
	public String[][] getRawData(){
		return sRawData;
	}

}
