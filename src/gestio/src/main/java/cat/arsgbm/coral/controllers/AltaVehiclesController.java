package cat.arsgbm.coral.controllers;

import cat.arsgbm.coral.classes.*;
import cat.arsgbm.coral.exceptions.ClauDuplicadaException;
import cat.arsgbm.coral.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Date;

import cat.arsgbm.coral.classes.DefaultDBController;

public class AltaVehiclesController extends DefaultDBController {

    @FXML
    Label lbTitular;

    @FXML
    ComboBox cmbTransportista,cmbTipusTargeta,cmbTipusVehicle;
    @FXML
    TextField tfMatricula;

    @FXML
    DatePicker dpItv, dpAsseguranca, dpTargetaTransport;
    @FXML
    Button btDonarAlta, btGuardaTargetaTransport, btGuardaAsseguranca;
    @FXML
    CheckBox cbLlogat, cbActiva_Baixa_Temporal;

    private DAOHelper<Vehicles> helper;
    private Transportistes transp;
    private Vehicles vehicle;
    private String rutaFitxer;

    /**
     * Metode que inicia una connexio a la base de dades i es l'encarregat de carregar les dades en els ComboBox
     */
    @Override
    public void inicia() {
        helper = new DAOHelper<>(getEmf(), Vehicles.class);

        switch (getMode()) {
            case CERCA:
                accioCerca();
                break;
            case ALTA:
                accioAlta();
                break;
        }
        //CARREGO ELS COMBOBOX AMB LLISTA DE LES TAULES I EL SEU PROPI HELPER PER CADA TAULA QUE CORRESPON A UN COMBOBOX
        DAOHelper<Transportistes> tmpHelper = new DAOHelper<>(getEmf(), Transportistes.class);
        cmbTransportista.setItems(tmpHelper.getAllToObservableList());

        DAOHelper<TipusVehicle> tipusVehicleDAOHelper = new DAOHelper<>(getEmf(), TipusVehicle.class);
        cmbTipusVehicle.setItems(tipusVehicleDAOHelper.getAllToObservableList());

        DAOHelper<TipusTargeta> tipusTargetaDAOHelper = new DAOHelper<>(getEmf(), TipusTargeta.class);
        cmbTipusTargeta.setItems(tipusTargetaDAOHelper.getAllToObservableList());
    }

    /**
     * Metode per modificar parametres del formulari de cerca (formulari d'alta modificat), introdueix les dades d'un vehicle i comprova si
     * hi ha un fitxer guardat en la carpeta de Vehicles
     */
    private void accioCerca() {
        //CONFIGURO BOTONS I TEXT PER CERCA
        lbTitular.setText(Constants.MSG_TITOL_MODIFICA_TRANSPORTISTES);
        btDonarAlta.setText(Constants.BTN_GUARDAR);
        tfMatricula.setText(vehicle.getMatricula());

// OMPLO ELS CAMPS AMB ELS VALORS QUE AGAFO DE OBJECTE VEHICLE AMB ELS GETTERS

        if (vehicle.getItv() != null)
            dpItv.setValue(Utilitats.dataToLocalDate(vehicle.getItv()));

        if (vehicle.getAssegurancaVehicle() != null)
            dpAsseguranca.setValue(Utilitats.dataToLocalDate(vehicle.getAssegurancaVehicle()));

        if (vehicle.getTargetaTransport() != null)
            dpTargetaTransport.setValue(Utilitats.dataToLocalDate(vehicle.getTargetaTransport()));


        cbLlogat.setSelected(vehicle.isLlogat());
        cbActiva_Baixa_Temporal.setDisable(true);
        // TODO: Gestionar els botons

        //String rutaCH = AppConfigSingleton.getInstancia().getDirDoc() + File.separator + "Transportistes" + File.separator + transp.getCif_nif() + "certificathisenda";
        //File f = new File(rutaCH);
        //if (f.exists()){

        if (vehicle.getPathAssegurancaVehicle() != null) {
            btGuardaAsseguranca.getStyleClass().clear();
            btGuardaAsseguranca.getStyleClass().add("button-doc-guardar");
        }else{
            btGuardaAsseguranca.getStyleClass().clear();
            btGuardaAsseguranca.getStyleClass().add("button-doc-pujar");
        }

        if (vehicle.getPathTargetaTransport() !=null) {
            btGuardaTargetaTransport.getStyleClass().clear();
            btGuardaTargetaTransport.getStyleClass().add("button-doc-guardar");
        }else{
            btGuardaTargetaTransport.getStyleClass().clear();
            btGuardaTargetaTransport.getStyleClass().add("button-doc-pujar");
        }

    }

    /**
     * Metode que modifica els parametres del formulari d'alta de vehicles
     */
    private void accioAlta() {
        lbTitular.setText(Constants.MSG_TITOL_ALTA_TRANSPORTISTES);
        btDonarAlta.setText(Constants.BTN_NOU_REGISTRE);
        cbActiva_Baixa_Temporal.setVisible(false);
    }

    /**
     * @param v Parametre de tipus Vehicle
     * @return el objecte omplert amb dades o modificat
     *
     * Metode per obtenir les dades del formulari de modificacio o d'alta
     * Es rep un objecte de la classe Vehicles, ja sigui buit o amb dades, s'actualitza i es retorna per fer un INSERT o un UPDATE
     */
    //CREO UN OBJECTE VEHICLE AMB ELS VALORS SELECCIONATS DEL FORMULARI QUE JA SÓN CORRECTES PER QUE HE PASSAT EL testDadesCorrectes()
    private Vehicles adaptaVehicle(Vehicles v) {
        v.setTransportista((Transportistes) cmbTransportista.getSelectionModel().getSelectedItem());
        v.setTipusVehicle((TipusVehicle) cmbTipusVehicle.getSelectionModel().getSelectedItem());
        v.setTipusTargeta((TipusTargeta) cmbTipusTargeta.getSelectionModel().getSelectedItem());
        v.setMatricula(tfMatricula.getText());

        if (dpItv.getValue() != null) {
            v.setItv(Date.from(dpItv.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            v.setItv(null);
        }

        if (dpTargetaTransport.getValue() != null) {
            v.setTargetaTransport(Date.from(dpTargetaTransport.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            v.setTargetaTransport(null);
        }

        if (dpAsseguranca.getValue() != null) {
            v.setAssegurancaVehicle(Date.from(dpAsseguranca.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            v.setAssegurancaVehicle(null);
        }

        v.setActivaBaixaTemporal(cbActiva_Baixa_Temporal.isSelected());
        v.setLlogat(cbLlogat.isSelected());
        return v;
    }

    /**
     * @return la comprovacio per donar permis per fer el INSERT o el UPDATE
     *
     * Metode per comprovar si els textfields i/o els ComboBox amb dades obligatories tenen dades introduides
     * Si els textfields o ComboBox no tenen dades, es retorna una serie d'alertes avisant al usuari i es canvia la variable booleana
     */
//MÈTODE QUE COMPROVA ELS CAMPS OBLIGATORIS I DONA ALERTA I FOCUS SI NO ESTAN PLENS
    private boolean testDadesCorrectes() {

        boolean result = true;

        if (cmbTransportista.getSelectionModel().isEmpty()) {

            System.out.println("transportista buit");
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_XOFERS, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_TRANSPASSOCIAT);
            cmbTransportista.requestFocus();
            result=false;

        }else if (cmbTipusVehicle.getSelectionModel().isEmpty()) {

            System.out.println("tVehicle buit");
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_XOFERS, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_TIPUS_VEHICLE);
            cmbTipusVehicle.requestFocus();
            result=false;

        }else if (cmbTipusTargeta.getSelectionModel().isEmpty()) {

            System.out.println("tTargeta buit");
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_XOFERS, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_TIPUS_TARGETA);
            cmbTipusTargeta.requestFocus();
            result=false;

        }else if (tfMatricula.getLength() == 0) {

            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_XOFERS, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_MATRICULA);
            System.out.println("matricula buida ");
            result = false;
            tfMatricula.requestFocus();
        }

        return result;
    }

    /**
     * @param event Parametre referenciat a fer clic en el boto
     *
     * Metode que pertany al boto del formulari de donar d'alta o el de modificar i s'encarrega de donar permis per fer el INSERT o el UPDATE
     * Al fer clic al boto, s'executa el metode de les alertes. Si no hi ha cap alerta emergent, es dona permis al metode que fa el INSERT
     * o el UPDATE per executar-lo a la base de dades i al acabar, tanca el formulari.
     */
    @FXML
    public void btVehicleAltaOnAction(ActionEvent event) {

       boolean guardaAlt= testDadesCorrectes();

        if (guardaAlt) {

            boolean tancar = false;
            switch (getMode()) {
                case ALTA:
                    tancar = guardarAlta();
                    cmbTransportista.requestFocus();
                    break;
                case CERCA:
                    guardarCerca();
                    break;
            }
            if (tancar)
                ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        }
    }

    /**
     * @return si el INSERT s'ha fet correctament o no
     * @throws ClauDuplicadaException Excepcio creada per controlar la duplicitat en la base de dades
     *
     * Metode que fa el INSERT a la base de dades si no hi ha cap problema
     * Es crea un objecte de tipus Vehicle i s'utilitza el metode adaptaVehicle() per omplir-lo de dades
     * S'intenta el INSERT a la base de dades i es controla que la matricula no es dupliqui
     */
    private boolean guardarAlta() {
        Vehicles vehicle = adaptaVehicle(new Vehicles());
        try {
            // POSO UPDATE PER QUE ESTIC ACTUALITZANT UN TRANSPORTISTA , PER QUE VEHICLE PENJA DE TRANSPORTISTA,
            // NO CREO , AFEGEIXO , PÈR TANT UPDATE
            helper.update(vehicle);
            return true;
        } catch (Exception e) {
            if (e instanceof ClauDuplicadaException) {
                Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_XOFERS, Constants.MSG_ADV_CAMP_DUPLICAT_BUIT, Constants.MSG_CAMP_MATRICULA_DUPLICADA);
                tfMatricula.requestFocus();
                System.out.println("Clau Duplicada");
            }
        }
        return false;
    }

    /**
     * Metode per fer un UPDATE a la base de dades
     * S'utilitza un objecte de tipus Vehicle amb dades, es passa al formulari de modificacio (formulari d'alta modificat),
     * s'actualitzen les dades amb el metode adaptaVehicles() i finalment, es fa el UPDATE a la base de dades
     */
    private void guardarCerca() {
        adaptaVehicle(vehicle);
        helper.update(vehicle);
    }
}