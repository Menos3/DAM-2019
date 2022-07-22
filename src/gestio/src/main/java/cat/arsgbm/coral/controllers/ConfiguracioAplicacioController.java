package cat.arsgbm.coral.controllers;

import cat.arsgbm.coral.classes.*;
import cat.arsgbm.coral.model.Config;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

//  implements inizializable , crea metode , run later per esperar a que estiguio creada la conexió
// Default controller crea la conexió per que totes les classes que la necessitin la utilitzin
public class ConfiguracioAplicacioController extends DefaultController implements Initializable {
    private boolean update = true;
    private DAOHelper<Config> helper;
    Config config;

    @FXML
    private TextArea taMissatgeAvis;
    @FXML
    private TextField tfCorreuRecepcio1;
    @FXML
    private TextField tfCorreuRecepcio2;
    @FXML
    private TextField tfDirectoriArrelDocs;
    @FXML
    private TextField tfMailResp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            /* aquí  dintre està el runLater que permetrà esperar fins que estigui la conexió creada per
            baixar Config de la BBDD sense que sigui null per falta de conexió */
            public void run() {
                //conectem a la base de dades desde DAOHelper amb la classe Config
                helper = new DAOHelper<>(getEmf(), Config.class);// DAOHelper conté el emf que fa la conexió
                List<Config> llista = helper.getAll();  //fem que ens doni una llista de tot el que conté la tbl_Config


                // condició que decideix si insertar o actualitzar
                if (!llista.isEmpty()) {
                    config = llista.get(0);// li passem el valor 0 de la llista per que només te un objecte
                    //ara he de conseguir els valors dels camps de Config amb els geters
                    tfMailResp.setText(config.getMailResp());
                    tfDirectoriArrelDocs.setText(config.getDirDoc());
                    tfCorreuRecepcio1.setText(config.getMailRecep1());
                    tfCorreuRecepcio2.setText(config.getMailRecep2());
                    taMissatgeAvis.setText(config.getMissatgeAvis());
                } else {
                    update = false;
                }
            }
        });
    }

    @FXML
    public void btGuardarOnAction(ActionEvent event) {

        if (tfMailResp.getLength() == 0 || tfCorreuRecepcio1.getLength() == 0 || tfCorreuRecepcio2.getLength() == 0 ||
                tfDirectoriArrelDocs.getLength() == 0 || taMissatgeAvis.getLength() == 0) {

            Utilitats.AlertaGeneralWarning(Constants.MSG_FINESTRA_CONFIGURACIO, Constants.MSG_ADV_CAMPS_BUITS, Constants.MSG_TOTS_CAMPS);

            } else{

                if (update) {
                    config.setMailRecep1(tfCorreuRecepcio1.getText());
                    config.setMailRecep2(tfCorreuRecepcio2.getText());
                    config.setDirDoc(tfDirectoriArrelDocs.getText());
                    config.setMailResp(tfMailResp.getText());
                    config.setMissatgeAvis(taMissatgeAvis.getText());
                    helper.update(config);
                } else {
                    Config c = new Config();
                    c.setMailRecep1(tfCorreuRecepcio1.getText());
                    c.setMailRecep2(tfCorreuRecepcio2.getText());
                    c.setDirDoc(tfDirectoriArrelDocs.getText());
                    c.setMailResp(tfMailResp.getText());
                    c.setMissatgeAvis(taMissatgeAvis.getText());
                    try {
                        helper.insert(c);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    AppConfigSingleton.getInstancia().setMailRecep1(tfCorreuRecepcio1.getText());
                    AppConfigSingleton.getInstancia().setMailRecep2(tfCorreuRecepcio2.getText());
                    AppConfigSingleton.getInstancia().setDirDoc(tfDirectoriArrelDocs.getText());
                    AppConfigSingleton.getInstancia().setMailResp(tfMailResp.getText());
                    AppConfigSingleton.getInstancia().setMissatgeAvis(taMissatgeAvis.getText());

                    update = true;
                }

                ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
            }
    }

        public void cercador(){

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(Constants.MSG_FINESTRA_DIRECTORYCHOOSER);
            Window stage = null;
            tfDirectoriArrelDocs.setText(String.valueOf(directoryChooser.showDialog(stage)));
        }
}