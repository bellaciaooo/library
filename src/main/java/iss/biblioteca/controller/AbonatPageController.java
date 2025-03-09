package iss.biblioteca.controller;

import iss.biblioteca.domain.Abonat;
import iss.biblioteca.domain.Carte;
import iss.biblioteca.domain.Imprumut;
import iss.biblioteca.domain.Status;
import iss.biblioteca.dto.CarteImprumutDTO;
import iss.biblioteca.service.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AbonatPageController {
    private Service service;
    private Abonat abonat;

    ObservableList<Carte> cartiModel = FXCollections.observableArrayList();
    ObservableList<CarteImprumutDTO> cartiImprumutateModel = FXCollections.observableArrayList();

    @FXML
    Button logoutButton;
    @FXML
    Button imprumutaCarteButton;

    @FXML
    TableColumn<Carte,String> titluNameColumn;
    @FXML
    TableColumn<Carte, String> autorColumn;
    @FXML
    TableColumn<Carte,String> nrExemplareColumn;
    @FXML
    TableColumn<Carte,String> taxaColumn;
    @FXML
    TableView<Carte> cartiList;

    @FXML
    TableColumn<CarteImprumutDTO,String> titluImprumutColumn;
    @FXML
    TableColumn<CarteImprumutDTO, String> autorImprumutColumn;
    @FXML
    TableColumn<CarteImprumutDTO,String> taxaImprumutColumn;
    @FXML
    TableColumn<CarteImprumutDTO,String> plataImprumutColumn;
    @FXML
    TableView<CarteImprumutDTO> cartiImprumutateList;

    public void setService(Service service){
        this.service = service;
    }

    public void setUser(Abonat utilizator) {
        this.abonat = utilizator;
        initModel();
    }

    private void initModel() {
        //lista carti
        Iterable<Carte> messages = service.getCarti();
        List<Carte> spc = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        System.out.println(spc.toArray().length);
        cartiModel.setAll(spc);

        //lista carti imprumutate
        Iterable<CarteImprumutDTO> messages2 = service.getCartiImprumutateForAbonat(abonat.getId());
        List<CarteImprumutDTO> spc2 = StreamSupport.stream(messages2.spliterator(), false)
                .collect(Collectors.toList());
        System.out.println(spc2.toArray().length);
        cartiImprumutateModel.setAll(spc2);
    }

    @FXML
    public void initialize() {
        //colorare carti
        cartiList.setRowFactory(tv -> new TableRow<Carte>() {
            @Override
            protected void updateItem(Carte item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || item.getNrExemplare() == null)
                    setStyle("");
                else if (item.getNrExemplare() - service.nrExemplareImprumutatePerCarte(item.getId()) == 0) {
                    setStyle("-fx-background-color: rgba(241,64,64,0.95);");
                }
                else
                    setStyle("");
            }
        });

        //lista carti
        titluNameColumn.setCellValueFactory(new PropertyValueFactory<Carte,String>("titlu"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<Carte,String>("autor"));
        taxaColumn.setCellValueFactory(new PropertyValueFactory<Carte,String>("taxa"));
        nrExemplareColumn.setCellValueFactory(c -> {
            Carte carte = c.getValue();
            Integer imprumutate = this.service.nrExemplareImprumutatePerCarte(carte.getId());
            Integer disp = carte.getNrExemplare() - imprumutate;
            return new SimpleStringProperty(disp.toString());
        });

        //lista carti imprumutate
        titluImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("titlu"));
        autorImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("autor"));
        taxaImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("taxa"));
        plataImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("plata"));

        //models
        cartiList.setItems(cartiModel);
        cartiImprumutateList.setItems(cartiImprumutateModel);
    }

    @FXML
    public void imprumutaButtonClicked() throws IOException {
        try {
            Carte carte = (Carte) cartiList.getSelectionModel().getSelectedItem();
            if(carte == null){
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION, "Info", "Selecteaza o carte pentru a putea realiza un imprumut!");
            }
            else {
                Imprumut imprumut = new Imprumut(this.abonat,carte, Status.neefectuata);
                this.service.addImprumut(imprumut);
                initModel();
            }
        }
        catch (IllegalArgumentException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        } catch (RuntimeException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
    }

    @FXML
    public void logoutButtonClicked() throws IOException {
        try {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Goodbye friend!", "See you soon,"+abonat.getNume()+"!");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/loggingPage.fxml"));
            AnchorPane root = loader.load();

            LoggingPageController controller = loader.getController();
            controller.setService(service);

            Stage stage = new Stage();
            Scene scene = new Scene(root,512,468);
            stage.setTitle("Biblioteca Bella");
            stage.setScene(scene);
            stage.show();

            //aici se inchide fereastra de homepage
            Stage thisStage = (Stage) logoutButton.getScene().getWindow();
            thisStage.close();
        } catch (IllegalArgumentException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        } catch (RuntimeException e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", e.getMessage());
            return;
        }
    }
}
