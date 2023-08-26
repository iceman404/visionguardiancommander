module com.project.visionguardiancommander {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;

    requires opencv;

        requires org.bytedeco.javacpp;

    requires java.sql;
    requires java.desktop;
    //requires org.bytedeco.javacv;


    opens com.project.visionguardiancommander to javafx.fxml;
    exports com.project.visionguardiancommander;
}