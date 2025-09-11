package indigo8solutions;

import indigo8solutions.dao.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.image.Image;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Database.createTables();
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = new Scene(root);
        Image icon = new Image(App.class.getResource("/images/icon.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(800);
        primaryStage.setTitle("Survey Form");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}