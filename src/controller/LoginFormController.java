package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    public AnchorPane rootLoginForm;
    public TextField txtUserName;
    public PasswordField txtPassword;

    public static String name1;
    public static String un;


    public void lblCreateAccountOnMouseClicked(MouseEvent mouseEvent) throws IOException {

//        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/CreateNewAccountForm.fxml"));
//        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) rootLoginForm.getScene().getWindow();
        primaryStage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("../view/CreateNewAccountForm.fxml"))));
        primaryStage.setTitle("Register Form");


    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        Login();
    }

    public void txtPasswordOnAction(ActionEvent actionEvent) {
        Login();
    }

    public void Login(){
        if (txtUserName.getText().trim().isEmpty()){
            txtUserName.requestFocus();
        }else if (txtPassword.getText().trim().isEmpty()){
            txtPassword.requestFocus();
        }else{
            String userName = txtUserName.getText();
            String password = txtPassword.getText();

            Connection connection = DBConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from user where userName = ? and password = ?");
                preparedStatement.setObject(1,userName);
                preparedStatement.setObject(2,password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    name1 = resultSet.getString(2);
                    un = resultSet.getString(1);

                    Stage stage = (Stage)rootLoginForm.getScene().getWindow();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ToDoForm.fxml"))));



                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Email or Password does not match");
                    alert.showAndWait();

                    txtUserName.clear();
                    txtPassword.clear();
                    txtUserName.requestFocus();
                }



            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

    }
}
