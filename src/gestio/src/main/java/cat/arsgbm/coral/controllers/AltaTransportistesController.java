package cat.arsgbm.coral.controllers;

import cat.arsgbm.coral.classes.*;
import cat.arsgbm.coral.exceptions.ClauDuplicadaException;
import cat.arsgbm.coral.model.Transportistes;
import dam2.controls.limitedtextfield.LimitedTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Date;

public class AltaTransportistesController extends DefaultDBController {

    @FXML
    Label lbTitular;
    @FXML
    TextField tfRaosocial, tfCorreuTransp, tfTelefon;
    @FXML
    LimitedTextField tfCIFNIF;
    @FXML
    DatePicker dpCertificathisenda, dpCertificatprl, dpAssegurancamerc, dpAssegurancarc;
    @FXML
    Button btDonaralta, btGuardarCH, btGuardarCPRL, btGuardarAM, btGuardarARC;
    @FXML
    CheckBox cbTrade, cbActiva_Baixa_Temporal;

    private TipusCerca mode;
    private DAOHelper<Transportistes> helper;
    private Transportistes transp;
    private String rutaFitxer;

    /**
     * Metode de la interficie inicia que s'encarrega de passar la connexio i la classe amb la que es treballara i mirar si s'esta
     * en el formulari de cerca (en el que es modifiquen les dades) o en el formulari d'alta (en el que l'objecte esta buit i vols fer un
     * registre nou a la taula de la base de dades)
     *
     */
    @Override
    public void inicia() {
        helper = new DAOHelper<>(getEmf(), Transportistes.class);
        switch (getMode()){
            case CERCA: accioCerca(); break;
            case ALTA: accioAlta(); break;        }

    }

    /**
     * Metode que monta i introdueix les dades al formulari de Cerca (Modificacio) i comprova si ja hi ha un arxiu guardat
     * en la carpeta de Transportistes (aspecte visual)
     *
     */
    private void accioCerca() {
        lbTitular.setText(Constants.MSG_TITOL_MODIFICA_TRANSPORTISTES);
        btDonaralta.setText(Constants.BTN_GUARDAR);
        tfRaosocial.setText(transp.getRaosocial());
        tfCIFNIF.setText(transp.getCif_nif());
        tfCorreuTransp.setText(transp.getMail_transp());
        tfTelefon.setText(transp.getTelefon());

        if (transp.getCert_hisenda() != null)
            dpCertificathisenda.setValue(Utilitats.dataToLocalDate(transp.getCert_hisenda()));

        if (transp.getCert_prl() != null)
            dpCertificatprl.setValue(Utilitats.dataToLocalDate(transp.getCert_prl()));

        if (transp.getAsseguranca_merc() != null)
            dpAssegurancamerc.setValue(Utilitats.dataToLocalDate(transp.getAsseguranca_merc()));


        if (transp.getAsseguranca_rc() != null)
            dpAssegurancarc.setValue(Utilitats.dataToLocalDate(transp.getAsseguranca_rc()));

        cbTrade.setSelected(transp.isTrade());
        cbActiva_Baixa_Temporal.setDisable(true);
        // TODO: Gestionar els botons

        //String rutaCH = AppConfigSingleton.getInstancia().getDirDoc() + File.separator + "Transportistes" + File.separator + transp.getCif_nif() + "certificathisenda";
        //File f = new File(rutaCH);
        //if (f.exists()){

        if (transp.getPathCertHisenda() != null) {
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-guardar");
        }else{
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-pujar");
        }

        if (true) {
            btGuardarCPRL.getStyleClass().clear();
            btGuardarCPRL.getStyleClass().add("button-doc-guardar");
        }else{
            btGuardarCPRL.getStyleClass().clear();
            btGuardarCPRL.getStyleClass().add("button-doc-pujar");
        }

        if (true) {
            btGuardarAM.getStyleClass().clear();
            btGuardarAM.getStyleClass().add("button-doc-guardar");
        }else{
            btGuardarAM.getStyleClass().clear();
            btGuardarAM.getStyleClass().add("button-doc-pujar");
        }

        if(true) {
            btGuardarARC.getStyleClass().clear();
            btGuardarARC.getStyleClass().add("button-doc-guardar");
        }else{
            btGuardarARC.getStyleClass().clear();
            btGuardarARC.getStyleClass().add("button-doc-pujar");
        }
        /*
        String extensio;
        if(setPathCertHisenda != null){
            extensio = obtenirExtensio(rutaCertificatPRL);
            arxiuCertificathisenda = transp.getCif_nif() + extensio;
            transp.setArxiuHisenda(arxiuCertificathisenda);}
*/
    }

    /**
     * Metode que monta el formulari d'alta de TRansportistes (aspecte visual)
     *
     */
    private void accioAlta() {
        lbTitular.setText(Constants.MSG_TITOL_ALTA_TRANSPORTISTES);
        btDonaralta.setText(Constants.BTN_NOU_REGISTRE);
        cbActiva_Baixa_Temporal.setVisible(false);
    }

    /**
     * @param transportista  Parametre de la classe Transportistes
     * @return l'objecte buit omplert de dades o l'objecte actualitzat
     *
     * Metode per obtenir les dades del formulari de modificacio o d'alta
     * Es rep un objecte de la classe Transportistes, ja sigui buit o amb dades, s'actualitza i es retorna per fer un INSERT o un UPDATE
     *
     */
    private Transportistes adapta(Transportistes transportista) {
        transportista.setRaosocial(tfRaosocial.getText());
        transportista.setCif_nif(tfCIFNIF.getText());
        transportista.setMail_transp(tfCorreuTransp.getText());
        transportista.setTelefon(tfTelefon.getText());

        if (dpCertificathisenda.getValue() != null) {
            transportista.setCert_hisenda(Date.from(dpCertificathisenda.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }else{
            transportista.setCert_hisenda(null);
        }

        if (dpCertificatprl.getValue() != null){
            transportista.setCert_prl(Date.from(dpCertificatprl.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }else{
            transportista.setCert_prl(null);
        }

        if (dpAssegurancamerc.getValue() != null) {
            transportista.setAsseguranca_merc(Date.from(dpAssegurancamerc.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }else{
            transportista.setAsseguranca_merc(null);
        }

        if(dpAssegurancarc.getValue() != null) {
            transportista.setAsseguranca_rc(Date.from(dpAssegurancarc.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        }else{
            transportista.setAsseguranca_rc(null);
        }

        if (transportista.getPathCertHisenda() != null) {
            transportista.setPathCertHisenda(rutaFitxer);
        }else{
            transportista.setPathCertHisenda(null);
        }

        if (transportista.getPathCertPrl() != null) {
            transportista.setPathCertPrl(rutaFitxer);
        }else {
            transportista.setPathCertPrl(null);
        }

        if (transportista.getPathAssegurancaMerc() != null) {
            transportista.setPathAssegurancaMerc(rutaFitxer);
        }else{
            transportista.setPathAssegurancaMerc(null);
        }

        if (transportista.getPathAssegurancaRc() != null) {
            transportista.setPathAssegurancaRc(rutaFitxer);
        }else{
            transportista.setPathAssegurancaRc(null);
        }

        transportista.setTrade(cbTrade.isSelected());
        return transportista;
    }

    /**
     * @param event  Parametre referenciat a fer clic en el boto
     *
     * Metode que pertany al boto del formulari de donar d'alta i s'encarrega de donar permis per fer el INSERT o el UPDATE
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
     * Metode per comprovar si els textfields amb dades obligatories tenen dades introduides
     * Si els textfields no tenen dades, es retorna una serie d'alertes avisant al usuari i es canvia la variable booleana
     *
     */
    private boolean testDadesCorrectes() {
        boolean result = true;

        if (tfRaosocial.getLength() == 0) {
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_TRANSPORTISTES, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_RAO_SOCIAL);
            result = false;
            tfRaosocial.requestFocus();
        } else if (tfCIFNIF.getLength() == 0) {
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_TRANSPORTISTES, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_CIFNIF);
            result = false;
            tfCIFNIF.requestFocus();
        } else if (tfCorreuTransp.getLength() == 0) {
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_TRANSPORTISTES, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_CORREUTRANSPORTISTA);
            result = false;
            tfCorreuTransp.requestFocus();

        }

        return  result;
    }

    /**
     * @return si el INSERT s'ha fet correctament o no
     * @throws ClauDuplicadaException Excepcio creada per controlar la duplicitat en la base de dades
     *
     * Metode que fa el INSERT a la base de dades si no hi ha cap problema
     * Es crea un objecte de tipus Transportista i s'utilitza el metode adapta() per omplir-lo de dades
     * S'intenta el INSERT a la base de dades i es controla que el CIF/NIF no es dupliqui
     *
     */
    private boolean guardarAlta() {
        Transportistes t = adapta(new Transportistes());
        try {
            helper.insert(t);
            return true;
        } catch (Exception e) {
            if (e instanceof ClauDuplicadaException) {
                Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTA_TRANSPORTISTES, Constants.MSG_ADV_CAMP_DUPLICAT_BUIT, Constants.MSG_CAMP_CIFNIF_DUPLICAT);
                tfCIFNIF.requestFocus();
                System.out.println("Clau Duplicada");
            }
        }
        return false;
    }

    /**
     * Metode per fer un UPDATE a la base de dades
     * S'utilitza un objecte de tipus Transportista amb dades, es passa al formulari de modificacio (formulari d'alta modificat),
     * s'actualitzen les dades amb el metode adapta() i finalment, es fa el UPDATE a la base de dades
     */
    private void guardarCerca() {
        adapta(transp);
        helper.update(transp);
    }

    /**
     * @param event Parametre referenciat a fer clic al boto
     *
     * Metode que s'utilitza per guardar el fitxer del certificat d'hisenda a la carpeta que s'ha especificat en el formulari de configuracio
     * S'utilitza el metode GuardarFitxerTransportista() de la classe Utilitats per fer que guardi el fitxer i s'obte la ruta del fitxer sencera per fer
     * la comprovacio de si hi ha un fitxer ja existent o no canviant la icona en el formulari de modificacio (formulari d'alta modificat)
     */
    public void btGuardarCHOnAction(ActionEvent event) {

        if(mode == TipusCerca.CERCA){
            rutaFitxer = Utilitats.GuardarFitxerTransportista((AppConfigSingleton.getInstancia().getDirDoc()), transp.getCif_nif()+"_certificathisenda");
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-guardar");
        }else{
            rutaFitxer = Utilitats.GuardarFitxerTransportista((AppConfigSingleton.getInstancia().getDirDoc()), tfCIFNIF.getText()+"_certificathisenda");
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-pujar");
        }
    }

    /**
     * @param event Parametre referenciat a fer clic al boto
     *
     * Metode que s'utilitza per guardar el fitxer del certificat prl a la carpeta que s'ha especificat en el formulari de configuracio
     * S'utilitza el metode GuardarFitxerTransportista() de la classe Utilitats per fer que guardi el fitxer i s'obte la ruta del fitxer sencera per fer
     * la comprovacio de si hi ha un fitxer ja existent o no canviant la icona en el formulari de modificacio (formulari d'alta modificat)
     */
    public void btGuardarCPRLOnAction(ActionEvent event) {
        if(mode == TipusCerca.CERCA){
            rutaFitxer = Utilitats.GuardarFitxerTransportista((AppConfigSingleton.getInstancia().getDirDoc()),transp.getCif_nif()+"_certificatprl");
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-guardar");
        }else{
            rutaFitxer = Utilitats.GuardarFitxerTransportista((AppConfigSingleton.getInstancia().getDirDoc()),tfCIFNIF.getText()+"_certificatprl");
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-pujar");
        }
    }

    /**
     * @param event Parametre referenciat a fer clic al boto
     *
     * Metode que s'utilitza per guardar el fitxer de l'assegurança de mercaderies a la carpeta que s'ha especificat en el formulari de configuracio
     * S'utilitza el metode GuardarFitxerTransportista() de la classe Utilitats per fer que guardi el fitxer i s'obte la ruta del fitxer sencera per fer
     * la comprovacio de si hi ha un fitxer ja existent o no canviant la icona en el formulari de modificacio (formulari d'alta modificat)
     */
    public void btGuardarAMOnAction(ActionEvent event) {
        if(mode == TipusCerca.CERCA){
            rutaFitxer = Utilitats.GuardarFitxerTransportista((AppConfigSingleton.getInstancia().getDirDoc()),transp.getCif_nif()+"_certificatam");
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-guardar");
        }else{
            rutaFitxer = Utilitats.GuardarFitxerTransportista((AppConfigSingleton.getInstancia().getDirDoc()),tfCIFNIF.getText()+"_certificatam");
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-pujar");
        }
    }

    /**
     *
     * @param event Parametre referenciat a fer clic al boto
     *
     * Metode que s'utilitza per guardar el fitxer de l'assegurança de responsabilitat civil a la carpeta que s'ha especificat en el formulari de configuracio
     * S'utilitza el metode GuardarFitxerTransportista() de la classe Utilitats per fer que guardi el fitxer i s'obte la ruta del fitxer sencera per fer
     * la comprovacio de si hi ha un fitxer ja existent o no canviant la icona en el formulari de modificacio (formulari d'alta modificat)
     */
    public void btGuardarARCOnAction(ActionEvent event) {
        if(mode == TipusCerca.CERCA) {
            rutaFitxer = Utilitats.GuardarFitxerTransportista((AppConfigSingleton.getInstancia().getDirDoc()),transp.getCif_nif()+"_certificatarc");
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-guardar");
        }else{
            rutaFitxer = Utilitats.GuardarFitxerTransportista((AppConfigSingleton.getInstancia().getDirDoc()),tfCIFNIF.getText()+"_certificatarc");
            btGuardarCH.getStyleClass().clear();
            btGuardarCH.getStyleClass().add("button-doc-pujar");
        }
    }

    private String obtenirExtensio(String nomArxiu){

        return "." + nomArxiu.substring(nomArxiu.lastIndexOf('.') + 1);
    }


    public Transportistes getTransportista() {
        return transp;
    }

    public void setTransportista(Transportistes transportista) {
        this.transp = transportista;
    }

    public TipusCerca getMode() { return mode; }

    public void setMode(TipusCerca mode) { this.mode = mode; }
}