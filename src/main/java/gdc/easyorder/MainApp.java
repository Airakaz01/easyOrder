package gdc.easyorder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage)  {
        try{
            Parent parent = FXMLLoader.load(getClass().getResource("main.fxml"));
            Scene scene = new Scene(parent);
            stage.setTitle("Gestion des factures");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){launch();}
}
