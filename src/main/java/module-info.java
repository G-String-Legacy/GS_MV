module org.gs_users.gs_mv {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires java.prefs;
    requires java.desktop;
    requires javafx.web;

    uses org.gs_users.gs_mv.GS_Application;
    uses org.gs_users.gs_mv.GS_Controller;
    uses org.gs_users.gs_mv.model.Nest;
    uses org.gs_users.gs_mv.model.SampleSizeTree;
    uses org.gs_users.gs_mv.model.Facet;
    uses org.gs_users.gs_mv.steps.SynthGroups;
    uses org.gs_users.gs_mv.steps.AnaGroups;
    uses org.gs_users.gs_mv.steps.GSetup;
    uses org.gs_users.gs_mv.utilities.SampleSizeView;
    uses org.gs_users.gs_mv.utilities.Filer;
    uses org.gs_users.gs_mv.utilities.Factor;
    uses org.gs_users.gs_mv.utilities.VarianceComponent;
    uses org.gs_users.gs_mv.utilities.TextStack;
    uses org.gs_users.gs_mv.utilities.ConstructSimulation;
    uses org.gs_users.gs_mv.utilities.Normal;
    uses org.gs_users.gs_mv.utilities.Lehmer;
    uses org.gs_users.gs_mv.utilities.FacetModView;
    uses org.gs_users.gs_mv.utilities.About;
    uses org.gs_users.gs_mv.utilities.CombConstrct;

    opens org.gs_users.gs_mv to javafx.fxml;
    exports org.gs_users.gs_mv;
}