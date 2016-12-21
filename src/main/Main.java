package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;
    TableView<Record> table;
    
    private String dbUrl = "jdbc:mysql://localhost:3306/vinylcollection?useSSL=true";
	private String user = "root";
	private String password = enter password here;
	private String choice;
	private String search;
	private String trackname;
	private String artist;
	private String remix;
	private String label;
	private String recordID;
	private String trackID;
	private String owner;
	private String forSale;
	private String sql;
//	private ArrayList<Integer> listOfTrackID = new ArrayList<Integer>();  
	ObservableList<Record> records = FXCollections.observableArrayList(); 
	//Connection myConn = null;
	Connection connection; //= DriverManager.getConnection(dbUrl, user, password);
	PreparedStatement preparedStatement = null;
	ResultSet result = null;
	
	java.sql.CallableStatement myStmt = null;
	ResultSet myRs = null;

    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Vinyl Collection");
        window.setOnCloseRequest(e -> { 
        	e.consume();
        	closeProgram();
        	});
        
        // Track column
        TableColumn<Record, String> trackColumn = new TableColumn<>("Track");
        trackColumn.setMinWidth(150);
        trackColumn.setCellValueFactory(new PropertyValueFactory<>("track"));
        
        // Artist column
        TableColumn<Record, String> artistColumn = new TableColumn<>("Artist");
        artistColumn.setMinWidth(150);
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        
        // Remix column
        TableColumn<Record, String> remixColumn = new TableColumn<>("Remix");
        remixColumn.setMinWidth(150);
        remixColumn.setCellValueFactory(new PropertyValueFactory<>("remix"));
        
        // Label column
        TableColumn<Record, String> labelColumn = new TableColumn<>("Label");
        labelColumn.setMinWidth(150);
        labelColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
        
        // RecordID column
        TableColumn<Record, String> recordIDColumn = new TableColumn<>("ReleaseID");
        recordIDColumn.setMinWidth(150);
        recordIDColumn.setCellValueFactory(new PropertyValueFactory<>("recordID"));
        
        // Owner column
        TableColumn<Record, String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setMinWidth(150);
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        
        // For Sale column
        TableColumn<Record, String> forSaleColumn = new TableColumn<>("ForSale");
        forSaleColumn.setMinWidth(50);
        forSaleColumn.setCellValueFactory(new PropertyValueFactory<>("forSale"));
        
        table = new TableView<>();
        table.setItems(getRecords());
        table.getColumns().addAll(trackColumn, artistColumn, remixColumn, labelColumn, recordIDColumn, ownerColumn, forSaleColumn);
        
        
        // Grid Properties
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        // Layout
        Label title = new Label("Vinyl Collection");
        GridPane.setConstraints(title, 0, 0);
        
        // Add record button
        Button addRecord = new Button("Add New Record");
        GridPane.setConstraints(addRecord, 0, 1);
        addRecord.setOnAction(e -> AddRecord.display("Add Record"));
        
        // ChoiceBox searchBy
        ChoiceBox<String> searchBy = new ChoiceBox<>();
        searchBy.getItems().addAll("Trackname", "Artist", "Remix", "Label", "releaseID",  "Owner", "For Sale");
        searchBy.setValue("Artist");
        GridPane.setConstraints(searchBy, 0, 2);
        
        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search");
        GridPane.setConstraints(searchField, 1, 2);
        
        // Search button
        Button searchButton = new Button("Search");
        GridPane.setConstraints(searchButton, 2, 2);
        searchButton.setOnAction(e -> searchButtonClicked(searchBy, searchField));
        
        // Edit button
        Button editButton = new Button("Edit Track");
        GridPane.setConstraints(editButton, 3, 2);
        editButton.setOnAction(e -> { 
        	try {
				connection = DriverManager.getConnection(dbUrl, user, password);
			
        	
        	trackname = table.getSelectionModel().getSelectedItem().getTrack();
        	artist = table.getSelectionModel().getSelectedItem().getArtist();
        	remix = table.getSelectionModel().getSelectedItem().getRemix();
        	
        	sql = "SELECT trackID "
        			+ "FROM track "
        			+ "WHERE trackname=? "
        			+ "AND artist=? "
        			+ "AND remix=?;";
        	
        	preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, trackname);
			preparedStatement.setString(2, artist);
			preparedStatement.setString(3, remix);
			
			result = preparedStatement.executeQuery();
			
			while (result.next()) {
				trackID = result.getString("trackID");
			}
        	
        	EditTrack.display("Edit", trackname, artist, remix, trackID);
        
        	} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        
        // Delete button
        Button deleteRecordButton = new Button("Delete Record");
        GridPane.setConstraints(deleteRecordButton, 4, 2);
        deleteRecordButton.setOnAction(e -> {
        	boolean delete;
        	e.consume();
        	delete = deleteRecord();
        	if (delete) {
	        	ObservableList<Record> recordsSelected, allRecords; 
	        	allRecords = table.getItems();
	        	recordsSelected = table.getSelectionModel().getSelectedItems();
	        	
	        	recordID = table.getSelectionModel().getSelectedItem().getRecordID();
	        	
	        	
	        	// Med sql hämta alla trackID i appears_on
	        	try {
	        	sql = "SELECT trackID FROM appears_on WHERE releaseID=?;";
	        	
	        	preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, recordID);
				
				result = preparedStatement.executeQuery();
				
	        	while (result.next()) {
	        		trackID = result.getString("trackID");
	        		
	        		sql = "DELETE FROM track WHERE trackID =?;";
	        		
	        		preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, trackID);
					
					preparedStatement.executeUpdate();
				}
	        	
	        	sql = "DELETE FROM record WHERE releaseID =?;";
        		
        		preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, recordID);
				
				preparedStatement.executeUpdate();
	        	
//	        	recordsSelected.forEach(allRecords::remove);
	        	
	        	// Insert the delete algorithm
	        	
	        	} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	
	        	
	        	searchButtonClicked(searchBy, searchField);
        	}
        });
        
        grid.getChildren().addAll(title, addRecord, searchBy, searchField, searchButton, editButton, deleteRecordButton);
        

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(grid);
        borderPane.setCenter(table);
        Scene scene = new Scene(borderPane, 1000, 600);
        window.setScene(scene);
        window.show();
        
    }

    private void searchButtonClicked(ChoiceBox<String> searchBy, TextField searchField ) {
    	getChoice(searchBy);
    	getSearch(searchField);
    	records.clear();

		try {
			connection = DriverManager.getConnection(dbUrl, user, password);
			
			if ((choice == "Artist") || (choice == "Trackname") || (choice == "Remix"))
				choice = "track." + choice;
			else 
				choice = "record." + choice;
			
			sql = "SELECT track.trackname, track.artist, track.remix, record.label, record.releaseID, record.owner, record.for_sale "
					+ "FROM track "
					+ "JOIN appears_on ON track.trackID=appears_on.trackID "
					+ "JOIN record ON appears_on.releaseID=record.releaseID "
					+ "WHERE "+choice+" LIKE?;";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "%"+search+"%");

			result = preparedStatement.executeQuery();

			while (result.next()) {
				trackname = result.getString("trackname");
				artist = result.getString("artist");
				remix = result.getString("remix");
				label = result.getString("label");
				recordID = result.getString("releaseID");
				owner = result.getString("owner");
				forSale = result.getString("for_sale");
				addRecord();
			}
			table.setItems(getRecords());
	
		}
		
    	catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		
	}

	private void addRecord() {
    	records.add(new Record(trackname, artist, remix, label , recordID, owner, forSale));
    }
    
    // Get all of the Records
    private ObservableList<Record> getRecords() {
    	return records;
    }
    
    private void getChoice(ChoiceBox<String> searchBy) {
		this.choice = searchBy.getValue();
		System.out.println(choice);
	}
    
    private void getSearch(TextField searchField) {
		this.search = searchField.getText();
		System.out.println(search);
	}

	private void closeProgram() {
    	Boolean answer = ConfirmBox.display("Exit Program", "Are you sure you want to exit?");
    	if (answer)
    		window.close();
    }

	private boolean deleteRecord() {
    	Boolean answer = ConfirmBox.display("Delete", "Are you sure you want to delete the record?");
    	if (answer)
    		return true;
    	else
    		return false;
    }
}
 