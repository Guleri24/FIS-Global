package com.horizonxe;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainController {
    private static BufferedWriter writer;
    private final List<String> projects = new ArrayList<>();
    private final List<String> modules = new ArrayList<>();
    @FXML
    public ComboBox<String> projectMenu;
    @FXML
    public ComboBox<String> moduleMenu;
    public AnchorPane mainView;
    private File executableFile;
    private File dataFile;
    private boolean isOSWindows;

    public void initialize() throws IOException {
        // Creates executable file Exec for creating scripts of the maven command
        createOsSpecificFile("exec");

        writer = Files.newBufferedWriter(Paths.get(String.valueOf(executableFile)));
        writer.write("");
        writer.flush();
        initData();
    }

    private void initData() throws IOException {
        // Creates Data file for storing projects and modules
        createOsSpecificFile("data");

        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader(dataFile));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            if (data.length > 1) {
                if (!data[0].isBlank())
                    projects.add(data[0]);
                if (!data[1].isBlank())
                    modules.add(data[1]);
            } else {
                projects.add(data[0]);
            }
        }
        csvReader.close();

        if (projects.isEmpty())
            projects.add("Empty");
        if (modules.isEmpty())
            modules.add("Empty");

        projectMenu.setItems(FXCollections.observableArrayList(projects));
        moduleMenu.setItems(FXCollections.observableArrayList(modules));
    }

    private void createOsSpecificFile(String type) throws IOException {
        isOSWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        String username = System.getProperty("user.name");
        String windowsPath = "C:\\Users\\" + username + "\\FisGlobal\\";
        String linuxPath = "/home/" + username + "/FisGlobal/";
        switch (type) {
            case "exec" -> {
                if (isOSWindows) {
                    executableFile = new File(windowsPath + "exec.bat");
                } else {
                    executableFile = new File(linuxPath + "exec.sh");
                }

                // Checks for permission and creation of exec file
                executableFile.getParentFile().mkdir();
                executableFile.createNewFile();
                executableFile.setExecutable(true);
            }
            case "data" -> {
                if (isOSWindows) {
                    dataFile = new File(windowsPath + "data.csv");
                } else {
                    dataFile = new File(linuxPath + "data.csv");
                }

                // Checks for permission and creation of data file
                dataFile.getParentFile().mkdir();
                dataFile.createNewFile();
                dataFile.setExecutable(true);
            }
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
        String preMavenBuildCommands = "ls ";    // Add commands which needed to be executed before maven build command
        writer.append(preMavenBuildCommands)
                .append(" && mvn -DProj ")
                .append(project)
                .append(" -DMod ")
                .append(module);
    }

    @FXML
    public void triggerCmd() throws IOException {
        writeData();
        Stage stage = (Stage) mainView.getScene().getWindow();
        writer.close();
        if (isOSWindows) {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"" + executableFile.getAbsolutePath() + "\"");
            stage.close();
        } else {
            stage.close();
            Runtime.getRuntime().exec("alacritty" + " -e bash -c " + executableFile.getAbsolutePath() + ";read");

        }
    }

    @FXML
    public void addData() throws IOException {
        Stage stage = (Stage) mainView.getScene().getWindow();
        if (isOSWindows) {
            Runtime.getRuntime().exec("cmd /c start excel /K \"" + dataFile.getAbsolutePath() + "\"");
            stage.close();
        } else {
            stage.close();
            Runtime.getRuntime().exec("gio " + " open " + dataFile.getAbsolutePath());
        }
    }
}

