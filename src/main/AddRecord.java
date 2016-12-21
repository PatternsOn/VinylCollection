package main;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.geometry.*;

public class AddRecord {
	
	static String dbUrl = "jdbc:mysql://localhost:3306/vinylcollection?useSSL=true";
	static String user = "root";
	static String password = enter password here;
	
    public static void display(String title) {
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
        
        // Add record
        Label addRecord = new Label("Add Record");
        GridPane.setConstraints(addRecord, 0, 0);
        
        // Label
        Label label = new Label("Label");
        GridPane.setConstraints(label, 0, 1);
        TextField labelField = new TextField();
        labelField.setPromptText("Label");
        GridPane.setConstraints(labelField, 0, 2);
        
        
        // Id
        Label id = new Label("Record ID");
        GridPane.setConstraints(id, 1, 1);
        TextField idField = new TextField();
        idField.setPromptText("Record ID");
        GridPane.setConstraints(idField, 1, 2);
        
        // Owner
        Label owner = new Label("Owner");
        GridPane.setConstraints(owner, 2, 1);
        TextField ownerField = new TextField();
        ownerField.setPromptText("Owner");
        GridPane.setConstraints(ownerField, 2, 2);
        
        // CreateButton
        Button createButton = new Button("Create Record");
        GridPane.setConstraints(createButton, 3, 2);
        createButton.setOnAction(e -> {
			// 1: Get a connection to a database
			try {
				Connection connection = DriverManager.getConnection(dbUrl, user, password);

				String sql = "INSERT INTO record (releaseID, label, owner) VALUES (? , ?, ?);";
		
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
	
				preparedStatement.setString(1, idField.getText());
				preparedStatement.setString(2, labelField.getText());
				preparedStatement.setString(3, ownerField.getText());
	
				preparedStatement.executeUpdate();

	        	AddTrack.display("Add Tracks", idField.getText());
	        	
			}
			
        	catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
					
        });
        
     // Done button
        Button goBack = new Button("Go Back");
        GridPane.setConstraints(goBack, 0, 3);
        goBack.setOnAction(e -> {
        	window.close();
        });
        
        grid.getChildren().addAll(addRecord, label, id, owner, labelField, idField, ownerField, createButton, goBack);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(grid);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(borderPane, 800, 600);
        window.setScene(scene);
        window.showAndWait();
    }

}
