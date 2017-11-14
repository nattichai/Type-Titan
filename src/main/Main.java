package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
 
public class Main extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception{
    	
        Parent root = FXMLLoader.load(getClass().getResource("MainStage.fxml"));
 
        stage.setTitle("Type Master");
        	stage.setScene(new Scene(root));
        	stage.setResizable(false);
        	stage.show();
        	stage.setOnCloseRequest(e -> {
        		if(MyController.getTimeline() != null)
        			MyController.getTimeline().stop();
        		try {	MyController.saveGame();		} catch (Exception e1) {}
        		new Alert(AlertType.NONE, "GAME SAVED!!", ButtonType.OK).showAndWait();
        	});
    }
    
}