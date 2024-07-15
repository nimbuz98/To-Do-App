package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tm.ToDoTM;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ToDoFormController {
    public Label lblGreeting;
    public Label lblUserId;
    public TextField txtSearch;
    public AnchorPane rootToDo;
    public Pane pane;
    public TextField txtDescription;
    public Button btnAddToDO;
    public ListView lstToDo;
    public Button btnDelete;
    public Button btnUpdate;

    public String selectedToDOId = null;
    public void initialize() throws SQLException {
        txtSearch.requestFocus();
        lblGreeting.setText("Hi "+LoginFormController.name1+" welcome to To-Do list");
        lblUserId.setText(LoginFormController.un);
        pane.setDisable(true);
        btnAddToDO.setDisable(true);
        txtDescription.setDisable(true);
        loadList();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        txtSearch.setDisable(true);

        lstToDo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                btnDelete.setDisable(false);
                btnUpdate.setDisable(false);
                txtSearch.setDisable(false);

                pane.setVisible(false);
                txtDescription.setDisable(true);
                btnAddToDO.setDisable(true);

                ToDoTM selectedItem = (ToDoTM) lstToDo.getSelectionModel().getSelectedItem();

                if(selectedItem == null){
                    return;
                }
                txtSearch.setText(selectedItem.toString());
                selectedToDOId=  selectedItem.getTodo_id();

            }
        });

    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do You Want to Log Out?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)){
            Stage stage = (Stage)rootToDo.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
        }
    }

    public void btnAddNewToDo(ActionEvent actionEvent) {

        lstToDo.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        txtSearch.setDisable(true);

        pane.setVisible(true);
        txtDescription.requestFocus();
        btnAddToDO.setDisable(false);
        txtDescription.setDisable(false);

    }

    public void btnAddToDoToList(ActionEvent actionEvent) {
        String id = generateAutoID();
        String description = txtDescription.getText();
        String user_id = lblUserId.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values (?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,description);
            preparedStatement.setObject(3,user_id);

            preparedStatement.executeUpdate();
            btnAddToDO.setDisable(true);
            loadList();
            txtDescription.clear();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String generateAutoID(){

        Connection connection = DBConnection.getInstance().getConnection();

        String todoID =null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from todo order by id desc limit 1");
            boolean next = resultSet.next();

            if(next){

                todoID= resultSet.getString(1);
                todoID = todoID.substring(1,todoID.length());

                int intID = Integer.parseInt(todoID);
                intID++;

                if(intID<10){
                    todoID = "T00"+intID;
                }else if(intID<100){
                    todoID = "T0"+intID;
                }else
                    todoID = "T"+intID;

            }else {
                todoID = "T001";
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todoID;
    }

    public void loadList() throws SQLException {
        ObservableList<ToDoTM> todos = lstToDo.getItems();
        todos.clear();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where userID = ?");
        preparedStatement.setObject(1,LoginFormController.un);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            String tId = resultSet.getString(1);
            String description = resultSet.getString(2);
            String uId = resultSet.getString(3);

            ToDoTM obj = new ToDoTM(tId,description,uId);

            todos.add(obj);
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent)  {
        String selectedText = txtSearch.getText();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where id = ?");
            preparedStatement.setObject(1,selectedText);
            preparedStatement.setObject(2,selectedToDOId);

            preparedStatement.executeUpdate();
            txtSearch.clear();
            loadList();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES)){
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where id = ? ");
                preparedStatement.setObject(1,selectedToDOId);
                preparedStatement.executeUpdate();

                loadList();
                txtSearch.clear();
                txtSearch.setDisable(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }
}
