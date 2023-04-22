module com.animearray.ouranimearray {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires com.miglayout.javafx;
    requires com.miglayout.core;
    requires MaterialFX;
    requires fr.brouillard.oss.cssfx;
    requires AnimateFX;
    requires java.sql;
    requires sqlite.jdbc;
    requires com.tobiasdiez.easybind;
//    requires jdk.incubator.concurrent; // Java 19 Virtual Threads

    opens com.animearray.ouranimearray to javafx.fxml;
    exports com.animearray.ouranimearray to javafx.graphics;
    exports com.animearray.ouranimearray.widgets.misc;
    opens com.animearray.ouranimearray.widgets.misc to javafx.fxml;
    opens com.animearray.ouranimearray.model to javafx.fxml;
    exports com.animearray.ouranimearray.widgets;
    opens com.animearray.ouranimearray.widgets to javafx.fxml;
    exports com.animearray.ouranimearray.search;
    opens com.animearray.ouranimearray.search to javafx.fxml;
}