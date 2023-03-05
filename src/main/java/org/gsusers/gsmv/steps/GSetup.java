package org.gsusers.gsmv.steps;

import java.io.*;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.gsusers.gsmv.GS_Application;

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
	 * JavaFX stage for GUI
	 */
	private Stage myStage = null;

	/**
	 * pointer to <code>logger</code>
	 */
	private Logger logger;

	/**
	 * pointer to Preferences API
	 */
	private Preferences prefs;
	//private Long iBytes;

	private String sWorking ;

	/**
	 * constructor for <code>gSetup</code>.
	 *
	 * @param _sWorking  name of 'Working Directory'
	 * @param _stage  display screen
	 * @param _logger  pointer to org.gs_users.gs_lv.GS_Application logger
	 * @param _prefs  Preferences
	 */
	public GSetup(String _sWorking, Stage _stage, Logger _logger, Preferences _prefs)
	{
		sWorking = _sWorking;
		myStage = _stage;
		logger = _logger;
		prefs = _prefs;
		try {
			this.ask();
		} catch(Exception e) {
			logger.warning (e.getMessage());
		}
	}

	/**
	 * Initializes preferences
	 *
	 * @throws IOException input errors
	 */
	public void ask() throws IOException
	{
		/*DirectoryChooser dc = new DirectoryChooser();
		dc.setInitialDirectory(null);
		File fDir = new File(System.getProperty("user.home"));
		if (fDir.exists())
			dc.setInitialDirectory(fDir);
		dc.setTitle("Choose location and create new working directory");
		File dir = dc.showDialog(myStage);
		try
		{
			dir.mkdir();
		} catch (Exception e)
		{
			logger.warning(e.getMessage());
		}
		dir.setWritable(true, false);
		String sWork = dir.getPath();
		*/
		//prefs.put("Home Directory", System.getProperty("user.home"));
		String sOS_Full = System.getProperty("os.name");
		String sTargetName = null;
		String sResourceName = null;
		String sOS = null;
		if (sOS_Full.indexOf("Windows") >=0)
		{
			sOS = "Windows";
			sResourceName = "urGenova_W.exe";
			sTargetName = "urGenova.exe";
		}
		else if (sOS_Full.indexOf("Linux") >= 0)
		{
			sOS = "Linux";
			sResourceName = "urGenova_L";
			sTargetName = "urGenova";
		}
		else if (sOS_Full.indexOf("Mac") >= 0)
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
		String sWork =  getBrennan(sResourceName, sTargetName, logger);
		prefs.put("Working Directory", sWork);
		prefs.put("OS", sOS);
	}

	private String getBrennan(String _sResourceName, String _sTargetName, Logger logger) throws IOException {
		int len;
		byte[] b = new byte[1024];
		String sTarget;
		File fTarget;
		String sBrennan = sWorking;
		File fWorking = new File(sBrennan);
		if (fWorking.mkdir()){
			sTarget = sBrennan + File.separator + _sTargetName;
			fTarget = new File(sTarget);
			FileOutputStream out = new FileOutputStream(sTarget);
			InputStream is = this.getClass().getResourceAsStream(_sResourceName);
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
		return fWorking.getPath();
	}
}

