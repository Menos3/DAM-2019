package cat.arsgbm.coral.classes;

import cat.arsgbm.coral.model.Config;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static cat.arsgbm.coral.classes.Constants.*;

public class Utilitats {

    /**
     * @param config  Objecte de la classe Config
     * Metode per actualitzar el Singleton a la base de dades quan es canvia algun registre
     * Es rep l'objecte modificat de la classe Config i s'aactualitza el Singleton per a que aquest faci el UPDATE a la base de dades
     *
     */

    public static void actualitza_config(Config config) {

        AppConfigSingleton.getInstancia().setDirDoc(config.getDirDoc());
        AppConfigSingleton.getInstancia().setMailRecep1(config.getMailRecep1());
        AppConfigSingleton.getInstancia().setMailRecep2(config.getMailRecep2());
        AppConfigSingleton.getInstancia().setMailResp(config.getMailResp());
        AppConfigSingleton.getInstancia().setMissatgeAvis(config.getMissatgeAvis());
    }

    /**
     * @param dateToConvert  Parametre tipus Date a convertir
     * @return la data transformada a LocalDate
     *
     * Metode per transformar un tipus Date a un tipus LocalDate per a que es pugui guardar en una base de dades
     *
     */

    public static LocalDate dataToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * @return la data del sistema en el moment de fer l'operacio
     *
     * Metode per agafar la data del sistema en el moment en que es faci una operacio
     *
     */

    public static java.sql.Date getDataActual() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }

    /**
     * @param localDate  Parametre tipus LocalDate a convertir
     * @return la data LocalDate transformada a Date
     *
     * Metode per transformar una data que ve d'una base de dades a una data que pot ser llegida per un component DatePicker
     *
     */

    public static Date localDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     *
     * @param Missatge1  Titol de la alerta (generalment Transportistes, Xofers o Vehicles)
     * @param Missatge2  Capcalera de la alerta (generalment Alta Transportistes, Xofers o Vehicles, Baixa Transportistes, Xofers o Vehicles)
     * @param Missatge3  El error del que s'esta avisant
     *
     * Metode per generar alertes de tipus WARNING
     * Es reben els tres missatges per omplir la finestra de la alerta i es mostra al usuari
     *
     */

    public static void AlertaGeneralWarning(String Missatge1, String Missatge2, String Missatge3) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Missatge1);
        alert.setHeaderText(Missatge2);
        alert.setContentText(Missatge3);
        ButtonType btDacord = new ButtonType(BTN_DACORD);
        alert.getButtonTypes().setAll(btDacord);
        alert.showAndWait();
    }

    /**
     * @param ruta_directori  Ruta especificada en el formulari de Configuracio
     * @param cif_nif         El CIF o NIF del Transportista
     * @return la ruta sencera on es troba l'arxiu guardat
     *
     * Metode per crear els directoris de Transportistes, Xofers i Vehicles en la ruta del formulari de Configuracio i guardar els arxius
     * Es rep la ruta i el cif o nif del transportista, es creen els directoris, amb un FileChooser es selecciona l'arxiu a guardar
     * en la carpeta de Transportistes, i es retorna la ruta d'aquest arxiu
     *
     */

    public static String GuardarFitxerTransportista(String ruta_directori, String cif_nif) {

        String directori_transp = ruta_directori + File.separator + "Transportistes";
        String directori_xofers = ruta_directori + File.separator + "Xofers";
        String directori_vehicles = ruta_directori + File.separator + "Vehicles";

        File f = new File(ruta_directori);
        boolean dir_tr;
        boolean dir_x;
        boolean dir_v;
        File ft = new File(directori_transp);
        File fx = new File(directori_xofers);
        File fv = new File(directori_vehicles);

        if (f.canWrite()) {
            if (f.exists()) {
                System.out.println("el path per crear estructura de gestió-transportistes/xofers/vehicles és correcte");

                if (ft.exists()) {
                    dir_tr = true;
                } else dir_tr = ft.mkdirs();

                if (fx.exists()) {
                    dir_x = true;
                } else dir_x = fx.mkdirs();

                if (fv.exists()) {
                    dir_v = true;
                } else dir_v = fv.mkdirs();

                if (dir_tr && dir_v && dir_x) {
                    System.out.println("els directoris s'han creat correctament i ja s'hi poden guardar els arxius. ");
                } else {
                    if (!dir_tr) System.out.println("NO s'ha pogut crear el directori de Transportistes");
                    if (!dir_x) System.out.println("NO s'ha pogut crear el directori de Xofers");
                    if (!dir_v) System.out.println("NO s'ha pogut crear el directori de Vehicles");
                }
            } else {
                System.out.println("el directori principal NO existeix , PROBLEMES PER CREAR-LO !!!!!");
            }
        } else {
            System.out.println("NO tens permisos per escriure, REVISA PERMISOS!!!");
        }

        System.out.println(directori_transp);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(MSG_FINESTRA_FILECHOOSER);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        );

        Window stage = null;
        File selectedimage = fileChooser.showOpenDialog(stage);
        System.out.println(selectedimage);
        String extension = FilenameUtils.getExtension(String.valueOf(selectedimage));
        System.out.println(extension);
        Path origen = selectedimage.toPath();
        Path destino = Paths.get(Paths.get(directori_transp) + File.separator + cif_nif + extension);

            try {
                Files.copy(origen, destino);
            } catch (IOException e) {
                e.printStackTrace();
                AlertaGeneralWarning(MSG_FINESTRA_GUARDARFITXERS, MSG_ADV_GUARDARFITXERS, MSG_GUARDARFITXERS);
            }
        return String.valueOf(destino);
    }

    /**
     * @param ruta_directori  Ruta especificada en el formulari de Configuracio
     * @param num_intern      Numero intern que la empresa assigna al xofer
     * @return la ruta sencera on es troba l'arxiu guardat
     *
     * Metode per crear els directoris de Transportistes, Xofers i Vehicles en la ruta del formulari de Configuracio i guardar els arxius
     * Es rep la ruta i el numero intern del xofer, es creen els directoris, amb un FileChooser es selecciona l'arxiu a guardar
     * en la carpeta de Xofers, i es retorna la ruta d'aquest arxiu
     */
    public static String GuardarFitxerXofer(String ruta_directori , String num_intern) {

        String directori_transp = ruta_directori + File.separator + "Transportistes";
        String directori_xofers = ruta_directori + File.separator + "Xofers";
        String directori_vehicles = ruta_directori + File.separator + "Vehicles";

        File f = new File(ruta_directori);
        boolean dir_creat = false;
        boolean dir_tr = false;
        boolean dir_x = false;
        boolean dir_v = false;
        File ft = new File(directori_transp);
        File fx = new File(directori_xofers);
        File fv = new File(directori_vehicles);

        if (f.canWrite()) {
            if (f.exists()) {
                System.out.println("el path per crear estructura de gestió-transportistes/xofers/vehicles és correcte");

                if (ft.exists()) {
                    dir_tr = true;
                } else dir_tr = ft.mkdirs();

                if (fx.exists()) {
                    dir_x = true;
                } else dir_x = fx.mkdirs();

                if (fv.exists()) {
                    dir_v = true;
                } else dir_v = fv.mkdirs();

                if (dir_tr && dir_v && dir_x) {
                    System.out.println("els directoris s'han creat correctament i ja s'hi poden guardar els arxius. ");
                } else {
                    if (!dir_tr) System.out.println("NO s'ha pogut crear el directori de Transportistes");
                    if (!dir_x) System.out.println("NO s'ha pogut crear el directori de Xofers");
                    if (!dir_v) System.out.println("NO s'ha pogut crear el directori de Vehicles");
                }
            } else {
                System.out.println("el directori principal NO existeix , PROBLEMES PER CREAR-LO !!!!!");
            }
        } else {
            System.out.println("NO tens permisos per escriure, REVISA PERMISOS!!!");
        }

        System.out.println(directori_xofers);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(MSG_FINESTRA_FILECHOOSER);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        );

        Window stage = null;
        File selectedimage = fileChooser.showOpenDialog(stage);
        System.out.println(selectedimage);
        String extension = FilenameUtils.getExtension(String.valueOf(selectedimage));
        System.out.println(extension);
        Path origen = selectedimage.toPath();
        Path destino = Paths.get(Paths.get(directori_xofers) + File.separator + num_intern + extension);

            try {
                Files.copy(origen, destino);
                return String.valueOf(destino);
            } catch (IOException e) {
                e.printStackTrace();
                AlertaGeneralWarning(MSG_FINESTRA_GUARDARFITXERS, MSG_ADV_GUARDARFITXERS, MSG_GUARDARFITXERS);
            }

        return String.valueOf(destino);
    }
}
