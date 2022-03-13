package com.horizonxe;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainController {
    private static BufferedWriter writer;
    private final List<String> projects = new ArrayList<>();
    private final List<String> modules = new ArrayList<>();
    @FXML
    public Button triggerCmd;
    @FXML
    public ComboBox<String> projectMenu;
    @FXML
    public ComboBox<String> moduleMenu;
    public AnchorPane mainView;
    private File file;
    private boolean isWindows;

    public void initialize() throws IOException, InterruptedException {
        file = newFile();
        file.getParentFile().mkdir();
        file.createNewFile();
        file.setExecutable(true);
        writer = Files.newBufferedWriter(Paths.get(String.valueOf(file)));
        writer.write("");
        writer.flush();
        initData();
        projectMenu.getItems().addAll(projects);
        projectMenu.setValue("");  // empty selection is object and not null
        moduleMenu.getItems().addAll(modules);
    }

    private File newFile() throws IOException, InterruptedException {
        isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        String username = System.getProperty("user.name");

        if (isWindows) {
            return new File("C:\\Users\\" + username + "\\FisGlobal\\data.bat");
        } else {
            return file = new File("/home/" + username + "/FisGlobal/data.sh");
        }
    }

    @FXML
    public void writeData() {
        try {
            if (!projectMenu.getValue().isEmpty()) {
                writeData(writer, projectMenu.getValue(), moduleMenu.getValue());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeData(BufferedWriter writer, String project, String module) throws IOException {
        writer.append("mvn -DProj ").append(project).append(" -DMod ").append(module);
    }

    // Enter the projects and modules here
    private void initData() {
        projects.add("France");
        projects.add("Germany");
        projects.add("Switzerland");

        modules.add("Paris");
        modules.add("Strasbourg");
        modules.add("Berlin");
        modules.add("Cologne");
        modules.add("Munich");
        modules.add("Zurich");

    }

    @FXML
    public void triggerCmd() throws IOException {
        writeData();
        Stage stage = (Stage) mainView.getScene().getWindow();
        writer.close();

        if (isWindows) {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"" + file.getAbsolutePath() + "\"");
            stage.close();
        } else {
            stage.close();
        /*
		    Runtime.getRuntime().exec(System.getenv("TERM") + " -e bash -c " + file.getAbsolutePath() + ";read");
        */
            Runtime.getRuntime().exec("alacritty" + " -e bash -c " + file.getAbsolutePath() + ";read");
        }

    }
}

