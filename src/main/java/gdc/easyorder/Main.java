package gdc.easyorder;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class Main {
    private Pane view;
    public Pane getPage(String fileName){
        try {

            URL fileUrl =Main.class.getResource("/gdc/easyorder/"+fileName+".fxml");
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("FXML FILE EST INTROUVABLE");
            }
            try {
                view = new FXMLLoader().load(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch (Exception e){
//            System.out.println("Aucune page "+fileName+"");
            e.printStackTrace();
        }
        return view;
    }

}
