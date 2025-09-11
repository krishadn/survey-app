package indigo8;
import indigo8.dao.Database;
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
        Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
        Scene scene = new Scene(root);
        Image icon = new Image(App.class.getResource("/images/icon.jpg").toExternalForm());
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(768);
        primaryStage.setTitle("Survey Maintenance");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}