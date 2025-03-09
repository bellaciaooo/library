package iss.biblioteca.controller;

import iss.biblioteca.domain.Abonat;
import iss.biblioteca.domain.Bibliotecar;
import iss.biblioteca.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoggingPageController {
    private Service service;

    @FXML
    TextField loginUsername;

    @FXML
    PasswordField loginParola;

    @FXML
    Button loginAbonatButton;

    @FXML
    Button loginBibliotecarButton;

    public void setService(Service service){
        this.service = service;
    }

    @FXML
    void loginAbonatButtonClicked() throws IOException {
        String username = loginUsername.getText();
        String password = loginParola.getText();

        if( loginUsername.getText().isEmpty() || loginParola.getText().isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info","Introduceti datele corespunzatoare!");
            return;
        }

        try {
            Abonat abonat = service.loginAbonat(username,password);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/abonatPage.fxml"));
            AnchorPane root = loader.load();

            AbonatPageController abonatPageController = loader.getController();
            abonatPageController.setService(service);
            abonatPageController.setUser(abonat);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 890, 627));
            stage.setTitle("Hello, " + abonat.getNume()+"!");
            stage.show();

            //aici se inchide fereastra de login
            Stage thisStage = (Stage) loginAbonatButton.getScene().getWindow();
            thisStage.close();
        }
        catch (IllegalArgumentException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
        /*catch (RuntimeException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }*/

    }

    @FXML
    void loginBibliotecarButtonClicked() throws IOException {
        String username = loginUsername.getText();
        String password = loginParola.getText();

        if( loginUsername.getText().isEmpty() || loginParola.getText().isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info","Introduceti datele corespunzatoare!");
            return;
        }

        try {
            Bibliotecar bibliotecar = service.loginBibliotecar(username,password);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/bibliotecarPage.fxml"));
            AnchorPane root = loader.load();

            BibliotecarPageController bibPageController = loader.getController();
            bibPageController.setService(service);
            bibPageController.setUser(bibliotecar);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1145, 716));
            stage.setTitle("Hello, " + bibliotecar.getUsername()+"!");
            stage.show();

            //aici se inchide fereastra de login
            Stage thisStage = (Stage) loginBibliotecarButton.getScene().getWindow();
            thisStage.close();
        }
        catch (IllegalArgumentException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }

    }

}
