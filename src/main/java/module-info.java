module com.example.javafx_ghilani {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml;
    requires java.desktop;

    exports com.example.javafx_ghilani;
    exports com.example.javafx_ghilani.model;
    exports com.example.javafx_ghilani.controller;
    exports com.example.javafx_ghilani.database;
    exports com.example.javafx_ghilani.util;
}