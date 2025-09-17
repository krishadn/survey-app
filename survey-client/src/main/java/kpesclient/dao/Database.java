package kpesclient.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static Connection getConnection() throws ClassNotFoundException, IOException {

        Class.forName("org.sqlite.JDBC");

        try {
            Connection c = DriverManager.getConnection(createUrl());
            try (Statement stmt = c.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            return c;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createTables() {

        try (Connection c = getConnection();
            Statement stmt = c.createStatement();) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS QUESTION (
                        QID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        CONTENT TEXT NOT NULL,
                        POSITION INT NOT NULL UNIQUE
                        )
                    """);
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS OPTION (
                        OPTID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        CONTENT TEXT NOT NULL, 
                        TALLY INTEGER NOT NULL,
                        QID INTEGER NOT NULL,
                        FOREIGN KEY (QID) REFERENCES QUESTION (QID) ON DELETE CASCADE
                        )
                    """);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createUrl() throws IOException  {
        String baseDir = System.getProperty("user.home");
        Path dbDir = Paths.get(baseDir, ".survey");
        Files.createDirectories(dbDir); 

        Path dbPath = Paths.get(System.getProperty("user.home"), ".survey", "survey.db");

        String url = "jdbc:sqlite:" + dbPath.toAbsolutePath().toString().replace("\\", "/");

        return url;
    }
    
}
