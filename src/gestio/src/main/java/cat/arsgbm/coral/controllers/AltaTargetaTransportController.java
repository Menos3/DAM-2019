package cat.arsgbm.coral.controllers;

import cat.arsgbm.coral.classes.Constants;
import cat.arsgbm.coral.classes.DAOHelper;
import cat.arsgbm.coral.classes.DefaultDBController;
import cat.arsgbm.coral.classes.Utilitats;
import cat.arsgbm.coral.exceptions.ClauDuplicadaException;
import cat.arsgbm.coral.model.TipusTargeta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AltaTargetaTransportController extends DefaultDBController {

    @FXML
    TextField tfNomTargeta;
    private DAOHelper<TipusTargeta> helper;

    /**
     * Metode de la interficie inicia que s'encarrega de passar la connexio de la base de dades i la classe amb la que es treballara
     *
     */
    @Override
    public void inicia() {
        helper = new DAOHelper<>(getEmf(), TipusTargeta.class);
    }

    /**
     * @param tg  Objecte de la classe TipusTargeta
     * @return el objecte actualitzat
     *
     * Metode per obtenir les dades del formulari de modificacio o d'alta
     * Es rep un objecte de la classe TipusTargeta, ja sigui buit o amb dades, s'actualitza i es retorna per fer un INSERT o u UPDATE
     *
     */
    private TipusTargeta adaptaTargeta (TipusTargeta tg) {

        tg.setNomTargeta(tfNomTargeta.getText());
        return tg;
    }

    /**
     * @return la comprovacio per donar permis per fer el INSERT o el UPDATE
     *
     * Metode per comprovar si el textfield amb dades obligatories no te dades introduides
     * Si el textfield no te dades, es retorna una alerta avisant al usuari i es canvia la variable booleana
     */
    private boolean testDadesCorrectes() {
        boolean result = true;

        if (tfNomTargeta.getLength() == 0) {
            Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTATIPUSTARGETA, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_CAMP_NOM_TARGETA);
            result = false;
            tfNomTargeta.requestFocus();
        }

        return result;
    }

    /**
     * @return la comprovacio de si el INSERT s'ha fet correctament o no
     * @throws Exception
     *
     * Metode encarregat de fer el INSERT a la base de dades i de controlar que el nom de la targeta introduit no coincideixi amb cap que ja esta
     * guardat en la base de dades
     *
     */
    private boolean guardarAlta() {
        TipusTargeta tt = adaptaTargeta(new TipusTargeta());
        try{
            helper.insert(tt);
            return true;

        }catch (Exception e) {

            if (e instanceof ClauDuplicadaException) {
                Utilitats.AlertaGeneralWarning(Constants.MSG_TITOL_ALTATIPUSTARGETA, Constants.MSG_ADV_CAMP_DUPLICAT_BUIT, Constants.MSG_CAMP_NOMTARGETA_DUPLICAT);
                tfNomTargeta.requestFocus();
                System.out.println("Clau Duplicada");
            }
        }
        return false;
    }

    /**
     * @param event  Parametre referenciat a fer clic en el boto
     *
     * Metode que pertany al boto del formulari de donar d'alta i s'encarrega de donar permis per fer el INSERT
     * Al fer clic al boto, s'executa el metode de les alertes. Si no hi ha cap alerta emergent, es dona permis al metode que fa el INSERT
     * per executar-lo a la base de dades i al acabar, tanca el formulari.
     *
     */
    public void btDonarAltaOnAction(ActionEvent event) {

        boolean tancar = false;

        if (testDadesCorrectes()) {
            tancar = guardarAlta();
        }

        if (tancar) {
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        }
    }
}
