package dam2.controls.limitedtextfield;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        LimitedTextField control = new LimitedTextField();
        stage.setScene(new Scene(control));
        stage.setTitle("Control LimitedTextField !!!");
        stage.setWidth(300);
        stage.setHeight(200);
        stage.show();
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
