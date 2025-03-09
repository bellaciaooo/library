module iss.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens iss.biblioteca to javafx.fxml;
    exports iss.biblioteca;

    opens iss.biblioteca.service to javafx.fxml;
    exports iss.biblioteca.service;

    opens iss.biblioteca.domain to javafx.fxml;
    exports iss.biblioteca.domain;

    opens iss.biblioteca.controller to javafx.fxml;
    exports iss.biblioteca.controller;

    opens iss.biblioteca.repo to javafx.fxml;
    exports iss.biblioteca.repo;

    opens iss.biblioteca.dto to javafx.fxml;
    exports iss.biblioteca.dto;
}