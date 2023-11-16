package Application;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Controller.Dashboard_Controller;
import Controller.UserInfo;
import Controller.VIPdashboard_Controller;

import java.io.File;
import java.io.FileWriter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
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
			} catch (SQLException e) {
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
				System.out.println("Username already exsists, please change your usename");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("You cannot use this username because it already exist. ");
				alert.show();
			} else {
				psInsert = connection.prepareStatement("INSERT INTO users (Firstname, Lastname, Username, Password, VIPstatus) VALUES (?, ?, ?, ?, ?)");
				psInsert.setString(1, Firstname);
				psInsert.setString(2, Lastname);
				psInsert.setString(3, Username);
				psInsert.setString(4, Password);
				psInsert.setString(5, VIPstatus);
				psInsert.executeUpdate();
				
				// Check VIP status and redirect accordingly
//	            if ("yes".equalsIgnoreCase(VIPstatus)) {
//	                // Redirect to VIP dashboard
//	                changeScene(event, "/View/VIPDashboard.fxml", "VIP Dashboard", Firstname, Lastname, VIPstatus);
//	            } else {
//	                // Redirect to simple dashboard
//	                changeScene(event, "/View/Dashboard.fxml", "Welcome to the Data Analytics Hub", Firstname, Lastname, VIPstatus);
//	            }
//				changeScene(event, "/View/Dashboard.fxml", "Welcome to the Data Analytics Hub", Firstname, Lastname, VIPstatus);
				changeScene(event, "/View/Login.fxml", "Login", null, null, null);
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

	public static void loginUser(javafx.event.ActionEvent event, String Username, String Password) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
			preparedStatement = connection.prepareStatement("SELECT Password, Firstname, Lastname, VIPstatus FROM users WHERE Username = ?");
			preparedStatement.setString(1, Username);
			System.out.println("Query: " + preparedStatement.toString());
		    System.out.println("Username: " + Username);
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.isBeforeFirst()) {
				System.out.println("User not found in database");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Provided credentials are incorrect. ");
				alert.show();
			} else {
				while (resultSet.next()) {
					String retrievePassword = resultSet.getString("Password");
					String retrieveFirstname = resultSet.getString("FirstName");
					String retrieveLastname = resultSet.getString("LastName");
					String retrieveVIPstatus = resultSet.getString("VIPstatus");

					if (retrievePassword.equals(Password) && retrieveVIPstatus.equals("No")) {
						changeScene(event, "/View/Dashboard.fxml", "Welcome to the Data Analytics Hub", retrieveFirstname, retrieveLastname, "");
					} 
					
					else {
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
	
	public static void updateUserProfile(javafx.event.ActionEvent event, String username, String firstName, String lastName, String password) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    PreparedStatement checkUsernameStatement = null;
	    PreparedStatement updateProfileStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
	        connection.setAutoCommit(false);
	        // Check if the username exists
	        String checkUsernameQuery = "SELECT Username FROM users WHERE Username = ?";
	        checkUsernameStatement = connection.prepareStatement(checkUsernameQuery);
	        checkUsernameStatement.setString(1, username);
	        resultSet = checkUsernameStatement.executeQuery();
	        if (resultSet.next()) {

	        String updateQuery = "UPDATE users SET Firstname = ?, Lastname = ?, Password = ? WHERE Username = ?";
	        preparedStatement = connection.prepareStatement(updateQuery);
	        preparedStatement.setString(1, firstName);
	        preparedStatement.setString(2, lastName);
	        preparedStatement.setString(3, password);
	        preparedStatement.setString(4, username);

	        int rowsUpdated = preparedStatement.executeUpdate();
	        if (rowsUpdated > 0) {
	            showAlert(Alert.AlertType.INFORMATION, "Success", "User profile updated successfully.");
	        } else {
	            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user profile.");
	        }
	        } else {
	            showAlert(Alert.AlertType.ERROR, "Error", "Username does not exist. Please check your username.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the user profile.");    
	    } finally {
	        try {
	            if (connection != null) {
	                connection.setAutoCommit(true); 
	                connection.close();
	            }
	            if (preparedStatement != null) {
	                preparedStatement.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	private static void showAlert(Alert.AlertType alertType, String title, String message) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setContentText(message);
	    alert.showAndWait();
	}

	public static String findPostDetails(String postID) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
	        String selectQuery = "SELECT Content, Likes, Author, Shares, DateTime FROM posts WHERE PostID = ?";
	        preparedStatement = connection.prepareStatement(selectQuery);
	        preparedStatement.setString(1, postID);

	        resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            String content = resultSet.getString("Content");
	            int likes = resultSet.getInt("Likes");
	            String author = resultSet.getString("Author");
	            int shares = resultSet.getInt("Shares");
	            String dateTime = resultSet.getString("DateTime");

	            String postDetails = "Post Content: " + content + "\nLikes: " + likes + "\nAuthor: " + author + "\nShares: " + shares + "\nDate/Time: " + dateTime;
	            return postDetails;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching post details.");
	    } finally {
	        closeResources(connection, preparedStatement);
	    }

	    return null; // Return null if post is not found
	}
	public static String findPostDetailsbyLike(String postID) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
	        String selectQuery = "SELECT Content, Likes, Author, Shares, DateTime FROM posts WHERE Likes = ?";
	        preparedStatement = connection.prepareStatement(selectQuery);
	        preparedStatement.setString(1, postID);

	        resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            String content = resultSet.getString("Content");
	            int likes = resultSet.getInt("Likes");
	            String author = resultSet.getString("Author");
	            int shares = resultSet.getInt("Shares");
	            String dateTime = resultSet.getString("DateTime");

	            String postDetails = "Post Content: " + content + "\nLikes: " + likes + "\nAuthor: " + author + "\nShares: " + shares + "\nDate/Time: " + dateTime;
	            return postDetails;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching post details.");
	    } finally {
	        closeResources(connection, preparedStatement);
	    }

	    return null; // Return null if post is not found
	}
	 public static String findPostDetailsFromDB(String postID) {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
	        String postDetails = null;

	        try {
	            // Establish a database connection
		        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");

	            // Prepare a SQL query to retrieve post details based on the post ID
	            String selectQuery = "SELECT Content, Likes, Author, Shares, DateTime FROM posts WHERE PostID = ?";
	            preparedStatement = connection.prepareStatement(selectQuery);
	            preparedStatement.setString(1, postID);

	            // Execute the query and retrieve the results
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                // Extract post details from the result set
	                String content = resultSet.getString("Content");
	                int likes = resultSet.getInt("Likes");
	                String author = resultSet.getString("Author");
	                int shares = resultSet.getInt("Shares");
	                String dateTime = resultSet.getString("DateTime");

	                // Construct a string with the post details
	                postDetails = content + "," + likes + "," + author + "," + shares + "," + dateTime;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            closeResources(connection, preparedStatement);
	        }

	        return postDetails;
	    }
	    public static int getPostLikesCount() {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
	        int likesCount = 0;

	        try {
	            // Establish a database connection
	            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");

	            // Prepare a SQL query to count post likes
	            String countQuery = "SELECT COUNT(*) FROM post_likes";
	            preparedStatement = connection.prepareStatement(countQuery);

	            // Execute the query and retrieve the count
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                likesCount = resultSet.getInt(1);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            closeResources(connection, preparedStatement);
	        }

	        return likesCount;
	    }

	    public static int getUsersCount() {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        ResultSet resultSet = null;
	        int usersCount = 0;

	        try {
	            // Establish a database connection
	            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");

	            // Prepare a SQL query to count users
	            String countQuery = "SELECT COUNT(*) FROM users";
	            preparedStatement = connection.prepareStatement(countQuery);

	            // Execute the query and retrieve the count
	            resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                usersCount = resultSet.getInt(1);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            closeResources(connection, preparedStatement);
	        }

	        return usersCount;
	    }
	 public static List<String> getAllPostDataFromDB() {
		    Connection connection = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;
		    List<String> postDataList = new ArrayList<>();

		    try {
		        // Establish a database connection
		        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");

		        // Prepare a SQL query to retrieve all post data
		        String selectQuery = "SELECT PostID, Content, Likes, Author, Shares, DateTime FROM posts";
		        preparedStatement = connection.prepareStatement(selectQuery);

		        // Execute the query and retrieve the results
		        resultSet = preparedStatement.executeQuery();

		        // Iterate through the result set and add post data to the list
		        while (resultSet.next()) {
		            String postID = resultSet.getString("PostID");
		            String content = resultSet.getString("Content");
		            int likes = resultSet.getInt("Likes");
		            String author = resultSet.getString("Author");
		            int shares = resultSet.getInt("Shares");
		            String dateTime = resultSet.getString("DateTime");

		            // Construct a string with the post details and add it to the list
		            String postData = postID + "," + content + "," + likes + "," + author + "," + shares + "," + dateTime;
		            postDataList.add(postData);
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        closeResources(connection, preparedStatement);
		    }

		    return postDataList;
		}
	 public static void exportAllPostDataToCSV(String fileName, List<String> allPostData) {
		    // Specify the delimiter used in the CSV file
		    final String COMMA_DELIMITER = ",";
		    final String NEW_LINE_SEPARATOR = "\n";

		    FileWriter fileWriter = null;

		    try {
		        fileWriter = new FileWriter(fileName);

		        // Write the CSV file header
		        fileWriter.append("PostID,Content,Likes,Author,Shares,DateTime");
		        fileWriter.append(NEW_LINE_SEPARATOR);

		        // Write all post data to the CSV file
		        for (String postData : allPostData) {
		            fileWriter.append(postData);
		            fileWriter.append(NEW_LINE_SEPARATOR);
		        }

		        showAlert(Alert.AlertType.INFORMATION, "Success", "All post data exported to CSV file.");
		    } catch (IOException e) {
		        e.printStackTrace();
		        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while exporting post data.");
		    } finally {
		        try {
		            if (fileWriter != null) {
		                fileWriter.flush();
		                fileWriter.close();
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}


	public static String findPostlikeDetails(String postID) {
	    Connection connection = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;

	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
	        String selectQuery = "SELECT Likes FROM posts WHERE PostID = ?";
	        preparedStatement = connection.prepareStatement(selectQuery);
	        preparedStatement.setString(1, postID);

	        resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            int likes = resultSet.getInt("Likes");
	            
	            String postDetails = "\nLikes: " + likes;
	            return postDetails;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching post details.");
	    } finally {
	        closeResources(connection, preparedStatement);
	    }

	    return null; // Return null if post is not found
	}
	 public static void addPost(String content, String likes, String postId, String author, String dateTime, String shares) {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;

	        try {
	            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
	            String insertQuery = "INSERT INTO posts (Content, Likes, PostID, Author, DateTime, Shares) VALUES (?, ?, ?, ?, ?, ?)";
	            preparedStatement = connection.prepareStatement(insertQuery);
	            preparedStatement.setString(1, content);
	            preparedStatement.setString(2, likes);
	            preparedStatement.setString(3, postId);
	            preparedStatement.setString(4, author);
	            preparedStatement.setString(5, dateTime);
	            preparedStatement.setString(6, shares);

	            int rowsInserted = preparedStatement.executeUpdate();
	            if (rowsInserted > 0) {
	                showAlert(Alert.AlertType.INFORMATION, "Success", "Post added successfully.");
	            } else {
	                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add the post.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert(Alert.AlertType.ERROR, "Error", "There is a post exist with this ID.");
	        } finally {
	            closeResources(connection, preparedStatement);
	        }
	    }
	 public static List<PieChart.Data> getChartDataFromDatabase() {
		    Connection connection = null;
		    PreparedStatement preparedStatement = null;
		    ResultSet resultSet = null;
		    List<PieChart.Data> chartData = new ArrayList<>();

		    try {
		        // Establish a database connection
		        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");

		        // Prepare a SQL query to retrieve data for the pie chart
		     //   String selectQuery = "SELECT category, count FROM your_table"; // Update with your database table and columns
		        String selectQuery = "SELECT Likes, Shares FROM posts";

		        preparedStatement = connection.prepareStatement(selectQuery);

		        // Execute the query and retrieve the results
		        resultSet = preparedStatement.executeQuery();

		        // Iterate through the result set and add data to the chartData list
		        while (resultSet.next()) {
		            String Like = resultSet.getString("Likes");
		            int Share = resultSet.getInt("Shares");
		            chartData.add(new PieChart.Data(Like, Share));
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching chart data from the database.");
		    } finally {
		        closeResources(connection, preparedStatement);
		    }

		    return chartData;
		}

	 public static void removePost(String postId) {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;

	        try {
	            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
	            String deleteQuery = "DELETE FROM posts WHERE PostID = ?";
	            preparedStatement = connection.prepareStatement(deleteQuery);
	            preparedStatement.setString(1, postId);

	            int rowsDeleted = preparedStatement.executeUpdate();
	            if (rowsDeleted > 0) {
	                showAlert(Alert.AlertType.INFORMATION, "Success", "Post removed successfully.");
	            } else {
	                showAlert(Alert.AlertType.ERROR, "Error", "Failed to remove the post.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while removing the post.");
	        } finally {
	            closeResources(connection, preparedStatement);
	        }
	    }
	 private static void closeResources(Connection connection, PreparedStatement preparedStatement) {
	        if (preparedStatement != null) {
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
//	 public static int[] getPostSharesCount() {
//		    Connection connection = null;
//		    PreparedStatement preparedStatement = null;
//		    ResultSet resultSet = null;
//		    int[] shareCounts = new int[3]; // To store counts for three share groups
//
//		    try {
//		        // Establish a database connection
//		        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dataanalyticshub_db", "root", "root");
//
//		        // Prepare SQL queries to count shares in different groups
//		        String query0To99 = "SELECT COUNT(*) FROM posts WHERE Shares >= 0 AND Shares <= 99";
//		        String query100To999 = "SELECT COUNT(*) FROM posts WHERE Shares >= 100 AND Shares <= 999";
//		        String query1000OrMore = "SELECT COUNT(*) FROM posts WHERE Shares >= 1000";
//
//		        preparedStatement = connection.prepareStatement(query0To99);
//		        resultSet = preparedStatement.executeQuery();
//		        if (resultSet.next()) {
//		            shareCounts[0] = resultSet.getInt(1);
//		        }
//
//		        preparedStatement = connection.prepareStatement(query100To999);
//		        resultSet = preparedStatement.executeQuery();
//		        if (resultSet.next()) {
//		            shareCounts[1] = resultSet.getInt(1);
//		        }
//
//		        preparedStatement = connection.prepareStatement(query1000OrMore);
//		        resultSet = preparedStatement.executeQuery();
//		        if (resultSet.next()) {
//		            shareCounts[2] = resultSet.getInt(1);
//		        }
//
//		    } catch (SQLException e) {
//		        e.printStackTrace();
//		    } finally {
//		        closeResources(connection, preparedStatement);
//		    }
//
//		    return shareCounts;
//		}
	 

//	 public static void exportPostToCSV(String postID, String fileName, String content, int likes, String author, int shares, String dateTime) {
//		    try (PrintWriter writer = new PrintWriter(new File(fileName + ".csv"))) {
//		        StringBuilder sb = new StringBuilder();
//		        sb.append("PostID,Content,Likes,Author,Shares,DateTime\n");
//		        sb.append(postID).append(",").append(content).append(",").append(likes).append(",").append(author).append(",").append(shares).append(",").append(dateTime).append("\n");
//		        writer.write(sb.toString());
//		        showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Post details exported to " + fileName + ".csv");
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		        showAlert(Alert.AlertType.ERROR, "Export Error", "An error occurred while exporting post details.");
//		    }
//		}
	 
	 

	 public static int[] getPostSharesCount() {
	     int[] shareCounts = new int[3]; // To store counts for three share groups
	     Random random = new Random();

	     shareCounts[0] = random.nextInt(100); // 0-99 shares
	     shareCounts[1] = random.nextInt(900) + 100; // 100-999 shares
	     shareCounts[2] = random.nextInt(9000) + 1000; // 1000+ shares

	     return shareCounts;
	 }

	 public static void exportPostToCSV(String postID, String fileName, String content, int likes, String author, int shares, String dateTime) {
	        // Specify the delimiter used in CSV file
	        final String COMMA_DELIMITER = ",";
	        final String NEW_LINE_SEPARATOR = "\n";

	        // CSV file header
	        final String FILE_HEADER = "PostID,Content,Likes,Author,Shares,DateTime";

	        FileWriter fileWriter = null;

	        try {
	            fileWriter = new FileWriter(fileName);

	            // Write the CSV file header
	            fileWriter.append(FILE_HEADER);
	            fileWriter.append(NEW_LINE_SEPARATOR);

	            // Write post details to the CSV file
	            fileWriter.append(postID);
	            fileWriter.append(COMMA_DELIMITER);
	            fileWriter.append(content);
	            fileWriter.append(COMMA_DELIMITER);
	            fileWriter.append(String.valueOf(likes));
	            fileWriter.append(COMMA_DELIMITER);
	            fileWriter.append(author);
	            fileWriter.append(COMMA_DELIMITER);
	            fileWriter.append(String.valueOf(shares));
	            fileWriter.append(COMMA_DELIMITER);
	            fileWriter.append(dateTime);
	            fileWriter.append(NEW_LINE_SEPARATOR);

	            showAlert(Alert.AlertType.INFORMATION, "Success", "Post details exported to CSV file.");
	        } catch (IOException e) {
	            e.printStackTrace();
	            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while exporting post details.");
	        } finally {
	            try {
	                if (fileWriter != null) {
	                    fileWriter.flush();
	                    fileWriter.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	 
	
}