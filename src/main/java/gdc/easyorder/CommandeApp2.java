package gdc.easyorder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CommandeApp2 extends Application {
    @Override
    public void start(Stage stage)  {
        try{
            Parent parent = FXMLLoader.load(getClass().getResource("commande.fxml"));
            Scene scene = new Scene(parent);
            stage.setTitle("Gestion des commandes2222");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){launch();}
}
