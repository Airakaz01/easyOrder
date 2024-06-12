package gdc.easyorder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    public Button logInBtn;
    public PasswordField pwdInput;
    public  TextField userNameInput;
    @FXML
    private ImageView Exit;
    public void login() {
        PreparedStatement st = null;
        ResultSet rs = null;
        Connection con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement("SELECT * FROM user WHERE user_name =? AND user_pwd=?");
            st.setString(1, userNameInput.getText());
            st.setString(2, pwdInput.getText());
            rs = st.executeQuery();
            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bienvenue", ButtonType.OK);
                alert.show();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Set the loaded root node as the new scene
                Scene scene = new Scene(root);
                Stage stage = (Stage) logInBtn.getScene().getWindow();
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();
;
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Information icorrecte", ButtonType.OK);
                alert.show();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ;
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logInBtn.setOnAction(actionEvent -> login());
        Exit.setOnMouseClicked(event -> System.exit(0));
    }
}
