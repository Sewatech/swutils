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
