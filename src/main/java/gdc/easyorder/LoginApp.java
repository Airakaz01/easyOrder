package gdc.easyorder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginApp extends Application {
    @Override
    public void start(Stage stage)  {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApp.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
