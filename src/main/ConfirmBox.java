package main;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ConfirmBox  {
	
	static boolean answer;
	
    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        // Create two buttons
        Button yesButton = new Button("YES");
        Button noButton = new Button("NO");
        
        yesButton.setOnAction(e -> {
        	answer = true; 
        	window.close();
        });
        
        noButton.setOnAction(e -> {
        	answer = false;
        	window.close();
        });

        HBox layout = new HBox(10);
        layout.setPadding(new Insets(10, 10, 10 , 10));
        layout.getChildren().addAll(label, yesButton, noButton );
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    }

}
