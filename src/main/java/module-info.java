module fis.global {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.horizonxe to javafx.fxml;
    exports com.horizonxe;
}