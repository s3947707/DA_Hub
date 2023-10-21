package Application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Controller.Dashboard_Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainUtility {
	
	public static void changeScene(javafx.event.ActionEvent event, String fxmlFile, String Title, String Firstname, String Lastname, String VIPstatus) {
		Parent root = null;
		if (Firstname != null && Lastname != null && VIPstatus != null) {
			try {
				FXMLLoader loader = new FXMLLoader(MainUtility.class.getResource(fxmlFile));
				root = loader.load();
				Dashboard_Controller Dashboard_Controller = loader.getController();
				Dashboard_Controller.setUserInformation(Firstname, Lastname, VIPstatus);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				root = FXMLLoader.load(MainUtility.class.getResource(fxmlFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle(Title);
		stage.setScene(new Scene(root, 700, 400));
		stage.show();
	}
	
	
	public static void signupUser(javafx.event.ActionEvent event, String Firstname, String Lastname, String Username, String Password, String VIPstatus) {
		Connection connection = null;
		PreparedStatement psInsert = null;
		PreparedStatement psCheckUserExists = null;
		ResultSet resultSet = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
			psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
			psCheckUserExists.setString(1, Username);
			resultSet = psCheckUserExists.executeQuery();
			
			if (resultSet.isBeforeFirst()) {
				System.out.println("User already exsists");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("You cannot use this username. ");
				alert.show();
			} else {
				psInsert = connection.prepareStatement("INSERT INTO users (Firstname, Lastname, Username, Password, VIPstatus VALUES (?, ?, ?, ?, ?)");
				psInsert.setString(1, Firstname);
				psInsert.setString(2, Lastname);
				psInsert.setString(3, Username);
				psInsert.setString(4, Password);
				psInsert.setString(5, VIPstatus);
				psInsert.executeUpdate();
				
				changeScene(event, "/View/Dashboard.fxml", "Welcome to the Data Analytics Hub", Firstname, Lastname, VIPstatus);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (psCheckUserExists!= null) {
				try {
					psCheckUserExists.close();
				} catch (SQLException e) {
					e.printStackTrace();	
				}
			}
			
			if (psInsert != null) {
				try {
					psInsert.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static void loginUser(javafx.event.ActionEvent event, String Firstname, String Lastname, String Username, String Password) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
			preparedStatement = connection.prepareStatement("SELECT Password, VIPstatus FROM users WHERE Username = ?");
			preparedStatement.setString(1, Username);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.isBeforeFirst()) {
				System.out.println("User not found in database");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Provided credentials are incorrect. ");
				alert.show();
			} else {
				while (resultSet.next()) {
					String retrievePassword = resultSet.getString("Password");
					String retrieveVIPstatus = resultSet.getString("VIPstatus");
					if (retrievePassword.equals(Password)) {
						changeScene(event, "/View/Dashboard.fxml", "Welcome to the Data Analytics Hub", Firstname, Lastname, retrieveVIPstatus);
					} else {
						System.out.println("Passwords did not match. ");
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setContentText("The provided credientials are incorrect. ");
						alert.show();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (preparedStatement!= null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();	
				}
			}
			
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
}