package kpesclient.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kpesclient.model.Question;

public class QuestionDAO {

    public static int createQuestion(Question question){
        
        String sql = "INSERT INTO QUESTION (CONTENT,POSITION) VALUES (?, ?)";
        
        try (Connection c = Database.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);){
            
            stmt.setString(1, question.getContent());
            stmt.setInt(2, question.getPosition());

            int rowAffected = stmt.executeUpdate();

            System.out.println("Inserted " + rowAffected + " row/s for question ");

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return id;
                }
            }
            
            return 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


    public static ArrayList<Question> getAllQuestions() {

        String sql = "SELECT * FROM QUESTION";

        try (Connection c = Database.getConnection();
            Statement stmt = c.createStatement();) {

            ResultSet result = stmt.executeQuery(sql);
            ArrayList<Question> questions = new ArrayList<>();
            while (result.next()) {
                int qid = result.getInt("QID");
                String content = result.getString("CONTENT");
                int position = result.getInt("POSITION");
                Question question = new Question(qid, content, position); 
                questions.add(question);
            }
            return questions;
            
        } catch (Exception e) {
            System.out.println("Get All Question Failed: " + e.getMessage());
            return null;
        }
    }

    public static Question getQuestionByQid(int query) {

        String sql = "SELECT * FROM QUESTION WHERE QID=?";

        try (Connection c = Database.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);){
            
            stmt.setInt(1, query);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int qid = result.getInt("QID");
                String content = result.getString("CONTENT");
                int position = result.getInt("POSITION");
                Question question = new Question(qid, content, position);

                return question;
            }

            return null;

            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }


    public static int updateQuestionContent(int qid, String content) {
        
        String sql = "UPDATE QUESTION SET CONTENT = ? WHERE QID=?;";

        try (Connection c = Database.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);){
            
            stmt.setString(1, content);
            stmt.setInt(2, qid);

            int rowAffected = stmt.executeUpdate();

            System.out.println("Updated " + rowAffected + " row/s for question " + qid);

            if(rowAffected == 1){
                return 0;
            } else if(rowAffected == 0){
                return 1;
            } else {
                return -1;
            }
                    
        } catch (Exception e) {;
            return -1;
        }



    }


    public static int deleteQuestionByQid(int qid, int position) {
        String deleteStmt = "DELETE FROM QUESTION WHERE QID=?";
        String updateStmt = "UPDATE QUESTION SET POSITION = POSITION - 1 WHERE POSITION > ?";
        int deletedRow = 0;
        int updatedRow = 0;
        
        try (Connection c = Database.getConnection()){
            c.setAutoCommit(false);

            try (PreparedStatement stmt1 = c.prepareStatement(deleteStmt)) {
                stmt1.setInt(1, qid);
                deletedRow = stmt1.executeUpdate();
                System.out.println("Deleted " + deletedRow + " row/s for question " + qid);
            }

            try (PreparedStatement stmt2 = c.prepareStatement(updateStmt)) {
                stmt2.setInt(1, position);
                updatedRow = stmt2.executeUpdate();
                System.out.println("Updated " + updatedRow + " row/s after question " + qid);
            }
            
            c.commit();

            if(deletedRow == 1){  
                return 0;
            } else if(deletedRow == 0){
                return 1;
            } else {
                return -1;
            }
            
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    
}
