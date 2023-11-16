

package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import Application.MainUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;

public class DataVisualization_Controller implements Initializable{
	
	@FXML
	private Button button_logout;
	
	@FXML
	private Button home;

	
    @FXML
    private PieChart chartsDatas;
    
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
		 int postLikes = MainUtility.getPostLikesCount();
	        int usersCount = MainUtility.getUsersCount();

	        // Create PieChart Data
	        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
	            new PieChart.Data("Post Likes", postLikes),
	            new PieChart.Data("Users", usersCount)
	        );

	        chartsDatas.setData(pieChartData);
	        chartsDatas.setTitle("Distribution of Likes vs Users");
	}
	
	
	
	
	
}