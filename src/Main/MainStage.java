package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
 
public class MainStage  extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainStage.fxml"));
 
        primaryStage.setTitle("Type Master");
        	primaryStage.setScene(new Scene(root));
        	primaryStage.setResizable(false);
        	primaryStage.show();
        	primaryStage.setOnCloseRequest(e -> {
        		if(MyController.timeline != null)
        			MyController.timeline.stop();
        		try {
					MyController.saveGame();
				} catch (Exception e1) {}
        		new Alert(AlertType.NONE, "GAME SAVED!!", ButtonType.OK).showAndWait();
        	});
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}