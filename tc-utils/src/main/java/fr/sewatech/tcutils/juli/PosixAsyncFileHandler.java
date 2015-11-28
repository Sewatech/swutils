package fr.sewatech.tcutils.juli;

import fr.sewatech.tcutils.commons.PosixFiles;
import org.apache.juli.AsyncFileHandler;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.ErrorManager;
import java.util.logging.LogManager;

/**
 * @author Alexis Hassler
 */
public class PosixAsyncFileHandler extends AsyncFileHandler {

    @Override
    protected void openWriter() {
        super.openWriter();
        applyPosixGroup();
    }

    private void applyPosixGroup() {
        try {
            String className = this.getClass().getName();
            String group = LogManager.getLogManager().getProperty(className + ".group");
            if (group != null) {
                File logFile = guessLogFile();
                PosixFiles.changeGroup(logFile, group);
            }
        } catch (IOException e) {
            reportError(null, e, ErrorManager.OPEN_FAILURE);
        }
    }

    private File guessLogFile() {
        String className = this.getClass().getName();
        File directory = new File(getProperty(className + ".directory", "logs"));;
        String prefix = getProperty(className + ".prefix", "juli.");
        boolean rotatable = Boolean.parseBoolean(getProperty(className + ".rotatable", "true"));
        String suffix = getProperty(className + ".suffix", ".log");

        return new File(directory.getAbsoluteFile(), prefix + buildDate(rotatable) + suffix);
    }

    private String getProperty(String name, String defaultValue) {
        String value = LogManager.getLogManager().getProperty(name);
        if (value == null) {
            value = defaultValue;
        } else {
            value = value.trim();
        }
        return value;
    }

    private String buildDate(boolean rotatable) {
        String date;
        if (rotatable) {
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String tsString = ts.toString().substring(0, 19);
            date = tsString.substring(0, 10);
        } else {
            date = "";
        }
        return date;
    }

}
// I know, there's a lot of duplicated code with FileHandler... No idea how to avoid it, everything is private in
// FileHandler