package fr.sewatech.tcutils.valves;

import fr.sewatech.tcutils.commons.PosixFiles;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.io.IOException;

/**
 * @author Alexis Hassler
 */
public class PosixAccessLogValve extends AccessLogValve {

    private static final Log log = LogFactory.getLog(PosixAccessLogValve.class);

    private String posixGroupName;

    protected synchronized void open() {
        super.open();
        if ( (currentLogFile != null) && (posixGroupName != null) ) {
            try {
                PosixFiles.changeGroup(currentLogFile, posixGroupName);
            } catch (IOException e) {
                log.warn("Cannot change group of access log file.");
            }
        }
    }

    public void setPosixGroupName(String posixGroupName) {
        this.posixGroupName = posixGroupName;
    }
}
