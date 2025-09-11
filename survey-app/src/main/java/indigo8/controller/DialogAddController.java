package indigo8.controller;

import java.util.ArrayList;
import java.util.List;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import indigo8.dao.OptionDAO;
import indigo8.dao.QuestionDAO;
import indigo8.model.Option;
import indigo8.model.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DialogAddController {

    private Question question;
    private Boolean saved = false;

    @FXML
    private VBox optionVBox;

    @FXML
    private TextArea questionText;

    
    @FXML
    public void handleAddOption(ActionEvent event) {

        TextField content = new TextField();
        content.setMinWidth(500);
        content.setMinHeight(40);

        Button deleteBtn = new Button("Delete");
        FontAwesomeIconView trashIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        trashIcon.setSize("15");
        deleteBtn.setGraphic(trashIcon);
        deleteBtn.setMinHeight(40);
        deleteBtn.setMinWidth(100);
        deleteBtn.setOnAction(e -> {
            HBox thisHbox = (HBox)((Node)e.getSource()).getParent();
            VBox thisVbox = (VBox) thisHbox.getParent();
            thisVbox.getChildren().remove(thisHbox);
        });
        
        Region region = new Region();

        HBox hbox = new HBox(10, content, region, deleteBtn);
        HBox.setHgrow(region, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);

        optionVBox.getChildren().add(hbox);
    
    }

    @FXML
    public void handleSave(ActionEvent event) {

        // to save the question first
        String questionContent = questionText.getText();


        Scene scene = ((Node)event.getSource()).getScene();
        Stage stage = (Stage)scene.getWindow();
        String title = stage.getTitle();
        int position = Integer.parseInt(title.split("#")[1]);
        
        Question question = new Question(questionContent, position);
        System.out.println(question);

        int qid = QuestionDAO.createQuestion(question);
        this.question = new Question(qid, questionContent, position);
        System.out.println("QID: " + qid);
        
        Parent root = (Parent) scene.getRoot();
        List<TextField> textFields = getAllTextFields(root);
        for (TextField tf: textFields){
            if (tf.getText().isBlank() || tf.getText().isEmpty()) {
                continue;
            }
            Option option = new Option(tf.getText(), 0, qid);
            OptionDAO.createOption(option);
        }
        this.saved = true;

        stage.close();

    }

    @FXML
    public void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


    // utility functions
    private static List<TextField> getAllTextFields(Parent root) {
        List<TextField> textFields = new ArrayList<>();
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                textFields.add((TextField) node);
            } else if (node instanceof Parent) {
                textFields.addAll(getAllTextFields((Parent) node));
            }
        }
        return textFields;
    }

    public Question getSavedQuestion() {
        return question;
    }

    public Boolean isSaved() {
        return saved;
    }

}
