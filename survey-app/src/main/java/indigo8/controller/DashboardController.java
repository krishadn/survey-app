package indigo8.controller;

import java.io.IOException;
import java.util.ArrayList;

import indigo8.App;
import indigo8.dao.OptionDAO;
import indigo8.dao.QuestionDAO;
import indigo8.model.Option;
import indigo8.model.Question;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DashboardController {

    private ArrayList<Question> questions;

    @FXML
    private Button manageBtn;

    @FXML
    private VBox contentContainer;

    @FXML
    private void initialize() {
        manageBtn.setOnAction(event -> {
            
            try {
                Parent root = FXMLLoader.load(App.class.getResource("/view/manage-survey.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        loadUI();

    }



    private void loadUI(){
        
        questions = QuestionDAO.getAllQuestions();
        if (!(questions==null)) {
            questions.forEach(question -> addQuestionToUI(question));
        }
        
    }



    private void addQuestionToUI(Question question){

        ArrayList<Option> options = OptionDAO.getOptionsByQid(question.getQid());
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        ObservableList<Option> obsOption = FXCollections.observableArrayList();
        int[] totalTally = {0};
      

        if (options != null) {

            options.forEach(option -> {
                pieChartData.add(new PieChart.Data(option.getContent(), option.getTally()));
                totalTally[0] += option.getTally();
            });

            obsOption = FXCollections.observableArrayList(options);

        }

        TableColumn<Option, String> contentCol = new TableColumn<>("Option");
        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));

        TableColumn<Option, Integer> tallyCol = new TableColumn<>("Tally");
        tallyCol.setCellValueFactory(new PropertyValueFactory<>("tally"));

        TableColumn<Option, String> percentCol = new TableColumn<>("Percentage");        
        percentCol.setCellValueFactory(cellData -> {
            Option option = cellData.getValue();
            int tally = option.getTally();
            double percent = 0;

            if (totalTally[0] == 0) {
                percent = 0;
            } else {
                try {
                    percent = ((double)tally / totalTally[0]) * 100;
                } catch (ArithmeticException e) {
                    // e.printStackTrace();
                }
            }
            
            return new ReadOnlyStringWrapper(String.format("%.2f%%", percent));
        });
        


        TableView<Option> tableView = new TableView<Option>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().clear();
        tableView.getColumns().addAll(contentCol, tallyCol, percentCol);
        tableView.setItems(obsOption);

        Label totalLabel = new Label("Total Responses: ");
        Label qtyLabel = new Label(String.valueOf(totalTally[0]));
        HBox labelCont = new HBox(10, totalLabel, qtyLabel);
        labelCont.setMinHeight(40);
        labelCont.setAlignment(Pos.TOP_LEFT);

        VBox vBox = new VBox(tableView, labelCont);

        PieChart pieChart = new PieChart(pieChartData);

        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setLabelLineLength(25);

        SplitPane splitPane = new SplitPane(vBox, pieChart);
        splitPane.setOrientation(Orientation.HORIZONTAL);

        TitledPane titledPane = new TitledPane(String.format("Question #%d: %s", question.getPosition(), question.getContent()), splitPane);
        titledPane.setExpanded(false);
        titledPane.setWrapText(true);
        titledPane.setFont(Font.font("Segoe UI",16));
        contentContainer.getChildren().add(titledPane);

    }
    
    
}
