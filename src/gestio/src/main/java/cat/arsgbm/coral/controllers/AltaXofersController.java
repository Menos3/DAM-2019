package cat.arsgbm.coral.controllers;

import cat.arsgbm.coral.classes.*;
import cat.arsgbm.coral.exceptions.ClauDuplicadaException;
import cat.arsgbm.coral.model.Transportistes;
import cat.arsgbm.coral.model.Xofers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Date;

public class AltaXofersController extends DefaultDBController {
    private TipusCerca mode;
    private DAOHelper<Xofers> helper;
    private Xofers xofer;
    private String rutaFitxer;

    @FXML
    Label lbTitular;

    @FXML
    ComboBox cmbTransportista;

    @FXML
    TextField tfNom, tfCognom, tfNumIntern, tfTelefon, tfCorreuXofer;

    @FXML
    DatePicker dpCarnetConduir, dpCarnetADR, dpCarnetCarreter;

    @FXML
    Button btDonaralta, btGuardarCarnetXofer, btGuardarCarnetADR, btGuardarCarnetCarreter;

    @FXML
    CheckBox cbEmprempta, cbActiva_Baixa_Temporal;

    /**
     * Metode que inicia una connexio a la base de dades i s'encarrega de carregar les dades en el ComboBox
     */
    @Override
    public void inicia() {
        helper = new DAOHelper<>(getEmf(), Xofers.class);
        switch (getMode()) {
            case CERCA:
                accioCerca();
                break;
            case ALTA:
                accioAlta();
                break;
        }
        DAOHelper<Transportistes> tmpHelper = new DAOHelper<>(getEmf(), Transportistes.class);
        cmbTransportista.setItems(tmpHelper.getAllToObservableList());
    }

    /**
     * Metode per modificar parametres del formulari de cerca (formulari d'alta modificat), introdueix les dades d'un xofer i comprova si
     * hi ha un fitxer guardat en la carpeta de Xofer
     */
    private void accioCerca() {//Omple Camps
        cmbTransportista.getSelectionModel().select(xofer);
        lbTitular.setText(Constants.MSG_TITOL_ALTA_XOFERS);
        btDonaralta.setText(Constants.BTN_GUARDAR);
        tfNom.setText(xofer.getNomXofer());
        tfCognom.setText(xofer.getCognomXofer());
        tfNumIntern.setText(xofer.getNumIntern());
        tfCorreuXofer.setText(xofer.getCorreuXofer());
        tfTelefon.setText(xofer.getTelefon());

        if (xofer.getCarnetConduir() != null)
            dpCarnetConduir.setValue(Utilitats.dataToLocalDate(xofer.getCarnetConduir()));

        if (xofer.getCarnetAdr() != null)
            dpCarnetADR.setValue(Utilitats.dataToLocalDate(xofer.getCarnetAdr()));

        if (xofer.getCarnetCarreter() != null)
            dpCarnetCarreter.setValue(Utilitats.dataToLocalDate(xofer.getCarnetCarreter()));

        cbEmprempta.setSelected(xofer.isEmpremta());
        cbActiva_Baixa_Temporal.setSelected(xofer.isActivaBaixaTemporal());
    }

    /**
     * Metode que modifica els parametres del formulari d'alta de xofers
     */
    private void accioAlta() {
        lbTitular.setText(Constants.MSG_TITOL_ALTA_XOFERS);
        btDonaralta.setText(Constants.BTN_NOU_REGISTRE);
        cbActiva_Baixa_Temporal.setVisible(false);

    }

    /**
     * @param xofer Parametre de tipus Xofers
     * @return el objecte omplert amb dades o modificat
     *
     * Metode per obtenir les dades del formulari de modificacio o d'alta
     * Es rep un objecte de la classe Xofers, ja sigui buit o amb dades, s'actualitza i es retorna per fer un INSERT o un UPDATE
     */
    private Xofers adapta(Xofers xofer) {
        xofer.setTransportista((Transportistes) cmbTransportista.getSelectionModel().getSelectedItem());
        xofer.setNomXofer(tfNom.getText());
        xofer.setCognomXofer(tfCognom.getText());
        xofer.setNumIntern(tfNumIntern.getText());
        xofer.setCorreuXofer(tfCorreuXofer.getText());
        xofer.setTelefon(tfTelefon.getText());

        if (dpCarnetConduir.getValue() != null) {
            xofer.setCarnetConduir(Date.from(dpCarnetConduir.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            xofer.setCarnetConduir(null);
        }

        if (dpCarnetADR.getValue() != null) {
            xofer.setCarnetAdr(Date.from(dpCarnetADR.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            xofer.setCarnetAdr(null);
        }

        if (dpCarnetCarreter.getValue() != null) {
            xofer.setCarnetCarreter(Date.from(dpCarnetCarreter.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            xofer.setCarnetCarreter(null);
        }

        xofer.setEmpremta(cbEmprempta.isSelected());
        return xofer;
    }

    /**
     * @param event Parametre referenciat a fer clic en el boto
     *
     * Metode que pertany al boto del formulari de donar d'alta o el de modificar i s'encarrega de donar permis per fer el INSERT o el UPDATE
     * Al fer clic al boto, s'executa el metode de les alertes. Si no hi ha cap alerta emergent, es dona permis al metode que fa el INSERT
     * o el UPDATE per executar-lo a la base de dades i al acabar, tanca el formulari.
     */
    @FXML
    public void btDonaraltaOnAction(ActionEvent event) {
        if (testDadesCorrectes()) {
            boolean tancar = false;
            switch (mode) {
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
     * @return la comprovacio per donar permis per fer el INSERT o el UPDATE
     *
     * Metode per comprovar si els textfields i/o els ComboBox amb dades obligatories tenen dades introduides
     * Si els textfields o ComboBox no tenen dades, es retorna una serie d'alertes avisant al usuari i es canvia la variable booleana
     */
    private boolean testDadesCorrectes() {
        boolean result = true;
        if (cmbTransportista.getVisibleRowCount() == 0) {
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_XOFERS, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_TRANSPASSOCIAT);
            cmbTransportista.requestFocus();
        } else if (tfNom.getLength() == 0) {
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_XOFERS, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_NOM);
            result = false;
            tfNom.requestFocus();
        } else if (tfCognom.getLength() == 0) {
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_XOFERS, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_COGNOM);
            result = false;
            tfCognom.requestFocus();
        }

        return result;
    }

    /**
     * @return si el INSERT s'ha fet correctament o no
     * @throws Exception
     *
     * Metode que fa el INSERT a la base de dades si no hi ha cap problema
     * Es crea un objecte de tipus Xofers i s'utilitza el metode adapta() per omplir-lo de dades
     * S'intenta el INSERT a la base de dades i es controlen els possibles errors al insertar
     */
    private boolean guardarAlta() {
        Xofers x = adapta(new Xofers());
        try {
            helper.update(x);
            return true;
        } catch (Exception e) {

            e.printStackTrace();

            }

        return false;
    }

    /**
     * Metode per fer un UPDATE a la base de dades
     * S'utilitza un objecte de tipus Xofers amb dades, es passa al formulari de modificacio (formulari d'alta modificat),
     * s'actualitzen les dades amb el metode adapta() i finalment, es fa el UPDATE a la base de dades
     */
    private void guardarCerca() {
        adapta(xofer);
        helper.update(xofer);
    }

    /**
     * @param Event Parametre que fa referencia a fer clic en el boto
     *
     * Metode que s'utilitza per guardar el fitxer del carnet de conduir a la carpeta que s'ha especificat en el formulari de configuracio
     * S'utilitza el metode GuardarFitxerXofer() de la classe Utilitats per fer que guardi el fitxer i s'obte la ruta del fitxer sencera per fer
     * la comprovacio de si hi ha un fitxer ja existent o no canviant la icona en el formulari de modificacio (formulari d'alta modificat)
     */
    public void btGuardarCarnetXoferOnAction(ActionEvent Event) {
        if(mode == TipusCerca.CERCA){
            rutaFitxer = Utilitats.GuardarFitxerXofer((AppConfigSingleton.getInstancia().getDirDoc()), xofer.getTransportista()+"_carnetcotxe");
            btGuardarCarnetXofer.getStyleClass().clear();
            btGuardarCarnetXofer.getStyleClass().add("button-doc-guardar");
        }else{
            rutaFitxer = Utilitats.GuardarFitxerXofer((AppConfigSingleton.getInstancia().getDirDoc()), cmbTransportista.getId()+"_carnetcotxe");
            btGuardarCarnetXofer.getStyleClass().clear();
            btGuardarCarnetXofer.getStyleClass().add("button-doc-pujar");
        }
    }

    /**
     * @param Event Parametre que fa referencia a fer clic en el boto
     *
     * Metode que s'utilitza per guardar el fitxer del carnet ADR a la carpeta que s'ha especificat en el formulari de configuracio
     * S'utilitza el metode GuardarFitxerXofer() de la classe Utilitats per fer que guardi el fitxer i s'obte la ruta del fitxer sencera per fer
     * la comprovacio de si hi ha un fitxer ja existent o no canviant la icona en el formulari de modificacio (formulari d'alta modificat)
     */
    public void btGuardarCarnetADROnAction(ActionEvent Event) {
        if(mode == TipusCerca.CERCA){
            rutaFitxer = Utilitats.GuardarFitxerXofer((AppConfigSingleton.getInstancia().getDirDoc()), xofer.getTransportista()+"_carnetadr");
            btGuardarCarnetXofer.getStyleClass().clear();
            btGuardarCarnetXofer.getStyleClass().add("button-doc-guardar");
        }else{
            rutaFitxer = Utilitats.GuardarFitxerXofer((AppConfigSingleton.getInstancia().getDirDoc()), cmbTransportista.getId()+"_carnetadr");
            btGuardarCarnetXofer.getStyleClass().clear();
            btGuardarCarnetXofer.getStyleClass().add("button-doc-pujar");
        }

    }

    /**
     * @param Event Parametre que fa referencia a fer clic en el boto
     *
     * Metode que s'utilitza per guardar el fitxer del carnet ADR a la carpeta que s'ha especificat en el formulari de configuracio
     * S'utilitza el metode GuardarFitxerXofer() de la classe Utilitats per fer que guardi el fitxer i s'obte la ruta del fitxer sencera per fer
     * la comprovacio de si hi ha un fitxer ja existent o no canviant la icona en el formulari de modificacio (formulari d'alta modificat)
     */
    public void btGuardarCarnetCarreterOnAction(ActionEvent Event) {
        if(mode == TipusCerca.CERCA){
            rutaFitxer = Utilitats.GuardarFitxerXofer((AppConfigSingleton.getInstancia().getDirDoc()), xofer.getTransportista()+"_carnetcarreter");
            btGuardarCarnetXofer.getStyleClass().clear();
            btGuardarCarnetXofer.getStyleClass().add("button-doc-guardar");
        }else{
            rutaFitxer = Utilitats.GuardarFitxerXofer((AppConfigSingleton.getInstancia().getDirDoc()), cmbTransportista.getId()+"_carnetcarreter");
            btGuardarCarnetXofer.getStyleClass().clear();
            btGuardarCarnetXofer.getStyleClass().add("button-doc-pujar");
        }
    }

    public void setMode(TipusCerca mode){ this.mode = mode; }

    public TipusCerca getMode() { return mode; }

    public Xofers getXofer() {return xofer; }

    public void setXofer(Xofers xofer) { this.xofer = xofer; }

}
