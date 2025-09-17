package kpes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kpes.model.Option;

public class OptionDAO {

     public static int createOption(Option option){
        
        String sql = "INSERT INTO OPTION (CONTENT,TALLY,QID) VALUES (?, ?, ?)";
        
        try (Connection c = Database.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);){
            
            stmt.setString(1, option.getContent());
            stmt.setInt(2, option.getTally());
            stmt.setInt(3, option.getQid());


            int rowAffected = stmt.executeUpdate();

            System.out.println("Inserted " + rowAffected + " row/s for option ");
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int optid = rs.getInt(1);
                    return optid;
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


    public static ArrayList<Option> getOptionsByQid(int query) {

        String sql = "SELECT * FROM OPTION WHERE QID=?";

        try (Connection c = Database.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);){
            
            stmt.setInt(1, query);

            ResultSet result = stmt.executeQuery();

            ArrayList<Option> options = new ArrayList<>();
            while (result.next()) {
                int optid = result.getInt("OPTID");
                String content = result.getString("CONTENT");
                int tally = result.getInt("TALLY");
                int qid = result.getInt("QID");
                Option option = new Option(optid,content, tally, qid); 
                options.add(option);
            }
            return options;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static int deleteOptionByOptId(int optid) {
        String sql = "DELETE FROM OPTION WHERE OPTID=?";
        
        try (Connection c = Database.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);){
            
            stmt.setInt(1, optid);


            int rowAffected = stmt.executeUpdate();

            System.out.println("Deleted " + rowAffected + " row/s for option " + optid);

            if(rowAffected == 1){
                return 0;
            } else if(rowAffected == 0){
                return 1;
            } else {
                return -1;
            }
            
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public static int updateOptionContent(int optid, String content) {
        
        String sql = "UPDATE OPTION SET CONTENT = ? WHERE OPTID=?;";

        try (Connection c = Database.getConnection();
            PreparedStatement stmt = c.prepareStatement(sql);){
            
            stmt.setString(1, content);
            stmt.setInt(2, optid);

            int rowAffected = stmt.executeUpdate();

            System.out.println("Updated " + rowAffected + " row/s for option " + optid);

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

    
}
