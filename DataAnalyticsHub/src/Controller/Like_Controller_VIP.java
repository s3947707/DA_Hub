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
import javafx.scene.control.TextField;

public class Like_Controller_VIP implements Initializable{
	
	@FXML
	private Button button_logout;
	
	@FXML
	private Button home;
	
	
	
		
   
    
    @FXML
    private TextField likeinput;

    @FXML
	private Button noOfLike;
   
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
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
		
		noOfLike.setOnAction(event -> {
		    String postID = likeinput.getText();
		    System.out.println("like number: " +likeinput.getText());
		    String postDetails = MainUtility.findPostDetailsbyLike(postID);

		    if (postDetails != null) {
		        showAlert(Alert.AlertType.INFORMATION, "Post Details", "Likes for Post ID " + postID + ": " + postDetails);
		    } else {
		        showAlert(Alert.AlertType.ERROR, "Post Not Found", "The post with ID " + postID + " does not exist.");
		    }
		});


	}

	public static void showAlert(Alert.AlertType alertType, String title, String contentText) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(contentText);
	    alert.showAndWait();
	}

	
	
	
}
