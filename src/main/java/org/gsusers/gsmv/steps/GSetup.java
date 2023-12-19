package org.gsusers.gsmv.steps;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Provides the screen for program setup
 *
 * @see <a href="https://github.com/G-String-Legacy/G_String/blob/main/workbench/GS_L/src/steps/gSetup.java">steps.gSetup</a>
 * @author ralph
 * @version %v..%
 */
public class GSetup
{

	/**
	 * pointer to Preferences API
	 */
	private final Preferences prefs;
	//private Long iBytes;

	private final String sWorking ;
	private String sTargetName = null;
	private String sResourceName = null;


	/**
	 * constructor for <code>gSetup</code>.
	 * GSetup tests if working directory for Brennan's urGenova exists,
	 * and creates it, if not. It populates it with the appropriate
	 * version of urGenova, according the the actual operating system.
	 *   In earlier versions of G_String users had had to create a working directory anywhere,
	 *   and with any name. Now it happens automatically in the user's home directory,
	 *   and it is named "G_String_Working_Directory".
	 *
	 * @param _logger  pointer to org.gs_users.gs_lv.GS_Application logger
	 * @param _prefs  Preferences
	 */
	public GSetup(Logger _logger, Preferences _prefs) throws IOException {
		String sHome = System.getProperty("user.home");
		String fileSeparator = File.separator;
		sWorking = sHome + fileSeparator + "G_String_Working_Directory";
		prefs = _prefs;
		try {
			this.ask();
		} catch(Exception e) {
			_logger.warning (e.getMessage());
		}
		File fTemp = new File(sWorking);
		if (!fTemp.exists())
			getBrennan(sWorking);
	}

	/**
	 * Initializes preferences
	 *
	 */
	public void ask() {
		String sOS_Full = System.getProperty("os.name");
		String sOS = null;
		if (sOS_Full.contains("Windows"))
		{
			sOS = "Windows";
			sResourceName = "urGenova_W.exe";
			sTargetName = "urGenova.exe";
		}
		else if (sOS_Full.contains("Linux"))
		{
			sOS = "Linux";
			sResourceName = "urGenova_L";
			sTargetName = "urGenova";
		}
		else if (sOS_Full.contains("Mac"))
		{
			sOS = "Mac";
			sResourceName = "urGenova_M";
			sTargetName = "urGenova";
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Operating system " + sOS + " not recognized.");
			alert.showAndWait();
			System.exit(1);
		}
		prefs.put("Working Directory", sWorking);
		prefs.put("OS", sOS);
	}

	public void getBrennan(String sWorking) throws IOException {
		int len;
		byte[] b = new byte[1024];
		String sTarget;
		File fTarget;
		File fWorking = new File(sWorking);
		if (fWorking.mkdir()){
			sTarget = sWorking + File.separator + sTargetName;
			fTarget = new File(sTarget);
			FileOutputStream out = new FileOutputStream(sTarget);
			InputStream is = this.getClass().getResourceAsStream(sResourceName);
			if (is == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setContentText("urGenova Input stream null.");
				alert.showAndWait();
			}
			while((len = is.read(b, 0, 1024)) > 0){
				out.write(b, 0, len);
			}
			is.close();
			out.close();
		fTarget.setExecutable(true, false);
		}
	}
}

