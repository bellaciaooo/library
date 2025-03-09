package iss.biblioteca.controller;

import iss.biblioteca.domain.*;
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
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BibliotecarPageController {
    private Service service;
    private Bibliotecar bibliotecar;

    ObservableList<Abonat> abonatiModel = FXCollections.observableArrayList();
    ObservableList<CarteImprumutDTO> imprumutModel = FXCollections.observableArrayList();

    @FXML
    TextField textFieldNume;
    @FXML
    TextField textFieldCnp;
    @FXML
    TextField textFieldTelefon;
    @FXML
    TextField textFieldAdresa;
    @FXML
    TextField textFieldUsername;
    @FXML
    TextField textFieldParola;

    @FXML
    Button logoutButton;
    @FXML
    Button restituireCarteButton;
    @FXML
    Button stergeAbonatButton;
    @FXML
    Button creareAbonatButton;
    @FXML
    Button actualizareAbonatButton;
    @FXML
    Button plataTaxaButton;

    @FXML
    TableColumn<Abonat,String> codColumn;
    @FXML
    TableColumn<Abonat, String> numeColumn;
    @FXML
    TableColumn<Abonat, String> usernameColumn;
    @FXML
    TableColumn<Abonat,String> cnpColumn;
    @FXML
    TableColumn<Abonat,String> telefonColumn;
    @FXML
    TableColumn<Abonat,String> adresaColumn;
    @FXML
    TableView<Abonat> abonatiList;

    @FXML
    TableColumn<CarteImprumutDTO,String> titluImprumutColumn;
    @FXML
    TableColumn<CarteImprumutDTO, String> autorImprumutColumn;
    @FXML
    TableColumn<CarteImprumutDTO,String> taxaImprumutColumn;
    @FXML
    TableColumn<CarteImprumutDTO,String> plataImprumutColumn;
    @FXML
    TableColumn<CarteImprumutDTO,String> usernameImprumutColumn;
    @FXML
    TableView<CarteImprumutDTO> cartiImprumutateAbonatList;
    @FXML
    TextField usernameAbonatSearch;
    @FXML
    TextField numeAbonatSearch;

    public void setService(Service service){
        this.service = service;
    }

    public void setUser(Bibliotecar utilizator) {
        this.bibliotecar = utilizator;
        initModel();
        handleSearchCartiImprumutateAbonat();
    }

    private void initModel() {
        //lista abonati
        Iterable<Abonat> messages = service.getAbonati();
        List<Abonat> spc = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        System.out.println(spc.toArray().length);
        abonatiModel.setAll(spc);
    }

    @FXML
    public void initialize() {
        //lista abonati
        codColumn.setCellValueFactory(new PropertyValueFactory<Abonat,String>("id"));
        numeColumn.setCellValueFactory(new PropertyValueFactory<Abonat,String>("nume"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Abonat,String>("username"));
        cnpColumn.setCellValueFactory(new PropertyValueFactory<Abonat,String>("cnp"));
        adresaColumn.setCellValueFactory(new PropertyValueFactory<Abonat,String>("adresa"));
        telefonColumn.setCellValueFactory(new PropertyValueFactory<Abonat,String>("telefon"));

        //lista imprumuturi
        titluImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("titlu"));
        autorImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("autor"));
        taxaImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("taxa"));
        plataImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("plata"));
        usernameImprumutColumn.setCellValueFactory(new PropertyValueFactory<CarteImprumutDTO,String>("usernameAbonat"));

        //sincronizare
        usernameAbonatSearch.textProperty().addListener(o -> handleSearchCartiImprumutateAbonat());
        numeAbonatSearch.textProperty().addListener(o -> handleSearchAbonati());

        //models
        abonatiList.setItems(abonatiModel);
        cartiImprumutateAbonatList.setItems(imprumutModel);
    }

    @FXML
    private void handleSearchAbonati() {
        Predicate<Abonat> p1 = n -> n.getNume().startsWith(numeAbonatSearch.getText());
        Iterable<Abonat> messages = service.getAbonati();
        List<Abonat> spc = StreamSupport.stream(messages.spliterator(), false)
                .toList();

        abonatiModel.setAll(spc
                .stream().filter(p1)
                .collect(Collectors.toList())
        );
    }

    @FXML
    public void handleSearchCartiImprumutateAbonat(){
        Predicate<CarteImprumutDTO> p1 = n -> n.getUsernameAbonat().startsWith(usernameAbonatSearch.getText());
        Iterable<CarteImprumutDTO> messages = service.getCartiImprumutate();
        List<CarteImprumutDTO> imp = StreamSupport.stream(messages.spliterator(), false)
                .toList();

        imprumutModel.setAll(imp
                .stream().filter(p1)
                .collect(Collectors.toList())
        );
    }

    @FXML
    public void plataTaxaButtonClicked() throws IOException{
        try {
            CarteImprumutDTO imprumut = (CarteImprumutDTO) cartiImprumutateAbonatList.getSelectionModel().getSelectedItem();
            if(imprumut == null){
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION, "Info", "Selecteaza o carte pentru a putea efectua plata imprumutului acesteia!");
            }
            else {
                this.service.plataTaxaImprumut(imprumut.getIdImprumut());
                handleSearchCartiImprumutateAbonat();
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION, "Info", "Plata a fost efectuata cu succes!");
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
    public void restituireCarteButtonClicked() throws IOException{
        try {
            CarteImprumutDTO imprumut = (CarteImprumutDTO) cartiImprumutateAbonatList.getSelectionModel().getSelectedItem();
            if(imprumut == null){
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION, "Info", "Selecteaza o carte pentru a putea efectua plata imprumutului acesteia!");
            }
            else {
                this.service.removeImprumut(imprumut.getIdImprumut());
                handleSearchCartiImprumutateAbonat();
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
    public void stergeAbonatButtonClicked() throws IOException{
        try {
            Abonat abonat= (Abonat) abonatiList.getSelectionModel().getSelectedItem();
            if(abonat == null){
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION, "Info", "Selecteaza un abonat pentru a-l putea sterge!");
            }
            else {
                this.service.deleteAbonat(abonat);
                initModel();
                handleSearchCartiImprumutateAbonat();
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION, "Info", "Stergerea contului de abonat a fost efectuata cu succes!");
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
    public void creareAbonatButtonClicked()throws IOException{
        try{
            if( textFieldNume.getText().isEmpty() ||
                textFieldCnp.getText().isEmpty() ||
                textFieldAdresa.getText().isEmpty() ||
                textFieldTelefon.getText().isEmpty() ||
                textFieldUsername.getText().isEmpty() ||
                textFieldParola.getText().isEmpty()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info","Introduceti toate datele corespunzatoare pentru a putea crea contul de abonat!");
                return;
            }
            else{
                String nume = textFieldNume.getText();
                String cnp = textFieldCnp.getText();
                String adresa = textFieldAdresa.getText();
                String telefon = textFieldTelefon.getText();
                String username = textFieldUsername.getText();
                String parola = textFieldParola.getText();
                Abonat abonat = new Abonat(username,parola,nume,cnp,adresa,telefon);
                this.service.saveAbonat(abonat);
                initModel();
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION, "Info", "Crearea contului de abonat a fost efectuata cu succes!");

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
    public void actualizareAbonatButtonClicked()throws IOException{
        try{
            if( textFieldNume.getText().isEmpty() ||
                    textFieldCnp.getText().isEmpty() ||
                    textFieldAdresa.getText().isEmpty() ||
                    textFieldTelefon.getText().isEmpty() ||
                    textFieldUsername.getText().isEmpty() ||
                    textFieldParola.getText().isEmpty()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info","Introduceti toate datele corespunzatoare pentru a putea actualiza contul de abonat!");
                return;
            }
            else{
                String nume = textFieldNume.getText();
                String cnp = textFieldCnp.getText();
                String adresa = textFieldAdresa.getText();
                String telefon = textFieldTelefon.getText();
                String parola = textFieldParola.getText();
                String username = textFieldUsername.getText();
                Abonat abonat = new Abonat(username,parola,nume,cnp,adresa,telefon);
                this.service.updateAbonat(abonat);
                initModel();
                MessageAlert.showMessage(null,Alert.AlertType.INFORMATION, "Info", "Actualizarea contului de abonat a fost efectuata cu succes!");
            }
        }catch (IllegalArgumentException e) {
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
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Goodbye friend!", "See you soon,"+bibliotecar.getUsername()+"!");

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
