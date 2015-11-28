/**
 * Copyright 2015 Sewatech
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
package fr.sewatech.tcutils.commons;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributeView;

/**
 * @author Alexis Hassler
 */
public class PosixFiles {
    public static void changeGroup(File file, String groupName) throws IOException {
        changeGroup(file.toPath(), groupName);
    }

    public static void changeGroup(Path path, String groupName) throws IOException {
        FileSystem fileSystem = FileSystems.getDefault();
        PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(path, PosixFileAttributeView.class);

        if (!fileSystem.provider().getFileStore(path).supportsFileAttributeView(fileAttributeView.name())) {
            throw new UnsupportedOperationException("Not on posix");
        }

        fileAttributeView.setGroup(fileSystem.getUserPrincipalLookupService().lookupPrincipalByGroupName(groupName));
    }

}
