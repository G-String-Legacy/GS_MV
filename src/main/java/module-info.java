module com.papaworx.gs_lv {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires java.prefs;

    uses com.papaworx.gs_lv.GS_Application;
    uses com.papaworx.gs_lv.GS_Controller;
    uses com.papaworx.gs_lv.model.Nest;
    uses com.papaworx.gs_lv.model.SampleSizeTree;
    uses com.papaworx.gs_lv.model.Facet;
    uses com.papaworx.gs_lv.steps.SynthGroups;
    uses com.papaworx.gs_lv.steps.AnaGroups;
    uses com.papaworx.gs_lv.steps.GSetup;
    uses com.papaworx.gs_lv.utilities.SampleSizeView;
    uses com.papaworx.gs_lv.utilities.Filer;
    uses com.papaworx.gs_lv.utilities.Factor;
    uses com.papaworx.gs_lv.utilities.VarianceComponent;
    uses com.papaworx.gs_lv.utilities.TextStack;
    uses com.papaworx.gs_lv.utilities.ConstructSimulation;
    uses com.papaworx.gs_lv.utilities.Normal;
    uses com.papaworx.gs_lv.utilities.Lehmer;
    uses com.papaworx.gs_lv.utilities.FacetModView;
    uses com.papaworx.gs_lv.utilities.About;
    uses com.papaworx.gs_lv.utilities.CombConstrct;

    opens com.papaworx.gs_lv to javafx.fxml;
    exports com.papaworx.gs_lv;
}