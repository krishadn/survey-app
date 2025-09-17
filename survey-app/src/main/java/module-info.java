module kpes {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.jensd.fx.glyphs.fontawesome;

    opens kpes to javafx.fxml;
    opens kpes.controller to javafx.fxml;

    exports kpes;
    exports kpes.controller;
    exports kpes.dao;
    exports kpes.model;
}