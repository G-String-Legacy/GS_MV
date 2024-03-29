package org.gsusers.gsmv.utilities;

import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * Utility for G_String Help
 * Reads 'help markup' file and converts it to javafx Text VBox
 *
 * @see <a href="https://github.com/G-String-Legacy/G_String/blob/main/workbench/GS_L/src/view/TextStack.java">view.TextStack</a>
 * @author ralph
 * @version %v..%
 *
 */
public class TextStack
{
	/**
	 * string array list of text lines
	 */
	private ArrayList<String> salLines = new ArrayList<>();

	/**
	 * pointer to Preferences API
	 */
	private Preferences prefs = null;

	/**
	 * pointer to org.gs_users.gs_lv.GS_Application Logger
	 */
	private gsLogger logger;

	/**
	 * constructor
	 *
	 * @param sLocation  file name of specific help file
	 * @param _prefs  pointer to Preferences API
	 * @param _logger  org.gs_users.gs_lv.GS_Application logger
	 */
	public TextStack(String sLocation, Preferences _prefs, gsLogger _logger)
	{
		prefs = _prefs;
		logger = _logger;
		try {
			readFile( sLocation );
		} catch (IOException e) {
			logger.log("TextStack", 48, "", e);
		}
	}

	/**
	 * read text file
	 *
	 * @param filename  name of input file
	 * @throws IOException I/O exception
	 */
	private void readFile(String filename) throws IOException {

		String sResource = "help/" + filename;
		InputStream stIn = this.getClass().getResourceAsStream(sResource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stIn));
        String line;
        while ((line = reader.readLine()) != null) {
            salLines.add(line);
        }
        reader.close();
	}

	/**
	 * constructs JavaFX element for display, and parses for markup code
	 *
	 * @return vBox scene
	 */
	public VBox vStack ()
	{
		String sTemp;
		String sOutput;
		String[] sOpts = {"title", "para", "bullet", "sub"};
		Text t;
		Image hand;
		InputStream stIn = this.getClass().getResourceAsStream("Pointer.png");
		hand = new Image(stIn, 30.0, 30.0, true, true);
		ImageView ivHand = new ImageView(hand);
		VBox vTemp = new VBox(10);
		vTemp.setMinWidth(600.0);
		vTemp.setPadding(new Insets(40.0, 40.0, 40.0, 40.0));
		vTemp.setStyle(prefs.get("Help Style","-fx-Background-color: Beige"));
		HBox wrapper = null;
		VBox bullet = null;
		bullet = new VBox();
		bullet.setPrefWidth(80.0);
		bullet.setAlignment(Pos.TOP_LEFT);
        for (String sLine: salLines)
        {
        	for (String s : sOpts)
        	{
        		if (sLine.indexOf(s) == 1)			//that's a hit
        		{
        			sTemp = "<" + s + ">";
        			sOutput = sLine.replaceAll(sTemp, "");
        			sTemp = "</" + s + ">";
        			sOutput = sOutput.replaceFirst(sTemp, "");
        			wrapper = new HBox();
        			wrapper.setPrefWidth(600.0);
        			switch (s)
        			{
	        			case "title":
	        				DropShadow ds = new DropShadow();
	        				ds.setOffsetY(3.0f);
	        				ds.setColor(Color.rgb(80, 40, 13));
	            			t = new Text(sOutput + "\n");
	            			t.setFont(Font.font ("sans-serif", FontWeight.BOLD, 32));
	            			t.setFill(Color.rgb(80, 40, 13));
	            			t.setEffect(ds);
	            			wrapper.setAlignment(Pos.CENTER);
	            			wrapper.getChildren().add(t);
	        				break;
	        			case "para":
	            			t = new Text(sOutput + "\n");
	            			t.setFont(Font.font ("sans-serif", 16));
	        				t.setWrappingWidth(650.0);
	            			t.setTextAlignment(TextAlignment.JUSTIFY);
	            			t.setFill(Color.rgb(80, 40, 13));
	            			wrapper.getChildren().add(t);
	        				break;
	        			case "bullet":
	            			t = new Text(sOutput + "\n");
	            			t.setFont(Font.font ("sans-serif", 16));
	        				bullet = new VBox();
	        				bullet.setPrefWidth(40.0);
	            			t.setFill(Color.rgb(80, 40, 13));
	        				ivHand = new ImageView(hand);
	        				bullet.getChildren().add(ivHand);
	        				t.setWrappingWidth(650.0);
	            			t.setTextAlignment(TextAlignment.JUSTIFY);
	            			wrapper.getChildren().addAll(bullet, t);
	        				break;
	        			case "sub":
	        				Integer iPointer = sOutput.indexOf('|');
	        				String ss1 = sOutput.substring(0,  iPointer);
	        				String ss2 = " " + sOutput.substring(iPointer + 1);
	        				Text t1 = new Text(ss1);
	            			t1.setFont(Font.font ("sans-serif", FontWeight.BOLD, 17));
	        				Text t2 = new Text(ss2);
	            			t2.setFont(Font.font ("sans-serif", 16));
	        				t1.setWrappingWidth(650.0);
	            			t1.setTextAlignment(TextAlignment.JUSTIFY);
	            			t1.setFill(Color.rgb(80, 40, 13));
	        				t2.setWrappingWidth(650.0);
	            			t2.setTextAlignment(TextAlignment.JUSTIFY);
	            			t2.setFill(Color.rgb(80, 40, 13));
	            			TextFlow tf = new TextFlow(t1, t2);
	            			wrapper.getChildren().add(tf);
	            			break;
	    				default:
	    					break;
        			}
            		vTemp.getChildren().add(wrapper);
            		break;
    			}
        	}
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vTemp);
        scrollPane.setFitToWidth(true);

        VBox vBox = new VBox();
        vBox.getChildren().add(scrollPane);
		return vBox;
	}
}
