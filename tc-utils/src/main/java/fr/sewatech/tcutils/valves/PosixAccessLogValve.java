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
