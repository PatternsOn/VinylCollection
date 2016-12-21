package main;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.*;

public class AddTrack {
	static String dbUrl = "jdbc:mysql://localhost:3306/vinylcollection?useSSL=true";
	static String user = "root";
	static String password = enter password here;
	
    public static void display(String title, String recordID) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(600);

        // Grid Properties
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Track
        Label track = new Label("Track");
        GridPane.setConstraints(track, 0, 1);
        TextField trackField = new TextField();
        trackField.setPromptText("Track");
        GridPane.setConstraints(trackField, 0, 2);
        
        // Artist
        Label artist = new Label("Artist");
        GridPane.setConstraints(artist, 1, 1);
        TextField artistField = new TextField();
        artistField.setPromptText("Artist");
        GridPane.setConstraints(artistField, 1, 2);
        
        // Remix
        Label remix = new Label("Remix");
        GridPane.setConstraints(remix, 2, 1);
        TextField remixField = new TextField();
        remixField.setPromptText("Remix");
        GridPane.setConstraints(remixField, 2, 2);
        
        // Add track
        Button addTrack = new Button("Add track");
        GridPane.setConstraints(addTrack, 3, 2);
        addTrack.setOnAction(e -> {
        	
        	// Get a connection to a database
			try {
				Connection connectionSQL = DriverManager.getConnection(dbUrl, user, password);
				PreparedStatement preparedStatement = null;
				ResultSet result = null;
				String sql;
				String trackID = null;
				String trackField2 = trackField.getText();
				String artistField2 = artistField.getText();
				String remixField2 = remixField.getText();
				
				
				
				// Add track
				sql = "INSERT INTO track (trackname, artist, remix) VALUES (? , ?, ?);";
				preparedStatement = connectionSQL.prepareStatement(sql);
				preparedStatement.setString(1, trackField2);
				preparedStatement.setString(2, artistField2);
				preparedStatement.setString(3, remixField2);
				preparedStatement.executeUpdate();
				
				// Get trackID
				sql = "SELECT trackID FROM track WHERE trackname=? AND artist=? AND remix=?;";
				preparedStatement = connectionSQL.prepareStatement(sql);
				preparedStatement.setString(1, trackField2);
				preparedStatement.setString(2, artistField2);
				preparedStatement.setString(3, remixField2);
				result = preparedStatement.executeQuery();
				
				while (result.next()) {
					trackID = result.getString("trackID");
					System.out.println(trackID); //(trackId);
				}
				
				// Add track to Appears On list
				sql = "INSERT INTO appears_on (trackID, releaseID) VALUES (? , ?);";
				preparedStatement = connectionSQL.prepareStatement(sql);
				preparedStatement.setString(1, trackID);
				preparedStatement.setString(2, recordID);
				preparedStatement.executeUpdate();
				
				connectionSQL.close();
				
			}
			
        	catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        });
        
        // Done button
        Button done = new Button("Done");
        GridPane.setConstraints(done, 0, 3);
        done.setOnAction(e -> {
        	
        	window.close();
        });

        grid.getChildren().addAll(track, artist, remix, trackField, artistField, remixField, addTrack, done);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(grid);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(borderPane, 800, 600);
        window.setScene(scene);
        window.showAndWait();
    }

}

