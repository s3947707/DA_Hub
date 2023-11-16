package Controller;

import java.io.File;
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
import javafx.stage.FileChooser;

public class Post_Controller implements Initializable{
	
	@FXML
	private Button button_logout;
	
	@FXML
	private Button home;
	
	@FXML
	private Button findPost;
	
	
	
    @FXML
    private TextField findPostTextField;

   
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
				MainUtility.changeScene(event, "/View/Dashboard.fxml", "Home", null, null, null);
				
			}
		});
		findPost.setOnAction(event -> {
		    String postID = findPostTextField.getText();
		    String postDetails = MainUtility.findPostDetails(postID);

		    if (postDetails != null) {
		        Alert alert = new Alert(Alert.AlertType.INFORMATION);
		        alert.setTitle("Post Details");
		        alert.setHeaderText("Post ID: " + postID);
		        alert.setContentText(postDetails);
		        alert.showAndWait();
		    } else {
		        showAlert(Alert.AlertType.ERROR, "Post Not Found", "The post with ID " + postID + " does not exist.");
		    }
		});
//		exportPost.setOnAction(event -> {
//		    String postID = findPostTextField.getText();
//		    String postDetails = MainUtility.findPostDetails(postID);
//
//		    if (postDetails != null) {
//		        // Assuming you have a way to get post details like content, likes, author, shares, and dateTime.
//		    	 Alert alert = new Alert(Alert.AlertType.INFORMATION);
//			       alert.setTitle("Post Details");
//			       alert.setHeaderText("Post ID: " + postID);
//			       alert.setContentText(postDetails);
//			       alert.showAndWait();
//
//		        // Prompt the user to select a folder and enter the file name.
//		        FileChooser fileChooser = new FileChooser();
//		        fileChooser.setTitle("Save Post Details as CSV");
//		        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
//		        File file = fileChooser.showSaveDialog(null);
//
//		        if (file != null) {
//		            String fileName = file.getName();
//		            MainUtility.exportPostToCSV(postID, fileName, content, likes, author, shares, dateTime);
//		        }
//		    } else {
//		        showAlert(Alert.AlertType.ERROR, "Post Not Found", "The post with ID " + postID + " does not exist.");
//		    }
//		});
//		exportPost.setOnAction(event -> {
//		    String postID = findPostTextField.getText();
//
//		    // Fetch post details from the database
//		    String postDetails = MainUtility.findPostDetailsFromDB(postID);
//
//		    if (postDetails != null) {
//		        // Assuming you have a way to get post details like content, likes, author, shares, and dateTime.
//
//		        // You've already retrieved post details in 'postDetails', so you don't need to re-fetch them.
//
//		        // Split the 'postDetails' string to extract individual details
//		        String[] details = postDetails.split(","); // Assuming you used a comma (,) as the delimiter
//
//		        if (details.length == 5) {
//		            String content = details[0];
//		            int likes = Integer.parseInt(details[1]);
//		            String author = details[2];
//		            int shares = Integer.parseInt(details[3]);
//		            String dateTime = details[4];
//
//		            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//		            alert.setTitle("Post Details");
//		            alert.setHeaderText("Post ID: " + postID);
//		            alert.setContentText(postDetails);
//		            alert.showAndWait();
//
//		            // Prompt the user to select a folder and enter the file name.
//		            FileChooser fileChooser = new FileChooser();
//		            fileChooser.setTitle("Save Post Details as CSV");
//		            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
//		            File file = fileChooser.showSaveDialog(null);
//
//		            if (file != null) {
//		                String fileName = file.getAbsolutePath();
//		                MainUtility.exportPostToCSV(postID, fileName, content, likes, author, shares, dateTime);
//		            }
//		        } else {
//		            showAlert(Alert.AlertType.ERROR, "Invalid Post Details", "The post details have an invalid format.");
//		        }
//		    } else {
//		        showAlert(Alert.AlertType.ERROR, "Post Not Found", "The post with ID " + postID + " does not exist.");
//		    }
//		});
//


	}

	public static void showAlert(Alert.AlertType alertType, String title, String contentText) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(contentText);
	    alert.showAndWait();
	}

	
	
	
}
