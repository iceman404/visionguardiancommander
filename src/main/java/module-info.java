module com.project.visionguardiancommander {
    requires javafx.controls;
    requires javafx.fxml;
            
    requires org.controlsfx.controls;


    //requires org.bytedeco.javacpp;

    requires java.sql;
    requires java.desktop;
    requires org.bytedeco.javacv;
    requires jakarta.mail;
    requires opencv;
    requires javacpp;
    //requires opencv;


    opens com.project.visionguardiancommander to javafx.fxml;
    exports com.project.visionguardiancommander;
}