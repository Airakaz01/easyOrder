package gdc.easyorder;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class MontantlimitetController implements Initializable {

    private static double MontantLimit = 1000000;
    private static final String FILE = "config.properties";

    @FXML
    private TextField tfMontant;

    @FXML
    private Button btnModifier;



    @FXML
    private Label labelM;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadMontantLimit();
        updateLabel();
    }

    private void updateLabel() {
        labelM.setText(String.valueOf(MontantLimit));
    }



    @FXML
    void ModifierMontant(ActionEvent event) {
        try {
            Double montant = Double.parseDouble(tfMontant.getText());
            if (montant >= 0) {
                MontantLimit = montant;
                updateLabel();
                saveMontantLimit();
            }
        } catch (NumberFormatException e) {
            tfMontant.setText("Invalid input");
        }
    }

    private void saveMontantLimit() {
        Properties properties = new Properties();
        properties.setProperty("MontantLimit", String.valueOf(MontantLimit));

        try (OutputStream output = new FileOutputStream(FILE)) {
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void loadMontantLimit() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(FILE)) {
            properties.load(input);
            MontantLimit = Double.parseDouble(properties.getProperty("MontantLimit", "1000000"));
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static double getMontantLimit() {
        return MontantLimit;
    }
    public static void setMontantLimit(double montantLimit) {
        MontantLimit = montantLimit;
    }

}
