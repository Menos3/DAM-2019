package cat.arsgbm.coral.controllers;

import cat.arsgbm.coral.Main;
import cat.arsgbm.coral.classes.*;
import cat.arsgbm.coral.model.Transportistes;
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

public class CercaTransportistesController extends DefaultDBController implements Initializable {
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
    private TableView<Transportistes> tvTransportistes;
    @FXML
    private TableColumn<Transportistes, String> colRaosocial;
    @FXML
    private TableColumn<Transportistes, String> colCIFNIF;
    @FXML
    private TableColumn<Transportistes, String> colCorreuTransp;
    @FXML
    private TableColumn<Transportistes, String> colTelefon;
    @FXML
    private TableColumn<Transportistes, Date> colCertificathisenda;
    @FXML
    private TableColumn<Transportistes, Date> colCertificatprl;
    @FXML
    private TableColumn<Transportistes, Date> colAssegurancamerc;
    @FXML
    private TableColumn<Transportistes, Date> colAssegurancarc;
    @FXML
    private TableColumn<Transportistes, Boolean> colBaixaTemporal;

    private DAOHelper<Transportistes> helper;
    private TipusCerca mode = TipusCerca.CERCA;

    /**
     *
     * @param location  Parametre de connexio a la base de dades
     * @param resources Parametre que passa dades a aquesta classe
     *
     * Metode que inicialitza els valors pel funcionament de la classe i el esdeveniment de quan fem doble clic amb el boto primari del ratoli
     * que s'obri el formulari de modificacio (formulari d'alta modificat) i per
     * S'utilitzen els metodes configuraColumnes() per omplir la taula, handle() per el doble clic amb el boto primari del ratoli, i per veure el checkbox
     * de la baixa temporal i el metode filtraTaula() per fer la cerca per nom o CIF
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //omplim la taula transportistes amb configuraColumnes()

        configuraColumnes();

        //afegim un event que escolta els clicks del ratolí per no haver de premer button
        tvTransportistes.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {


            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == Constants.DOUBLE_CLICK &&(! tvTransportistes.getItems().isEmpty()) && event.getButton() == MouseButton.PRIMARY) {
                    //li diem que si clica dues vegades amb el botó dret vagi a pantalla modificar si ve de cerca
                    if(TipusCerca.CERCA==mode){
                        accionsPerModificar(mode);
                    }else{      //si no ve de cerca lidiem que activi o desactivi baixa temporal i a la seguent si passem data actual(data baixa)
                        tvTransportistes.getSelectionModel().getSelectedItem().setActivaBaixaTemporal(!tvTransportistes.getSelectionModel().getSelectedItem().isActivaBaixaTemporal());
                        tvTransportistes.getSelectionModel().getSelectedItem().setBaixatemporal(Utilitats.getDataActual());
                        helper.update(tvTransportistes.getSelectionModel().getSelectedItem());
                        tvTransportistes.refresh();
                        filtraTaula();
                    }
                }
            }
        });
    }

    /**
     * Metode que s'encarrega de crear la connexio a la base de dades i dir-li al formulari si esta en mode CERCA o en mode BAIXA
     * per modificar els parametres del formulari
     * S'utilitza el metode actualitzaTaula() per agafar tots els registres de la base de dades i posar-los al formulari
     */
    @Override               // PRIMER HA DE CREAR LA CONEXIÓ AMB LA CLASSE TRANSPORTISTES PER AIXÒ ENTRA QUAN LI DIU DESDE CONTROLADOR
    public void inicia() {
        //creo la conexió a bbdd i li dic a quina taula ha de preguntatr
        helper = new DAOHelper<>(getEmf(), Transportistes.class);
        switch (mode) {
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

    /**
     * @param row Fila de la taula a la que se li vol posar el focus
     *
     * Metode per posar el focus a una filera del formulari a la que se li ha fet clic o s'ha obert el formulari de modificacio (formulari d'alta modificat)
     */
        //dona el focus a la filera que li passem al mètode
    private void goTableItem(int row) {
        tvTransportistes.requestFocus();
        tvTransportistes.scrollTo(row);
        tvTransportistes.getSelectionModel().select(row);
        tvTransportistes.getFocusModel().focus(row);
    }

    /**
     * Metode per agafar els registres de la base de dades, posar-los a la taula i actualitzar la taula amb el metode filtraTaula()
     */
    private void actualitzaTaula() {
        tvTransportistes.setItems(helper.getAllToObservableList());
        filtraTaula();
    }

    /**
     * Metode per fer la cerca d'un transportista per nom o CIF
     */
    private void filtraTaula() {
        // 1.  Ajustar el ObservableList en una llista filtrada (inicialment mostra totes les dades).
        FilteredList<Transportistes> filteredData = new FilteredList<>(tvTransportistes.getItems(), p -> true);

        // 2. Estableix el Predicat del filtre cada vegada que camvia en filtre
        tfNomCIF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(transportistes -> {
                // Si el text del filtre està buit (null o empty), mostrar-ho tot
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (transportistes.getRaosocial().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // El filtre coincideix amb el nom del producte                            EXPLICACIÓ??
                } else if ((transportistes.getCif_nif().indexOf(lowerCaseFilter) != -1)) {
                    return true; // El filtre conincideix amb el codi del producte
                }else{
                    return false; // No hi ha coincidència
                }
            });
        });

        // 3. Embolica el FilteredList en una SortedList.
        SortedList<Transportistes> sortedData = new SortedList<>(filteredData);

        // 4. Lliga el comparador de la SortedList al del TableView
        // 	  En qualsevol altre cas, l'ordenació de la TableView no tindrà efecte
        sortedData.comparatorProperty().bind(tvTransportistes.comparatorProperty());

        // 5. Afegeix la informació ordenada (i filtrada) a la taula
        tvTransportistes.setItems(sortedData);
    }

    /**
     * Metode per omplir les columnes de la taula CercaTransportistes
     */
//omple les columnes de la taula transportistes
    private void configuraColumnes() {
        System.out.println("estic a configura columnes");
        colRaosocial.setCellValueFactory(new PropertyValueFactory<>("raosocial"));
        colCIFNIF.setCellValueFactory(new PropertyValueFactory<>("cif_nif"));
        colCorreuTransp.setCellValueFactory(new PropertyValueFactory<>("mail_transp"));
        colTelefon.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        colCertificathisenda.setCellValueFactory(new PropertyValueFactory<>("cert_hisenda"));
        colCertificatprl.setCellValueFactory(new PropertyValueFactory<>("cert_prl"));
        colAssegurancamerc.setCellValueFactory(new PropertyValueFactory<>("asseguranca_merc"));
        colAssegurancarc.setCellValueFactory(new PropertyValueFactory<>("asseguranca_rc"));
        colBaixaTemporal.setCellFactory(column -> new CheckBoxTableCell());
        colBaixaTemporal.setCellValueFactory(dadesCella -> {
            Transportistes valorCella = dadesCella.getValue();
            BooleanProperty property = new SimpleBooleanProperty();
            property.setValue(valorCella.isActivaBaixaTemporal());
            return property;
        });
    }

    /**
     * @param actionEvent Parametre que fa referencia a fer clic sobre el boto de modifica o al fer doble clic amb el boto primari
     * del ratoli sobre una fila
     *
     * Metode que utilitza el metode accionsPerModificar() per obrir el formulari de modificacio
     */
    //mètode que obre vista de modificar
    @FXML
    public void btModificaOnAction(ActionEvent actionEvent) {

        accionsPerModificar(mode);
    }

    /**
     * @param cr Parametre de tipus TipusCerca
     * @throws IOException
     *
     * Metode que carrega el formulari d'AltaTransportistes a mode de modificacio de transportistes
     */
    private void accionsPerModificar(TipusCerca cr) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Alta_transportistes.fxml"));
            Parent root = loader.load();
            AltaTransportistesController controller = loader.getController();
            controller.setEmf(getEmf());
            controller.setMode(mode);
            controller.setTransportista(tvTransportistes.getSelectionModel().getSelectedItem());
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

    public TipusCerca getMode() {
        return mode;
    }

    public void setMode(TipusCerca mode) {
        this.mode = mode;
    }

    /**
     * @param actionEvent Parametre referent a fer clic al boto
     *
     * Metode que elimina un transportista de la base de dades utilitzant una alerta amb dos botons
     */
    // si clica baixa física surt alerta per assegurar que no fem disbarat
    public void btBaixaFisicaOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Constants.APP_NAME);
        alert.setHeaderText(Constants.MSG_ADV_CONFIRMAR_ACCIO);
        alert.setContentText(Constants.MSG_ADV_ESTAS_SEGUR_ELIMINAR + " al transportista: \n\n [ " +
                tvTransportistes.getSelectionModel().getSelectedItem().getRaosocial() + " ]");

        ButtonType btNo = new ButtonType(Constants.BTN_NO);
        ButtonType btSi = new ButtonType(Constants.BTN_SI);
        alert.getButtonTypes().setAll(btNo, btSi);

        Optional<ButtonType> result = alert.showAndWait(); //ESPERA A QUE APRETIS BOTÓ PER FER ALERTA
        // si cliquem SI envia ordre a bbdd per esborrar fila
        if (result.get() == btSi) {
            int itemActual = tvTransportistes.getSelectionModel().getSelectedIndex();
            helper.delete(tvTransportistes.getSelectionModel().getSelectedItem().getId());
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

    /**
     * @param actionEvent Parametre que fa referencia al boto
     * @throws IOException
     *
     * Metode que obre el formulari de modificacio (formulari d'alta modificat) per donar baixes temporals o treure-les
     */
    //obre pantalla modifica per donar alta o baixa temporal!!!
    @FXML
    public void btBaixaTemporalOnAction(ActionEvent actionEvent) {
        try {
            tvTransportistes.getSelectionModel().getSelectedItem().setActivaBaixaTemporal(!tvTransportistes.getSelectionModel().getSelectedItem().isActivaBaixaTemporal());
            tvTransportistes.getSelectionModel().getSelectedItem().setBaixatemporal(Utilitats.getDataActual());
            helper.update(tvTransportistes.getSelectionModel().getSelectedItem());
            tvTransportistes.refresh();
            filtraTaula();

        } catch (Exception ex) {

            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al donar de baixa ", ex);
        }
    }

}
