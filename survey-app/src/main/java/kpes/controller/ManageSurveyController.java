package kpes.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kpes.App;
import kpes.dao.OptionDAO;
import kpes.dao.QuestionDAO;
import kpes.model.Option;
import kpes.model.Question;
import kpes.model.QuestionComparator;

public class ManageSurveyController {

    private Scene scene;
    private Parent root;
    private ArrayList<Question> questions;
    private int currentPosition = 1;

    @FXML
    private Button backToDashBtn;

    @FXML
    private Button addQuestionBtn;

    @FXML
    private VBox questionParent;

    @FXML
    private void initialize() {

        loadDataAsync();
        
        backToDashBtn.setOnAction(event -> {
            
            try {
                root = FXMLLoader.load(App.class.getResource("/view/dashboard.fxml"));
                scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


    }

    private void loadDataAsync() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                questions = QuestionDAO.getAllQuestions();
                return null;
            }
        };

        task.setOnSucceeded(e -> updateUIWithData());
        new Thread(task).start();
    }

    private void updateUIWithData() {

        questions.sort(new QuestionComparator());

        for (Question question: questions) {
            addQuestionToUI(question);
        }

        addQuestionBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("/view/dialog-add.fxml"));
                DialogPane dialogPane = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Stage dialogStage = new Stage();
                Image icon = new Image(App.class.getResource("/images/icon.png").toExternalForm());
                dialogStage.getIcons().add(icon);
                dialogStage.setTitle(String.format("Question #%d", currentPosition));
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.initOwner(stage);
                dialogStage.setScene(new Scene(dialogPane));

                DialogAddController controller = loader.getController();

                dialogStage.showAndWait();

                if (controller.isSaved()) {
                    Question savedQuestion = controller.getSavedQuestion();
                    addQuestionToUI(savedQuestion);
                }
                

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }  
    
    private void addQuestionToUI(Question question) {
        currentPosition = question.getPosition() + 1;
        Label content = new Label(String.format("Question#%d: %s", question.getPosition(),question.getContent()));
        content.setWrapText(true);
        content.setMinWidth(500);
        content.setMinHeight(40);

        Button deleteBtn = new Button();
        FontAwesomeIconView trashIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        trashIcon.setSize("15");
        deleteBtn.setGraphic(trashIcon);
        deleteBtn.setMinHeight(40);
        deleteBtn.setMinWidth(50);
        deleteBtn.setId(String.valueOf(question.getPosition()));
        deleteBtn.setOnAction(e -> handleDelete(e));

        Button editBtn = new Button();
        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
        editIcon.setSize("15");
        editBtn.setGraphic(editIcon);
        editBtn.setMinHeight(40);
        editBtn.setMinWidth(50);
        editBtn.setOnAction(e -> {
            try {
                handleEdit(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        Region region = new Region();

        HBox hbox = new HBox(10, content, region, editBtn, deleteBtn);
        HBox.setHgrow(region, Priority.ALWAYS);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(10, 20, 0, 20));
        hbox.setId(String.valueOf(question.getQid()));
        
        Separator separator = new Separator(Orientation.HORIZONTAL);
        questionParent.getChildren().addAll(hbox,separator);

    }

    private void handleDelete(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Do you want to delete question?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Button button = (Button)event.getSource();
            int position = Integer.parseInt(button.getId());

            HBox thisHbox = (HBox)((Node)button).getParent();
            int qid = Integer.parseInt(thisHbox.getId());
            
            QuestionDAO.deleteQuestionByQid(qid, position);

            VBox thisVbox = (VBox) thisHbox.getParent();
            ObservableList<Node> children = thisVbox.getChildren();
            int hBoxIndex = children.indexOf(thisHbox);
            
            for (Node node: children.subList(hBoxIndex + 1, children.size())) {
                if (node instanceof HBox) {
                    HBox sibling = (HBox) node;
                    for (Node item: sibling.getChildren()) {
                        if (item instanceof Label) { 
                            Label label = (Label) item;
                            String questionContent = label.getText().split(": ")[1];
                            int newPosition = position++;
                            label.setText(String.format("Question#%d: %s", newPosition, questionContent));
                        }
                    }
                }

            }
            currentPosition = position;
            thisVbox.getChildren().remove(thisVbox.getChildren().indexOf(thisHbox) + 1);
            thisVbox.getChildren().remove(thisHbox);
        }

    }



    private void handleEdit(ActionEvent event) throws IOException {
        // to get qid from hbox
        HBox thisHbox = (HBox)((Node)event.getSource()).getParent();
        int qid = Integer.parseInt(thisHbox.getId());

        // get question and options data from db
        Question question = QuestionDAO.getQuestionByQid(qid);
        ArrayList<Option> options = OptionDAO.getOptionsByQid(qid);

        // create dialog box
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/view/dialog-edit.fxml"));
        DialogPane dialogPane = loader.load();
        DialogEditController controller = loader.getController();
        controller.setQuestion(question);
        controller.setExistingOptions(options);

        Stage stage = (Stage) thisHbox.getScene().getWindow();
        Stage dialogStage = new Stage();
        Image icon = new Image(App.class.getResource("/images/icon.png").toExternalForm());
        dialogStage.getIcons().add(icon);
        dialogStage.setTitle(String.format("Question #%d", question.getPosition()));
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(stage);
        dialogStage.setScene(new Scene(dialogPane));

        dialogStage.showAndWait();

        if (controller.isSaved()) {
            for (Node node : thisHbox.getChildren()) {
                if (node instanceof Label) {
                    Label label = (Label) node;
                    label.setText(String.format("Question#%d: %s", question.getPosition(),question.getContent()));
                }
            }
        }

    } 


}
