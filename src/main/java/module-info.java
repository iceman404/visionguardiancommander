module com.project4.visionguardiancommander {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
            
    opens com.project4.visionguardiancommander to javafx.fxml;
    exports com.project4.visionguardiancommander;
}