module com.animearray.ouranimearray {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;// Scenic view (https://stackoverflow.com/q/64703153/17771525)

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.dlsc.gemsfx;
    requires net.synedra.validatorfx;
    requires com.miglayout.javafx;
    requires com.miglayout.core;
    requires MaterialFX;
    requires fr.brouillard.oss.cssfx;
    requires AnimateFX;
    requires java.sql;
    requires sqlite.jdbc;
    requires com.tobiasdiez.easybind;
    requires VirtualizedFX;

    opens com.animearray.ouranimearray to javafx.fxml;
    exports com.animearray.ouranimearray to javafx.graphics;
    opens com.animearray.ouranimearray.model to javafx.fxml;
    exports com.animearray.ouranimearray.widgets;
    opens com.animearray.ouranimearray.widgets to javafx.fxml;
    exports com.animearray.ouranimearray.search;
    opens com.animearray.ouranimearray.search to javafx.fxml;
    exports com.animearray.ouranimearray.widgets.DAOs;
    opens com.animearray.ouranimearray.widgets.DAOs to javafx.fxml;
}