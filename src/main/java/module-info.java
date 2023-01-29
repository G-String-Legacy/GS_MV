module com.papaworx.gs_lv {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires java.prefs;

    opens com.papaworx.gs_lv to javafx.fxml;
    exports com.papaworx.gs_lv;
}