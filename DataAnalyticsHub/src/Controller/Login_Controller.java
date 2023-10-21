package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import Application.MainUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Login_Controller implements Initializable {

	@FXML
	private TextField text_username;
	
	@FXML
	private PasswordField text_password;
	
	@FXML
	private Button button_signup;
	
	@FXML
	private Button button_login;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		button_login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.loginUser(event, text_username.getText(), text_password.getText(), null, null);
				
			}
		});
		button_signup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/Signup.fxml", "Signup", null, null, null);
			}
			
		});
	}

	
}
