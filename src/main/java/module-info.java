import org.gsusers.gsmv.GS_Application;
import org.gsusers.gsmv.GS_Controller;
import org.gsusers.gsmv.model.Facet;
import org.gsusers.gsmv.model.Nest;
import org.gsusers.gsmv.model.SampleSizeTree;
import org.gsusers.gsmv.steps.AnaGroups;
import org.gsusers.gsmv.steps.GSetup;
import org.gsusers.gsmv.steps.SynthGroups;
import org.gsusers.gsmv.utilities.*;

/**
 * @author ralph
 */
module org.gsusers.gsmv {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires java.prefs;
    requires java.desktop;
    requires javafx.web;

    uses GS_Application;
    uses GS_Controller;
    uses Nest;
    uses SampleSizeTree;
    uses Facet;
    uses SynthGroups;
    uses AnaGroups;
    uses GSetup;
    uses SampleSizeView;
    uses Filer;
    uses Factor;
    uses VarianceComponent;
    uses TextStack;
    uses ConstructSimulation;
    uses Normal;
    uses Lehmer;
    uses FacetModView;
    uses About;
    uses CombConstrct;

    opens org.gsusers.gsmv to javafx.fxml;
    exports org.gsusers.gsmv;
}