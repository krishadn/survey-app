package kpesclient;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import kpesclient.dao.QuestionDAO;
import kpesclient.model.Question;

public class MainController {

    private ArrayList<Question> questions = new ArrayList<>();

    @FXML
    public void startSurvey(ActionEvent event) throws IOException {

        questions = QuestionDAO.getAllQuestions();

        if (questions.isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Under Construction");
            alert.setHeaderText("Apologies");
            alert.setContentText("The Application is under maintenance. Please come back later.");
            alert.showAndWait();
            return;
        }

        Parent root = FXMLLoader.load(App.class.getResource("/view/survey.fxml"));
        Stage stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
