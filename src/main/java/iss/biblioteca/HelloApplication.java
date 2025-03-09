package iss.biblioteca;
import java.math.*;

import iss.biblioteca.controller.LoggingPageController;
import iss.biblioteca.domain.Abonat;
import iss.biblioteca.domain.Imprumut;
import iss.biblioteca.repo.AbonatDbRepo;
import iss.biblioteca.repo.BibliotecarDbRepo;
import iss.biblioteca.repo.CarteDbRepo;
import iss.biblioteca.repo.ImprumutDbRepo;
import iss.biblioteca.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config.properties "+e);
        }

        AbonatDbRepo abonatDbRepo = new AbonatDbRepo(props);
        BibliotecarDbRepo bibliotecarDbRepo = new BibliotecarDbRepo(props);
        CarteDbRepo carteDbRepo = new CarteDbRepo(props);
        ImprumutDbRepo imprumutDbRepo = new ImprumutDbRepo(props,carteDbRepo,abonatDbRepo);

        Iterable<Abonat> abonati = abonatDbRepo.findAll();
        System.out.println(abonati);
        System.out.println(carteDbRepo.findAll());
        System.out.println(imprumutDbRepo.findCartiImprumutateForAbonat(1L));

        Service service = new Service(abonatDbRepo,bibliotecarDbRepo,carteDbRepo,imprumutDbRepo);
        int[] a={1,4,5};



        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/loggingPage.fxml"));
        AnchorPane root = loader.load();

        LoggingPageController loginPageController = loader.getController();
        loginPageController.setService(service);

        Scene scene = new Scene(root,512,468);
        stage.setTitle("Biblioteca Bella");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}