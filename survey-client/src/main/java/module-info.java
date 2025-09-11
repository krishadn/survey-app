module indigo8 {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.jensd.fx.glyphs.fontawesome;


    opens indigo8solutions to javafx.fxml;

    exports indigo8solutions;
    exports indigo8solutions.dao;
    exports indigo8solutions.model;
}