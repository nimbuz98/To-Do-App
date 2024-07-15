package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateNewAccountFormController {
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;
    public Label lblPasswordDoesntMatch1;
    public Label lblPasswordDoesntMatch2;
    public AnchorPane rootCreateNewAccountForm;
    public TextField txtUsername;
    public TextField txtEmail;
    public Button btnRegister;
    public Label lblUserID;

    public void initialize(){

        passwordDoesntMatchLable(false);
        displayFields(true);
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {
        String newPassword =  txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if(newPassword.equals(confirmPassword)){
            txtNewPassword.setStyle("-fx-border-color: transparent");
            passwordDoesntMatchLable(false);
            register();
        }else {
            txtNewPassword.setStyle("-fx-border-color: red");
            txtNewPassword.requestFocus();

            passwordDoesntMatchLable(true);
        }
    }

    public void passwordDoesntMatchLable(boolean state){
        lblPasswordDoesntMatch1.setVisible(state);
        lblPasswordDoesntMatch2.setVisible(state);
    }

    public void btnAddNewUser(ActionEvent actionEvent) {
        displayFields(false);
        txtUsername.requestFocus();
//        Connection connection = DBConnection.getInstance().getConnection();
//        System.out.println(connection);
        generateAutoID();
    }

    public void displayFields(boolean state){
        txtUsername.setDisable(state);
        txtEmail.setDisable(state);
        txtNewPassword.setDisable(state);
        txtConfirmPassword.setDisable(state);
        btnRegister.setDisable(state);

    }

    public void generateAutoID(){

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from user order by id desc limit 1");
            boolean next = resultSet.next();

            if(next){

                String userID = resultSet.getString(1);
                userID = userID.substring(1,userID.length());

                int intID = Integer.parseInt(userID);
                intID++;

                if(intID<10){
                    lblUserID.setText("U00"+intID);
                }else if(intID<100){
                    lblUserID.setText("U0"+intID);
                }else
                    lblUserID.setText("U"+intID);

            }else {
                lblUserID.setText("U001");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void register(){
        String userID = lblUserID.getText();
        String userName = txtUsername.getText();
        String email = txtEmail.getText();
        String newPassword = txtNewPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
//            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values (?,?,?,?)");
            preparedStatement.setObject(1,userID);
            preparedStatement.setObject(2,userName);
            preparedStatement.setObject(3,email);
            preparedStatement.setObject(4,newPassword);

            int i = preparedStatement.executeUpdate();

            if (i!=0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Success...");
                alert.showAndWait();

               Stage stage = (Stage)rootCreateNewAccountForm.getScene().getWindow();
               stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
