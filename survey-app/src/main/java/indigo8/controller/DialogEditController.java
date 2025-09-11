package indigo8.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DialogEditController {

    private Question question;
    private ArrayList<Option> existingOptions;
    private ArrayList<Integer> deletedOptions = new ArrayList<>();
    private int TEMP_OPTID = 99999;
    private Boolean saved = false;

    @FXML
    private TextArea questionText;

    @FXML
    private VBox optionVBox;


    @FXML
    public void handleAddOption(ActionEvent event) {
        Option newOption = new Option(TEMP_OPTID, "", 0, question.getQid());
        addOption(newOption);
    }

    @FXML
    public void handleSave(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Changes");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to save changes?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // handle change in question content
            String questionInUI = questionText.getText();

            if (!questionInUI.equals(question.getContent()) && !(questionInUI.isBlank() || questionInUI.isEmpty())) {
                QuestionDAO.updateQuestionContent(question.getQid(), questionInUI);
                question.setContent(questionInUI);
            }

            // handle deleted existing options
            deletedOptions.forEach(optid -> OptionDAO.deleteOptionByOptId(optid));

            // handle options not deleted
            Scene scene = ((Node)event.getSource()).getScene();
            Stage stage = (Stage)scene.getWindow();
            Parent root = (Parent)scene.getRoot();
            List<TextField> textFields = getAllTextFields(root);

            for (TextField tf: textFields){

                if (tf.getText().isBlank() || tf.getText().isEmpty()) {
                    continue;
                }

                int optid = Integer.parseInt(tf.getId());
                System.out.println(tf.getText() + " " + optid);

                // handle existing and new options
                if (!(optid == TEMP_OPTID)) {
                    OptionDAO.updateOptionContent(optid, tf.getText());
                } else {
                    Option option = new Option(tf.getText(), 0, question.getQid());
                    OptionDAO.createOption(option);
                }
                    
            }

            // change save state and close dialog box
            saved = true;

            stage.close();
        }

    }

    @FXML
    public void handleCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


    public void setQuestion(Question question) {
        this.question = question;
        updateUI();
    }

    public void setExistingOptions(ArrayList<Option> options) {
        this.existingOptions = options;
        updateUI();
    }


    private void updateUI() {
        if (questionText != null && question != null) {
            questionText.setText(question.getContent());
        }

        if (optionVBox != null && existingOptions != null && !existingOptions.isEmpty()) {
            
            existingOptions.forEach((option) -> addOption(option));
        }
    }

    private void addOption(Option option) {

        TextField content = new TextField();
        content.setMinWidth(500);
        content.setMinHeight(40);
        content.setText(option.getContent());
        content.setId(String.valueOf(option.getOptid()));

        Button deleteBtn = new Button("Delete");
        FontAwesomeIconView trashIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        trashIcon.setSize("15");
        deleteBtn.setGraphic(trashIcon);
        deleteBtn.setMinHeight(40);
        deleteBtn.setMinWidth(100);
        deleteBtn.setOnAction(e -> handleDeleteOption(e));
        
        Region region = new Region();

        HBox hbox = new HBox(10, content, region, deleteBtn);
        HBox.setHgrow(region, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setId(String.valueOf(option.getOptid()));

        optionVBox.getChildren().add(hbox);


    }

    private void handleDeleteOption(ActionEvent e) {

        HBox thisHbox = (HBox)((Node)e.getSource()).getParent();
        int optid = Integer.parseInt(thisHbox.getId());

        if (!(optid == TEMP_OPTID)) {
            deletedOptions.add(optid);
        }

        VBox thisVbox = (VBox) thisHbox.getParent();
        thisVbox.getChildren().remove(thisHbox);
                
            
        
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


    public Boolean isSaved(){
        return saved;
    }

    public Question getQuestion() {
        return question;
    }

    
}
