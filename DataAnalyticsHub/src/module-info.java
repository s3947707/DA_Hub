module DataAnalyticsHub {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;
	requires javafx.graphics;
	requires java.sql;
	
	opens Application to javafx.graphics, javafx.fxml;

}
