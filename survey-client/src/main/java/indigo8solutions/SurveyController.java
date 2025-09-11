package indigo8solutions;

import java.io.IOException;
import java.util.ArrayList;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import indigo8solutions.dao.OptionDAO;
import indigo8solutions.dao.QuestionDAO;
import indigo8solutions.model.Option;
import indigo8solutions.model.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SurveyController {

    private ArrayList<Question> questions = new ArrayList<>();
    private int navigator = 0;
    private ArrayList<Integer> answers = new ArrayList<>();
    private ArrayList<Option> optionsSelected = new ArrayList<>();
    private final int  FONT_SIZE = 20;
    private final String  FONT_FAMILY = "Segoe UI";
    private final String  FONT_COLOR = "#04130c";
    


    @FXML
    private HBox questionContainer;
    
    @FXML
    private VBox optionContainer;

    @FXML
    private HBox buttonContainer;

    @FXML
    private void initialize() {
        questions = QuestionDAO.getAllQuestions();

        int questionQty = questions.size();
        for (int i = 0; i < questionQty; i++) {
            answers.add(0);
        }

        loadQuestion();

    }

    private void loadQuestion() {

        Question currentQuestion = questions.get(navigator);
        ArrayList<Option> currentOptions = OptionDAO.getOptionsByQid(currentQuestion.getQid());

        // load question
        Label label = new Label(currentQuestion.getContent());
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD , FONT_SIZE));
        label.setTextFill(Color.web(FONT_COLOR));
        label.setWrapText(true);

        Label number = new Label(String.format("Q%d", currentQuestion.getPosition()));
        number.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD , 40));
        number.setTextFill(Color.web("#59a3e1"));
        number.setMinWidth(Region.USE_PREF_SIZE);
        
        questionContainer.getChildren().addAll(number, label);
        HBox.setHgrow(number, Priority.ALWAYS);
        
        // load options
        ToggleGroup group = new ToggleGroup();
        int optid = answers.get(navigator);

        currentOptions.forEach(option -> {
            RadioButton optRb = new RadioButton(option.getContent());
            optRb.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE));
            optRb.setTextFill(Color.web(FONT_COLOR));
            optRb.setId(String.valueOf(option.getOptid()));
            optRb.setToggleGroup(group);
            if (optid == option.getOptid()) {
                optRb.setSelected(true);
            }
            optionContainer.getChildren().add(optRb);
        });

        // load navigation buttons
        if (navigator != 0) {
            FontAwesomeIconView prevIcon = new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_LEFT);
            prevIcon.setFill(Color.web("#f3fdf8"));
            Button prevBtn = new Button();
            prevBtn.setGraphic(prevIcon);
            prevBtn.setText("Previous");
            prevBtn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE));
            prevBtn.setTextFill(Color.web("#f3fdf8"));
            prevBtn.setStyle("-fx-background-radius: 30px; -fx-background-color: #59a3e1;");
            prevBtn.setMinSize(150, 50);
            prevBtn.setOnAction(e -> handlePrevious(e));
            
            

            buttonContainer.getChildren().add(prevBtn);
        }
        
        Region region = new Region();
        buttonContainer.getChildren().add(region);
        HBox.setHgrow(region, Priority.ALWAYS);

        if (navigator < questions.size()) {
            FontAwesomeIconView nextIcon = new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_RIGHT);
            nextIcon.setFill(Color.web("#f3fdf8"));
            Button nextBtn = new Button();
            nextBtn.setText("Next");
            nextBtn.setGraphic(nextIcon);
            nextBtn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE));
            nextBtn.setTextFill(Color.web("#f3fdf8"));
            nextBtn.setStyle("-fx-background-radius: 30px; -fx-background-color: #59a3e1;");
            nextBtn.setMinSize(150, 50);
            nextBtn.setOnAction(e -> handleNext(e));
            buttonContainer.getChildren().add(nextBtn);
        }

    }

    private void handlePrevious(ActionEvent e) {

        // empty containers
        questionContainer.getChildren().clear();
        optionContainer.getChildren().clear();
        buttonContainer.getChildren().clear();
        
        // move navigator
        navigator--;

        // load previous question
        if (navigator >= 0) {
            loadQuestion();
        }

    }


    private void handleNext(ActionEvent e) {

        // get and validate answer
        ToggleGroup group = ((RadioButton)optionContainer.getChildren().get(0)).getToggleGroup();
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        
        if (selected == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Heads up");
            alert.setHeaderText("No option selected");
            alert.setContentText("Please select one (1) option");
            alert.showAndWait();
            return;
        }
        
        int optid = Integer.parseInt(selected.getId());
        answers.set(navigator, optid);

        // empty containers
        questionContainer.getChildren().clear();
        optionContainer.getChildren().clear();
        buttonContainer.getChildren().clear();
        
        // move navigator
        navigator++;

        // load next question or summary
        if (navigator < questions.size()) {
            loadQuestion();
        } else {
            loadSummary();
        }
        
    }

    private void loadSummary() {

        Label label = new Label("Summary");
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD , FONT_SIZE+4));
        label.setTextFill(Color.web(FONT_COLOR));
        questionContainer.getChildren().add(label);
        optionsSelected.clear();

        for (int i=0; i<questions.size(); i++) {
            Option optionSelected = OptionDAO.getOptionsByOptId(answers.get(i));
            optionsSelected.add(optionSelected);

            Label questionText = new Label(questions.get(i).getContent());
            questionText.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD , FONT_SIZE - 4));
            questionText.setTextFill(Color.web(FONT_COLOR));
            questionText.setWrapText(true);
            Label optionLabel = new Label(optionSelected.getContent());
            optionLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD , FONT_SIZE - 4));
            optionLabel.setTextFill(Color.web("#f3fdf8"));
            optionLabel.setWrapText(true);
            
            VBox vbox = new VBox(5, questionText, optionLabel);
            vbox.setAlignment(Pos.CENTER_LEFT);
            vbox.setPadding(new Insets(20, 20, 20, 20));
            vbox.setStyle("-fx-background-radius: 30px; -fx-background-color: #59a3e1;");
            
            optionContainer.getChildren().add(vbox);

        }

        FontAwesomeIconView backIcon = new FontAwesomeIconView(FontAwesomeIcon.CHEVRON_LEFT);
        backIcon.setFill(Color.web("#f3fdf8"));
        Button backBtn = new Button();
        backBtn.setGraphic(backIcon);
        backBtn.setText("Back to Survey");
        backBtn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE - 4));
        backBtn.setTextFill(Color.web("#f3fdf8"));
        backBtn.setStyle("-fx-background-radius: 30px; -fx-background-color: #59a3e1;");
        backBtn.setMinSize(160, 40);
        backBtn.setOnAction(e -> handlePrevious(e));
        buttonContainer.getChildren().add(backBtn);
        
        Region region1 = new Region();
        buttonContainer.getChildren().add(region1);
        HBox.setHgrow(region1, Priority.ALWAYS);

        FontAwesomeIconView submitIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
        submitIcon.setFill(Color.web("#f3fdf8"));
        Button submitBtn = new Button();
        submitBtn.setText("Submit");
        submitBtn.setGraphic(submitIcon);
        submitBtn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE - 4));
        submitBtn.setTextFill(Color.web("#f3fdf8"));
        submitBtn.setStyle("-fx-background-radius: 30px; -fx-background-color: #04130c;");
        submitBtn.setMinSize(160, 40);
        submitBtn.setOnAction(e -> {
            try {
                handleSubmit(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        buttonContainer.getChildren().add(submitBtn);

        Region region2 = new Region();
        buttonContainer.getChildren().add(region2);
        HBox.setHgrow(region2, Priority.ALWAYS);

        FontAwesomeIconView cancelIcon = new FontAwesomeIconView(FontAwesomeIcon.BAN);
        cancelIcon.setFill(Color.web("#f3fdf8"));
        Button cancelBtn = new Button();
        cancelBtn.setText("Cancel");
        cancelBtn.setGraphic(cancelIcon);
        cancelBtn.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, FONT_SIZE - 4));
        cancelBtn.setTextFill(Color.web("#f3fdf8"));
        cancelBtn.setStyle("-fx-background-radius: 30px; -fx-background-color: #c46c43ff;");
        cancelBtn.setMinSize(160, 40);
        cancelBtn.setOnAction(e -> {
            try {
                openMain(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        buttonContainer.getChildren().add(cancelBtn);

    }

    private void handleSubmit(ActionEvent e) throws IOException{
        
        optionsSelected.forEach(option -> {
            int newTally = option.getTally() + 1;
            OptionDAO.updateOptionTally(option.getOptid(), newTally);
        });

        openMain(e);

    }

    private void openMain(ActionEvent e) throws IOException{
        Parent root = FXMLLoader.load(App.class.getResource("/view/main.fxml"));
        Stage stage = (Stage)(((Node)e.getSource()).getScene().getWindow());
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
}
