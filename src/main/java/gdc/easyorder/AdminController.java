
package gdc.easyorder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminController {
    int id=0;
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    @FXML
    private Button EditAdminBtn;

    @FXML
    private TextField adminNameEdit;

    @FXML
    private PasswordField adminNvConfPwdEdit;

    @FXML
    private PasswordField adminNvPwdEdit;

    @FXML
    private PasswordField adminPwdEdit;

    @FXML
    private Button clearFieldsBtn;
    @FXML
    private Button addAdminBtn;

    @FXML
    private PasswordField adminConfPwd;

    @FXML
    private TextField adminName;

    @FXML
    private PasswordField adminPwd;
    void clear(){
        adminPwd.setText((adminPwd.getText() != null) ? null : "");
        adminConfPwd.setText((adminConfPwd.getText() != null) ? null : "");
        adminName.setText((adminName.getText() != null) ? null : "");
    }
    void clearEdit(){
        adminNameEdit.setText((adminNameEdit.getText() !=null)? null :"");
        adminPwdEdit.setText((adminPwdEdit.getText() !=null)? null :"");
        adminNvPwdEdit.setText((adminNvPwdEdit.getText() !=null)? null :"");
        adminNvConfPwdEdit.setText((adminNvConfPwdEdit.getText() !=null)? null :"");
    }

    @FXML
    void addAdmin(ActionEvent event) {
        try {
                con = ConnectionToDB.getConnexion();
                st = con.prepareStatement("SELECT * FROM user WHERE user_name =? AND user_pwd=?");
                st.setString(1, adminName.getText());
                st.setString(2, adminPwd.getText());
                rs = st.executeQuery();
                if (rs.next()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Cet utilisateur est déjà existant", ButtonType.OK);
                    alert.show();
                }else {
                    if (adminPwd.getText().equals(adminConfPwd.getText())) {
                        String query = "INSERT INTO user (user_name,user_pwd) values(?,?)";
                        try {
                            st = con.prepareStatement(query);
                            st.setString(1, adminName.getText());
                            st.setString(2, adminPwd.getText());
                            st.executeUpdate();
                            clear();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Admin ajouté avec succès", ButtonType.OK);
                            alert.show();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Information icorrecte", ButtonType.OK);
                        alert.show();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    @FXML
    void editAdmin(ActionEvent event) {
        try {
            con = ConnectionToDB.getConnexion();
            st = con.prepareStatement("SELECT * FROM user WHERE user_name =? AND user_pwd=?");
            st.setString(1, adminNameEdit.getText());
            st.setString(2, adminPwdEdit.getText());
            rs = st.executeQuery();
            if (rs.next()) {
                if (adminNvPwdEdit.getText().equals(adminNvConfPwdEdit.getText())) {
                    id =rs.getInt("user_id");
                    String query = "UPDATE user SET user_pwd=? WHERE user_id=?";
                    con = ConnectionToDB.getConnexion();
                        st = con.prepareStatement(query);
                        st.setString(1,adminNvPwdEdit.getText());
                        st.setInt(2,id);
                        st.executeUpdate();
                        clearEdit();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Modification bien effectuée", ButtonType.OK);
                    alert.show();
                }}
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Informations Incorrect(Mdp ou nom d'utilisateur)", ButtonType.OK);
                alert.show();
            }
        } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }
    @FXML
    void clearFields(ActionEvent event) {clear();}

}
