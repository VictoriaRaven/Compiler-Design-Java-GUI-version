import javax.swing.*;
import java.io.*;

public class FileExplorerGUI_main {

    private LexicalAnalyzer lexicalAnalyzer;

    public FileExplorerGUI_main() {
        lexicalAnalyzer = new LexicalAnalyzer();
    }

    public void openFileChooser() {
        // Create a file chooser to select a file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a File");

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected File: " + selectedFile.getAbsolutePath());

            // Analyze the selected file
            lexicalAnalyzer.analyzeFile(selectedFile);

            // Print errors or success message
            lexicalAnalyzer.printErrors();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileExplorerGUI_main fileChooserGUI = new FileExplorerGUI_main();
            fileChooserGUI.openFileChooser();
        });
    }
}
