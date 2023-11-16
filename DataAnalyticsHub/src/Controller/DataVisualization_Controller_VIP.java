

package Controller;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

public class DataVisualization_Controller_VIP implements Initializable{
	
	@FXML
	private Button button_logout;
	
	@FXML
	private Button home;
	
    @FXML
    private PieChart chartsDatas;
    
    int[] shareCounts = MainUtility.getPostSharesCount();
    int postLikes = shareCounts[0] + shareCounts[1] + shareCounts[2];
    int usersCount = (int) (Math.random() * 10000); 
    
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
	
		updatePieChartWithData();
	}

	private void updatePieChartWithData() {
		Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
        try {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
	        String selectQuery = "SELECT Likes, Shares FROM posts";

	        preparedStatement = connection.prepareStatement(selectQuery);

	        resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
            	    int Like = resultSet.getInt("Likes");
		            int Share = resultSet.getInt("Shares");

                PieChart.Data likesData = new PieChart.Data("Likes", Like);
                PieChart.Data usersData = new PieChart.Data("Shares", Share);
                chartsDatas.getData().clear();
                chartsDatas.getData().addAll(likesData, usersData);
            }

            resultSet.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}