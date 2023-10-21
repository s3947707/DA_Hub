package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import Application.MainUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class Signup_Controller implements Initializable {
	
	@FXML
	private TextField text_firstname;
	
	@FXML
	private TextField text_lastname;
	
	@FXML
	private TextField text_username;
	
	@FXML
	private PasswordField password_password;
	
	@FXML
	private RadioButton radio_yes;
	
	@FXML
	private RadioButton radio_no;
	
	@FXML
	private Button button_signup;
	
	@FXML
	private Button button_login;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ToggleGroup toggleGroup = new ToggleGroup();
		radio_yes.setToggleGroup(toggleGroup);
		radio_no.setToggleGroup(toggleGroup);
		
		radio_no.setSelected(true);
		
		button_signup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String toggleName = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
				
				if (!text_firstname.getText().trim().isEmpty() && !text_lastname.getText().trim().isEmpty() && !text_username.getText().trim().isEmpty() && !password_password.getText().trim().isEmpty()) {
					MainUtility.signupUser(event, text_firstname.getText(), text_lastname.getText(), text_username.getText(), password_password.getText(), toggleName);
				} else {
					System.out.println("Please fill in all fields");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("Please fill in all information to sign up. ");
					alert.show();
				}
			}
		});
		
		button_login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/Login.fxml", "Log in", null, null, null);
			}
		});
	}

	
}
