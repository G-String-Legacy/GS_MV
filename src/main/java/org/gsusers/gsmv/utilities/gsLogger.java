package org.gsusers.gsmv.utilities;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.*;
import java.util.prefs.Preferences;

/**
 * gsLogger supplements the basic logger to be useful in this
 * project with multiple packages, classes, and methods.
 * However, it is based on the basic java.util.logging.Logger.
 *
 * @author ralph
 * @version 2.0.13
 */

public class gsLogger {
    Logger bLogger;
    Level lDefault;
   public gsLogger(String _sName, Preferences _prefs, Level _lDefault) throws IOException {
       bLogger = Logger.getLogger(_sName);
       lDefault = _lDefault;
       String sWorkingDir = _prefs.get("Working Directory", System.getProperty("user.dir"));
       String sSep = File.separator;
       String sTarget = sWorkingDir + sSep + "gsLog_%g.log";
       FileHandler fhGS = new FileHandler(sTarget);
       fhGS.setFormatter(new SimpleFormatter());
       bLogger.addHandler(fhGS);
   }
   // explicit level, class, line number, message
   public void log(Level _l, String _sClass, Integer _iLine, String _sMessage){
        String sLocation = "[" + _sClass + ": " + _iLine.toString() + "] ";
        bLogger.log(_l, sLocation + _sMessage);
   }

   // explicit level, class, line number, message, stacktrace
   public void log(Level _l, String _sClass, Integer _iLine, String _sMessage, Exception _e){
       String sLocation = "[" + _sClass + ": " + _iLine.toString() + "] ";
       bLogger.log(_l, sLocation + _sMessage + "\n" + getStacktrace(_e));
   }

   // default level, class, line number, message
   public void log(String _sClass, Integer _iLine, String _sMessage){
       String sLocation = "[" + _sClass + ": " + _iLine.toString() + "] ";
       bLogger.log(Level.WARNING, sLocation + _sMessage);
   }

   // default level, class, line number, message, stacktrace
   public void log(String _sClass, Integer _iLine, String _sMessage, Exception _e){
       String sLocation = "[" + _sClass + ": " + _iLine.toString() + "] ";
       bLogger.log(lDefault, sLocation + _sMessage + "\n" + getStacktrace(_e));
   }

   private String getStacktrace(Exception e){
       CharArrayWriter cw = new CharArrayWriter();
       PrintWriter w = new PrintWriter(cw);
       e.printStackTrace(w);
       w.close();
       return cw.toString();
   }
}
