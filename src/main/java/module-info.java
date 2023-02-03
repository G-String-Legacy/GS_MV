module com.papaworx.gs_mv {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires java.prefs;
    requires java.desktop;
    requires javafx.web;

    uses com.papaworx.gs_mv.GS_Application;
    uses com.papaworx.gs_mv.GS_Controller;
    uses com.papaworx.gs_mv.model.Nest;
    uses com.papaworx.gs_mv.model.SampleSizeTree;
    uses com.papaworx.gs_mv.model.Facet;
    uses com.papaworx.gs_mv.steps.SynthGroups;
    uses com.papaworx.gs_mv.steps.AnaGroups;
    uses com.papaworx.gs_mv.steps.GSetup;
    uses com.papaworx.gs_mv.utilities.SampleSizeView;
    uses com.papaworx.gs_mv.utilities.Filer;
    uses com.papaworx.gs_mv.utilities.Factor;
    uses com.papaworx.gs_mv.utilities.VarianceComponent;
    uses com.papaworx.gs_mv.utilities.TextStack;
    uses com.papaworx.gs_mv.utilities.ConstructSimulation;
    uses com.papaworx.gs_mv.utilities.Normal;
    uses com.papaworx.gs_mv.utilities.Lehmer;
    uses com.papaworx.gs_mv.utilities.FacetModView;
    uses com.papaworx.gs_mv.utilities.About;
    uses com.papaworx.gs_mv.utilities.CombConstrct;

    opens com.papaworx.gs_mv to javafx.fxml;
    exports com.papaworx.gs_mv;
}