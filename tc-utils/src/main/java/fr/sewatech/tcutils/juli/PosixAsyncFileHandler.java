/**
 * Copyright 2016 Sewatech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        File directory = new File(getProperty(className + ".directory", "logs"));
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