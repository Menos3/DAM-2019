package cat.arsgbm.coral.controllers;

import cat.arsgbm.coral.Main;
import cat.arsgbm.coral.classes.*;
import cat.arsgbm.coral.model.Transportistes;
import cat.arsgbm.coral.model.Xofers;
import dam2.controls.limitedtextfield.LimitedTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CercaXofersController extends DefaultDBController implements Initializable {

    //declaro totes les variables que després relacionaré amb les columnes del formulari
    @FXML
    Button btModifica;
    @FXML
    Button btBaixaFisica;
    @FXML
    Button btBaixaTemporal;

    @FXML
    private LimitedTextField tfNomCIF;

    @FXML
    private TableView<Xofers> tvXofers;
    @FXML
    private TableColumn<Transportistes, String> colTransportista;
    @FXML
    private TableColumn<Xofers, String> colCognoms;
    @FXML
    private TableColumn<Xofers, String> colNom;
    @FXML
    private TableColumn<Xofers, String> colNumXofer;
    @FXML
    private TableColumn<Xofers, String> colTelefon;
    @FXML
    private TableColumn<Xofers, Date> colEmpremtaDigital;
    @FXML
    private TableColumn<Xofers, Date> colCarnetConduir;
    @FXML
    private TableColumn<Xofers, Date> colMercPerilloses;
    @FXML
    private TableColumn<Xofers, Date> colCarnetCarreter;
    @FXML
    private TableColumn<Xofers, Boolean> colActivaBaixaTemporal;
    private DAOHelper<Xofers> helper;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //omplim la taula transportistes amb configuraColumnes()
        configuraColumnes();

        //afegim un event que escolta els clicks del ratolí per no haver de premer button
        tvXofers.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == Constants.DOUBLE_CLICK && event.getButton() == MouseButton.PRIMARY) {
                    //li diem que si clica dues vegades amb el botó dret vagi a pantalla modificar si ve de cerca
 //*******************TIPUSCERCA L'ACONSEGUIM AMB getMode , PER QUE L'HERETA DE DEFAULTDBCONTROLLER **************************
                    if(getMode() == TipusCerca.CERCA){
                        accionsPerModificar(getMode());
                    }else{      //si no ve de cerca lidiem que activi o desactivi baixa temporal i a la seguent si passem data actual(data baixa)
                        tvXofers.getSelectionModel().getSelectedItem().setActivaBaixaTemporal(!tvXofers.getSelectionModel().getSelectedItem().isActivaBaixaTemporal());
                        tvXofers.getSelectionModel().getSelectedItem().setBaixaTemporal(Utilitats.getDataActual());
                        helper.update(tvXofers.getSelectionModel().getSelectedItem());
                        tvXofers.refresh();
                        filtraTaula();
                    }
                }
            }
        });
    }

    @Override               // PRIMER HA DE CREAR LA CONEXIÓ AMB LA CLASSE TRANSPORTISTES PER AIXÒ ENTRA QUAN LI DIU DESDE CONTROLADOR
    public void inicia() {
        //creo la conexió a bbdd i li dic a quina taula ha de preguntatr
        helper = new DAOHelper<>(getEmf(), Xofers.class);
        switch (getMode()) {
            // segons el mode que utilitzem configuro paràmetres de formulari
            case CERCA:
                btModifica.setText("Modifica");
                btBaixaFisica.setVisible(false);
                btBaixaTemporal.setVisible(false);
                break;
            case BAIXA:
                btModifica.setVisible(false);
                break;
        }
        System.out.println("DESPRÉS DE HELPER");
        // omplo la taula del formulari amb els valors de la bbdd que li he dit avans  i li dic on posar focus
        actualitzaTaula();
        goTableItem(0);
    }
// envia el focus a la filera qiue li diguem després d'actualitzar el tvXofers
    private void goTableItem(int row) {
        tvXofers.requestFocus();
        tvXofers.scrollTo(row);
        tvXofers.getSelectionModel().select(row);
        tvXofers.getFocusModel().focus(row);
    }

    private void actualitzaTaula() {
        tvXofers.setItems(helper.getAllToObservableList());
        filtraTaula();
    }

    private void filtraTaula() {
        // 1.  Ajustar el ObservableList en una llista filtrada (inicialment mostra totes les dades).
        FilteredList<Xofers> filteredData = new FilteredList<>(tvXofers.getItems(), p -> true);
         //
        // 2. Estableix el Predicat del filtre cada vegada que canvia en filtre
        //AQUÍ HI HA EL LISTENER QUE QUAN ESCRIUS UNA LLETRA PASSA PER TOTS ELS ITEMS DE TVXOFERS
        tfNomCIF.textProperty().addListener((observable, oldValue, newValue) -> {
            //FILTEREDDATA ÉS UNA LLISTA DEL FILTRATGE DE LA LLISTA QUE HI HAVIA I A DINS DE VARIABLE XOFER ES GUARDEN ELS ITEMS DE XOFER
            filteredData.setPredicate(xofer -> {
                // Si el text del filtre està buit (null o empty), mostrar-ho tot
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (xofer.getCognomXofer().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // El filtre coincideix amb el nom del producte                            EXPLICACIÓ??
                }else if ((xofer.getTransportista().getRaosocial().indexOf(lowerCaseFilter) != -1)) {
                    return true; // El filtre conincideix amb el codi del producte
                }
                return false; // No hi ha coincidència
            });
        });

        // 3. Embolica el FilteredList en una SortedList.
        SortedList<Xofers> sortedData = new SortedList<>(filteredData);

        // 4. Lliga el comparador de la SortedList al del TableView
        // 	  En qualsevol altre cas, l'ordenació de la TableView no tindrà efecte
        sortedData.comparatorProperty().bind(tvXofers.comparatorProperty());

        // 5. Afegeix la informació ordenada (i filtrada) a la taula
        tvXofers.setItems(sortedData);
    }

    private void configuraColumnes() {

        //relacionem les columnes amb la variable java que declarem al model de xofer

        colTransportista.setCellValueFactory(new PropertyValueFactory<>("transportista"));
        colCognoms.setCellValueFactory(new PropertyValueFactory<>("cognomXofer"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomXofer"));
        colNumXofer.setCellValueFactory(new PropertyValueFactory<>("numIntern"));
        colTelefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        colEmpremtaDigital.setCellValueFactory(new PropertyValueFactory<>("empremta"));
        colCarnetConduir.setCellValueFactory(new PropertyValueFactory<>("carnetConduir"));
        colMercPerilloses.setCellValueFactory(new PropertyValueFactory<>("carnetAdr"));
        colCarnetCarreter.setCellValueFactory(new PropertyValueFactory<>("carnetCarreter"));
        colActivaBaixaTemporal.setCellFactory(column -> new CheckBoxTableCell());
        colActivaBaixaTemporal.setCellValueFactory(dadesCella -> {
            Xofers valorCella = dadesCella.getValue();
            BooleanProperty property = new SimpleBooleanProperty();
            property.setValue(valorCella.isActivaBaixaTemporal());
            return property;
        });
    }

    public void btBaixaFisicaXoferOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Constants.APP_NAME);
        alert.setHeaderText(Constants.MSG_ADV_CONFIRMAR_ACCIO);
        alert.setContentText(Constants.MSG_ADV_ESTAS_SEGUR_ELIMINAR + " al xofer: \n\n [ " +
                tvXofers.getSelectionModel().getSelectedItem().getNomXofer()+
                tvXofers.getSelectionModel().getSelectedItem().getCognomXofer() + " ]");

        ButtonType btNo = new ButtonType(Constants.BTN_NO);
        ButtonType btSi = new ButtonType(Constants.BTN_SI);
        alert.getButtonTypes().setAll(btNo, btSi);

        Optional<ButtonType> result = alert.showAndWait(); //ESPERA A QUE APRETIS BOTÓ PER FER ALERTA
        // si cliquem SI envia ordre a bbdd per esborrar fila
        if (result.get() == btSi) {
            int itemActual = tvXofers.getSelectionModel().getSelectedIndex();
            helper.delete(tvXofers.getSelectionModel().getSelectedItem().getId());
            //actualitza i amb if coloquem focus a registre anterior
            actualitzaTaula();
            if (itemActual > 0)
                goTableItem(itemActual - 1);
            else
                goTableItem(0);
        } else {
            // Missatge cancel·lacio
        }
    }



    public void btModificaXoferOnAction(ActionEvent actionEvent) {
        accionsPerModificar(getMode());
    }
    private void accionsPerModificar(TipusCerca cr) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Alta_xofers.fxml"));
            Parent root = loader.load();
            AltaXofersController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setMode(getMode());
            controller.setXofer(tvXofers.getSelectionModel().getSelectedItem());
            controller.inicia();  //CREA CONEXIÓ AMB CLASSE
            Stage stage = new Stage();
            stage.setTitle("Modifica Transportistes");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.out.println("tancar");
                    actualitzaTaula();
                }
            });
            stage.show();
            //stage.alwaysOnTopProperty();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al iniciar", ex);
        }
    }

    public void btBaixaTemporalXoferOnAction(ActionEvent actionEvent) {
        tvXofers.getSelectionModel().getSelectedItem().setActivaBaixaTemporal(!tvXofers.getSelectionModel().getSelectedItem().isActivaBaixaTemporal());
        tvXofers.getSelectionModel().getSelectedItem().setBaixaTemporal(Utilitats.getDataActual());
        helper.update(tvXofers.getSelectionModel().getSelectedItem());
        tvXofers.refresh();
        filtraTaula();
    }
}
