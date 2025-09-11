module indigo8 {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.jensd.fx.glyphs.fontawesome;

    opens indigo8 to javafx.fxml;
    opens indigo8.controller to javafx.fxml;

    exports indigo8;
    exports indigo8.controller;
    exports indigo8.dao;
    exports indigo8.model;
}