package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import Application.MainUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Dashboard_Controller implements Initializable{
	
	@FXML
	private Button button_logout;
	
	@FXML
	private Label label_welcome;
	
	@FXML
	private Label label_menu;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		button_logout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/Login.fxml", "Login", null, null, null);
				
			}
		});
	}
	
	public void setUserInformation(String Firstname, String Lastname, String VIPstatus) {
		label_welcome.setText("Welcome " + Firstname + " " + Lastname + "!");
		label_menu.setText(VIPstatus + " Quick Access Menu");
	}
	
	
	
	
}
