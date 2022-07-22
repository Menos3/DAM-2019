package cat.arsgbm.coral.controllers;

import cat.arsgbm.coral.Main;
import cat.arsgbm.coral.classes.DefaultController;
import cat.arsgbm.coral.classes.DefaultDBController;
import cat.arsgbm.coral.classes.TipusCerca;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController extends DefaultController {


    DefaultDBController defaultDBController;
    private Object CercaTransportistesController;

    @FXML
    public void miConfiguracioAppOnAction(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Configuracio_aplicacio.fxml"));

        try {

            Parent root = loader.load();
            ConfiguracioAplicacioController controller = loader.getController();
            controller.setEmf(getEmf());
            Stage stage = new Stage();
            stage.setTitle("Configuracio de la Aplicacio");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            //stage.alwaysOnTopProperty();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al iniciar", ex);
        }
    }

    public void sobreAppOnAction(ActionEvent event) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/view/Sobre_aplicacio.fxml"));
            //getclass i get resorce son les rutes on ha d'anar a buscar
            // quan crei l'executable l'exectutable ho buscarà dintre del .jar
            Stage stage = new Stage();
            stage.setTitle("SOBREEEEEE!!!!");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();//mostra les tres línies anteriors
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
            //crido als mètode per obrir la seguent finestra i passo vista,titol i tipus cerca

    //*************************   TRANSPORTISTES    TRANSPORTISTES    TRANSPORTISTES   ********************
    @FXML
    public void miTransportistaAltaOnAction(ActionEvent event) {
        miOnAction("/view/Alta_transportistes.fxml","ALTA TRANSPORTISTES",TipusCerca.ALTA);
    }
//POSO TIPUSCERCA PER QUE NO L'UTILITZO, L'ENVIO A CONTROLADOR QUE HEREDA DE DEFAULTDBCONTROLLER QUE SI!!! QUE TÉ EL MÈTODE
    @FXML
    public void miTransportistaCercaOnAction(ActionEvent event) {
        miOnAction("/view/Cerca_transportistes.fxml","CERCA TRANSPORTISTES",TipusCerca.CERCA);

    }

    @FXML
    public void miTransportistaBaixaOnAction(ActionEvent event) {
        miOnAction("/view/Cerca_transportistes.fxml","BAIXA TRANSPORTISTES",TipusCerca.BAIXA);

    }

    // *********************************   XOFERS     XOFERS      XOFERS   *************************************
    @FXML
    public void miXoferAltaOnAction(ActionEvent event) {
        miOnAction("/view/Alta_xofers.fxml","ALTA XOFERS",TipusCerca.ALTA);
    }
    @FXML
    public void miXoferBaixaOnAction(ActionEvent event) {
        miOnAction("/view/Cerca_xofers.fxml","BAIXA XOFERS",TipusCerca.BAIXA);
    }
    @FXML
    public void miXoferCercaOnAction(ActionEvent event) {
        miOnAction("/view/Cerca_xofers.fxml","CERCA XOFERS",TipusCerca.CERCA);
    }

    // *********************************   VEHICLES   VEHICLES   VEHICLES   *************************************

    @FXML
    public void miAltaVehicleOnAction(ActionEvent actionEvent) {
        miOnAction("/view/Alta_vehicles.fxml", "ALTA VEHICLES", TipusCerca.ALTA);
    }

    public void miTipusVehicleOnAction(ActionEvent actionEvent) {
        miOnAction("/view/Alta_tipusVehicle.fxml", "ALTA DE TIPUS DE VEHICLE", TipusCerca.ALTA);
    }

    public void miTipusTargetaOnAction(ActionEvent actionEvent) {
        miOnAction("/view/Alta_targetaTransport.fxml", "ALTA DE TARGETES DE TRANSPORT", TipusCerca.ALTA);
    }

//***************************GENERALITZAR ON_ACTIONS***************************************************************

    private void miOnAction(String vista, String titol, TipusCerca mode) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(vista));
        try {
            Parent root = loader.load();//aquí executa inicializable
            DefaultDBController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setMode(mode);
            controller.inicia();
            Stage stage = new Stage();
            stage.setTitle(titol);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            //stage.alwaysOnTopProperty();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al iniciar", ex);
        }
    }
    //***************************************************************************************

    public void miSortirOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sortir");
        alert.setHeaderText("Esteu sortint del programa");
        alert.setContentText("Voleu sortir del programa?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            Platform.exit();
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }





}
