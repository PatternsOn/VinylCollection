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

public class EditTrack {
	static String dbUrl = "jdbc:mysql://localhost:3306/vinylcollection?useSSL=true";
	static String user = "root";
	static String password = enter password here;
	
	
    public static void display(String title, String trackname, String artist, String remix, String trackID) {
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

        // Layout
        Label titleLabel = new Label("Vinyl Collection");
        GridPane.setConstraints(titleLabel, 0, 0);
        
        // Track
        Label trackLabel = new Label("Track");
        GridPane.setConstraints(trackLabel, 0, 1);
        TextField trackField = new TextField();
        //trackField.setPromptText("Track");
        trackField.setText(trackname);
        GridPane.setConstraints(trackField, 0, 2);
        
        // Artist
        Label artistLabel = new Label("Artist");
        GridPane.setConstraints(artistLabel, 1, 1);
        TextField artistField = new TextField();
        //artistField.setPromptText("Artist");
        artistField.setText(artist);
        GridPane.setConstraints(artistField, 1, 2);
        
        // Remix
        Label remixLabel = new Label("Remix");
        GridPane.setConstraints(remixLabel, 2, 1);
        TextField remixField = new TextField();
        //remixField.setPromptText("Remix");
        remixField.setText(remix);
        GridPane.setConstraints(remixField, 2, 2);
        
        // Update
        Button updateButton = new Button("Update");
        GridPane.setConstraints(updateButton, 3, 2);
        updateButton.setOnAction(e -> {
        	
        	// Get a connection to a database
			try {
				Connection connectionSQL = DriverManager.getConnection(dbUrl, user, password);
				PreparedStatement preparedStatement;
				
				
				String sql;
				String trackFieldUpdate = trackField.getText();
				String artistFieldUpdate = artistField.getText();
				String remixFieldUpdate = remixField.getText();

				sql = "UPDATE track SET trackname=?, artist=?, remix=? WHERE trackID="+trackID+";";
				preparedStatement = connectionSQL.prepareStatement(sql);
				
			
				preparedStatement.setString(1, trackFieldUpdate);
				preparedStatement.setString(2, artistFieldUpdate);
				preparedStatement.setString(3, remixFieldUpdate);
				preparedStatement.executeUpdate();
					
			}
			
        	catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        });
        
        // Done button
        Button doneButton = new Button("Done");
        GridPane.setConstraints(doneButton, 0, 3);
        doneButton.setOnAction(e -> window.close());

        grid.getChildren().addAll(titleLabel, trackLabel, artistLabel, remixLabel, 
        		trackField, artistField, remixField, updateButton, doneButton);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(grid);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(borderPane, 800, 600);
        window.setScene(scene);
        window.showAndWait();
    }

}
