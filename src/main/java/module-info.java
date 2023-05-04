module com.example.mancalapro {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;
    requires org.json;
    requires jbcrypt;
    opens com.example.mancalapro to javafx.fxml, org.json, org.mindrot;
    exports com.example.mancalapro;
    exports com.example.mancalapro.model;
}