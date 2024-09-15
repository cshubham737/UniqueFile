package utility.com;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class Differance {

    public static void main(String[] args) {
        Path folder1 = Paths.get("C:\\Users\\ShubhamChaturvedi\\Downloads\\ResumeFromLatest"); // Folder 1
        Path folder2 = Paths.get("C:\\Users\\ShubhamChaturvedi\\Downloads\\ResumeUpload"); // Folder 2
        Path outputFolder = Paths.get("C:\\\\Users\\\\ShubhamChaturvedi\\\\Downloads\\\\Result"); // Folder to save the difference

        try {
            findAndCopyDifference(folder1, folder2, outputFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void findAndCopyDifference(Path folder1, Path folder2, Path outputFolder) throws IOException {
        // Create the output directory if it doesn't exist
        if (!Files.exists(outputFolder)) {
            Files.createDirectories(outputFolder);
        }

        // Collect all file paths in folder1 and folder2
        Set<String> folder1Files = collectFilePaths(folder1);
        Set<String> folder2Files = collectFilePaths(folder2);

        // Find the difference: files in folder1 but not in folder2
        Set<String> difference = new HashSet<>(folder1Files);
        difference.removeAll(folder2Files);

        // Copy the different files to the output folder
        for (String relativePath : difference) {
            Path sourceFile = folder1.resolve(relativePath);
            Path targetFile = outputFolder.resolve(relativePath);

            // Ensure the parent directories are created
            if (!Files.exists(targetFile.getParent())) {
                Files.createDirectories(targetFile.getParent());
            }

            Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied: " + sourceFile + " to " + targetFile);
        }

        System.out.println("Difference files copied successfully.");
    }

    public static Set<String> collectFilePaths(Path folder) throws IOException {
        Set<String> filePaths = new HashSet<>();
        
        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // Collect relative paths
                filePaths.add(folder.relativize(file).toString());
                return FileVisitResult.CONTINUE;
            }
        });

        return filePaths;
    }
}
