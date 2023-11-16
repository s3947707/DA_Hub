package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Application.MainUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AddRemovePost_Controller_VIP implements Initializable{
	
	@FXML
	private Button button_logout;
	
	@FXML
	private Button home;
	
	 @FXML
	    private Button addPostText;

	    @FXML
	    private TextField contentField;

	    @FXML
	    private TextField likesField;

	    @FXML
	    private TextField postIdField;

	    @FXML
	    private TextField postidforDelete;
	    
	    @FXML
	    private TextField authorField;

	    @FXML
	    private DatePicker dateTimePicker;

	    @FXML
	    private PasswordField passwordField;

	    @FXML
	    private TextField sharesField;
	    @FXML
	    private Button addPost;
	    @FXML
	    private Button RemovePost;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		Connection connection = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet resultSet = null;
//		try {
//			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
//			preparedStatement = connection.prepareStatement("SELECT Password, Firstname, Lastname, VIPstatus FROM users WHERE Username = ?");
//			String retrieveFirstname = resultSet.getString("FirstName");
//			String retrieveLastname = resultSet.getString("LastName");
//			home.setOnAction(new EventHandler<ActionEvent>() {
//				@Override
//				public void handle(ActionEvent event) {
//					MainUtility.changeScene(event, "/View/Dashboard.fxml", "Home", retrieveFirstname, retrieveLastname, "");
//					
//				}
//			});
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		button_logout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/Login.fxml", "Login", null, null, null);
				
			}
		});

		home.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/VIPdashboard.fxml", "Home", null, null, null);
				
			}
		});
		  addPost.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                String content = contentField.getText();
	                String likes = likesField.getText();
	                String postId = postIdField.getText();
	                String author = authorField.getText();
	                String dateTime = dateTimePicker.getValue().toString(); // You may need to format this as per your requirements
	                String shares = sharesField.getText();

	                // Call your utility method to add a post
	                MainUtility.addPost(content, likes, postId, author, dateTime, shares);

	                // Optionally, you can clear the fields after adding the post
	                clearFields();
	            }
	        });
		  RemovePost.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                String postId = postidforDelete.getText();

	                // Call your utility method to remove a post
	                MainUtility.removePost(postId);

	                // Optionally, you can clear the fields after removing the post
	                postidforDelete.clear();
	            }
	        });
	}
	 private void clearFields() {
	        contentField.clear();
	        likesField.clear();
	        postIdField.clear();
	        authorField.clear();
	        dateTimePicker.getEditor().clear();
	        sharesField.clear();
	    }
	
	
	
	
}
