package cat.arsgbm.coral;

import cat.arsgbm.coral.classes.*;
import cat.arsgbm.coral.controllers.MainController;
import cat.arsgbm.coral.model.Config;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.internal.util.config.ConfigurationException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private EntityManagerFactory emf;
    public static void main(String[] args) {
        launch(args);

    }


    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));

        try {
            Parent root = loader.load();
            MainController controller = loader.getController();
            controller.setEmf(emf);
            stage.setTitle("CORAL TRANSPORTES");
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al iniciar", ex);
            Platform.exit();
        }
    }
    //DINS DE init() I AVANS DE CREAR LA CONEXIÓ HAURÉ DE RECUPERAR EL PASSWORD DESENCRIPTAT
    @Override
    public void init() {
        try {
            super.init();
            AppPropertiesFile propertiesFile = llegirFitxerPropietats("app.properties");
            String passwd = "";
            boolean test = false;
            try {       //AQUÍ ESTÀ EL PASSWORD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                passwd = getDecryptedPropsFilePassword("app.properties", "jdbc.password", "encrypted");
                System.out.println("\nContrasenya actual:  " + passwd);  //AQUÍ ESTÀ EL PASSWORD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            } catch (PropertiesHelperException e) {
                System.err.println("Error en el procés d'encriptació");
            } catch (org.apache.commons.configuration.ConfigurationException e) {
                e.printStackTrace();
            }
            Map properties = new HashMap();
            properties.put("javax.persistence.jdbc.user", propertiesFile.getUserName());
            properties.put("javax.persistence.jdbc.password", passwd);
            //properties.put("test",test);


            emf = Persistence.createEntityManagerFactory(Constants.UP_PROD,properties);
            //emf = Persistence.createEntityManagerFactory("local");
            DAOHelper<Config> helper = new DAOHelper<>(emf, Config.class);
            Config config = helper.getLastItem();
            Utilitats.actualitza_config(config);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        if (emf != null && emf.isOpen())
            emf.close();

        super.stop();
    }

    // AQUÍ COMENÇA EL CODI PER DESENCRIPTAR PASSWORD


    private static String getDecryptedPropsFilePassword(String fitxer, String clauAEncriptar, String clauTestEncriptacio) throws PropertiesHelperException, ConfigurationException, org.apache.commons.configuration.ConfigurationException {
        AppPropertiesFileHelper helper = new AppPropertiesFileHelper(fitxer, clauAEncriptar, clauTestEncriptacio);
        return helper.getDecryptedUserPassword();
    }

    private static AppPropertiesFile llegirFitxerPropietats(String filename) {
        InputStream fitxer = null;
        Properties credencials = new Properties();
        AppPropertiesFile propertiesFile = new AppPropertiesFile();

        try {
            fitxer = Main.class.getClassLoader().getResourceAsStream(filename);

            if (fitxer == null) {
                System.err.println("No puc llegir " + filename);
            } else {
                credencials.load(fitxer);

                propertiesFile.setHost(credencials.getProperty("jdbc.host"));
                propertiesFile.setDatabase(credencials.getProperty("jdbc.database"));
                propertiesFile.setUserName(credencials.getProperty("jdbc.username"));
                propertiesFile.setPasswd(credencials.getProperty("jdbc.password"));

                System.out.println(propertiesFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fitxer != null) {
                try {
                    fitxer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return propertiesFile;
    }
}