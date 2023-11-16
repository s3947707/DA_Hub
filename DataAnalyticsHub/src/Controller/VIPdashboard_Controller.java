package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

import Application.MainUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class VIPdashboard_Controller implements Initializable{
	
	@FXML
	private Button button_logout;
	
	@FXML
	private Button home;
	
	@FXML
	private Button add_post;
	
	@FXML
	private Label label_welcome;
	
	@FXML
	private Label label_menu;
	
	@FXML
	private Button remove_post;
	
	@FXML
	private Button export_post;
	
	@FXML
	private Button edit_profile;
	
	@FXML
	private Button find_post_id;
	
	@FXML
	private Button find_post_likes;
	
	@FXML
	private Button importposts;
	
	@FXML
	private Button stats;
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
		stats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/DataVisualization_VIP.fxml", "Home", null, null, null);
				
			}
		});
		add_post.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/AddRemove_Post_VIP.fxml", "AddPost", null, null, null);
				
			}
		});
		remove_post.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/AddRemove_Post_VIP.fxml", "RemovePost", null, null, null);
				
			}
		});
		export_post.setOnAction(event -> {
		    // Get all post data from the database
		    List<String> allPostData = MainUtility.getAllPostDataFromDB();

		    if (!allPostData.isEmpty()) {
		        // Prompt the user to select a folder and enter the file name
		        FileChooser fileChooser = new FileChooser();
		        fileChooser.setTitle("Save All Post Data as CSV");
		        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
		        File file = fileChooser.showSaveDialog(null);

		        if (file != null) {
		            String fileName = file.getAbsolutePath(); // Get the absolute file path
		            MainUtility.exportAllPostDataToCSV(fileName, allPostData);
		        }
		    } else {
		        showAlert(Alert.AlertType.ERROR, "No Posts Found", "There are no posts in the database to export.");
		    }
		});

		edit_profile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/EditProfileVIP2.fxml", "EditProfile", null, null, null);
				
			}
		});
		find_post_id.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/Post_VIP.fxml", "FindPostID", null, null, null);
				
			}
		});
		find_post_likes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUtility.changeScene(event, "/View/Like_VIP.fxml", "FindPostLikes", null, null, null);
				
			}
		});
		importposts.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
//				MainUtility.changeScene(event, "/View/Like_VIP.fxml", "FindPostLikes", null, null, null);
				handleImportPosts(event);
				
			}
		});
	}
	
	private void handleImportPosts(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File file : selectedFiles) {

            try {
                // Read and process the CSV file
                importPostsFromCSV(file);
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Import Error", "An error occurred while importing posts.");
                e.printStackTrace();
            }
        }
            //showAlert(Alert.AlertType.INFORMATION, "Import Successful", "Social media posts imported successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Files Selected", "No CSV files were selected for import.");
        }
    }

	private void importPostsFromCSV(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");

            String insertSQL = "INSERT INTO posts (Content, Likes, Author, DateTime) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
            StringBuilder importedData = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 4) {
                    showAlert(Alert.AlertType.ERROR, "CSV Format Error", "The CSV file does not follow the correct format.");
                    return;
                }

                String content = data[0];
                int likes = Integer.parseInt(data[1]);
                String author = data[2];
                Timestamp dateTime = Timestamp.valueOf(data[3]);

                preparedStatement.setString(1, content);
                preparedStatement.setInt(2, likes);
                preparedStatement.setString(3, author);
                preparedStatement.setTimestamp(4, dateTime);
                preparedStatement.executeUpdate();
                

                // Append the imported data to the StringBuilder
                importedData.append("Content: ").append(content).append("\n");
                importedData.append("Likes: ").append(likes).append("\n");
                importedData.append("Author: ").append(author).append("\n");
                importedData.append("DateTime: ").append(dateTime.toString()).append("\n\n");
            }
            preparedStatement.close();
            conn.close();

            saveDataToTextFile(importedData.toString());

        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Import Error", "An error occurred while importing posts.");
            e.printStackTrace();
        }
    }

//	private void importPostsFromCSV(File file) throws IOException {
//	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//	        String line;
//	        StringBuilder postsText = new StringBuilder();
//
//	        while ((line = reader.readLine()) != null) {
//	            // Parse and process each line of the CSV file
//	            String[] data = line.split(",");
//	            if (data.length != 3) {
//	                // Handle incorrect CSV format
//	                showAlert(Alert.AlertType.ERROR, "CSV Format Error", "The CSV file does not follow the correct format.");
//	                return;
//	            }
//
//	            // Process the data
//	            String postText = data[0];
//	            String username = data[1];
//	            String timestamp = data[2];
//
//	            // Append the data to the StringBuilder
//	            postsText.append("Post Text: ").append(postText).append("\n");
//	            postsText.append("Username: ").append(username).append("\n");
//	            postsText.append("Timestamp: ").append(timestamp).append("\n");
//	            postsText.append("\n");
//
//	            // Save the post data to a text file
//	            saveDataToTextFile(postsText.toString());
//	        }
//	    }
//	}

	 private void saveDataToTextFile(String data) {
	        try {
	            File textFile = new File("imported_posts.txt");
	            FileWriter fileWriter = new FileWriter(textFile, true); 

	            try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
	                writer.write(data);
	                writer.newLine();
	            }

	        } catch (IOException e) {
	            showAlert(Alert.AlertType.ERROR, "File Save Error", "An error occurred while saving the imported posts to 'imported_posts.txt'.");
	            e.printStackTrace();
	        }
	   }
	public static void showAlert(Alert.AlertType alertType, String title, String contentText) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(contentText);
	    alert.showAndWait();
	}
	
	public void setUserInformation(String Firstname, String Lastname, String VIPstatus) {
		label_welcome.setText("Welcome " + Firstname + " " + Lastname + "!");
		label_menu.setText("VIP Quick Access Menu");
	}
}
