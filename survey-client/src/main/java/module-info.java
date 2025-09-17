module kpesclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.jensd.fx.glyphs.fontawesome;


    opens kpesclient to javafx.fxml;

    exports kpesclient;
    exports kpesclient.dao;
    exports kpesclient.model;
}