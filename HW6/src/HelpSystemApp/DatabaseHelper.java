package HelpSystemApp;

import java.sql.*;
import java.util.*;
import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:h2:./helpSystemDB";
    private final EncryptionHelper encryptionHelper;
    private final byte[] iv;
    private Connection connection;

    public DatabaseHelper() throws Exception {
        connection = DriverManager.getConnection(DB_URL);
        encryptionHelper = new EncryptionHelper();
        iv = EncryptionUtils.generateInitializationVector();
        createTable();
    }

    private void createTable() throws SQLException {
    	try (Statement stmt = connection.createStatement()) {
            // Drop the table if it exists to avoid schema conflicts
            stmt.execute("DROP TABLE IF EXISTS Articles");

            // Create the table with the correct schema
            stmt.execute("CREATE TABLE Articles (" +
                    "id INT PRIMARY KEY, " +
                    "title BLOB, " +
                    "author BLOB, " +
                    "abstract BLOB, " +
                    "keywords BLOB, " +
                    "body BLOB, " +
                    "refs BLOB)");
        }
    	
    	// printTableSchema();
    }
    
    // Prints the columns to verify that the table is correct (for debugging)
    public void printTableSchema() throws SQLException {
        String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'ARTICLES'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Column: " + rs.getString("COLUMN_NAME"));
            }
        }
    }

    public void saveArticle(int id, String title, String author, String abstractText, String keywords, String body, String references) throws Exception {
        String sql = "INSERT INTO Articles (id, title, author, abstract, keywords, body, refs) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setBytes(2, encryptField(title));
            stmt.setBytes(3, encryptField(author));
            stmt.setBytes(4, encryptField(abstractText));
            stmt.setBytes(5, encryptField(keywords));
            stmt.setBytes(6, encryptField(body));
            stmt.setBytes(7, encryptField(references));
            stmt.executeUpdate();
        }
    }

    public ResultSet loadArticles() throws SQLException {
        String sql = "SELECT * FROM Articles";
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public String getArticleField(ResultSet rs, String fieldName) throws Exception {
        byte[] encryptedData = rs.getBytes(fieldName);
        return decryptField(encryptedData);
    }

    public void deleteArticle(int id) throws SQLException {
        String sql = "DELETE FROM Articles WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public List<Article> loadAllArticles() throws Exception {
        List<Article> articles = new ArrayList<Article>();
        String sql = "SELECT * FROM Articles";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                articles.add(new Article(
                        rs.getInt("id"),
                        decryptField(rs.getBytes("title")),
                        decryptField(rs.getBytes("author")),
                        decryptField(rs.getBytes("abstract")),
                        decryptField(rs.getBytes("keywords")),
                        decryptField(rs.getBytes("body")),
                        decryptField(rs.getBytes("refs"))
                ));
            }
        }
        return articles;
    }

    public void deleteAllArticles() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Articles");
        }
    }

    private byte[] encryptField(String data) throws Exception {
        return encryptionHelper.encrypt(data, iv);
    }

    private String decryptField(byte[] encryptedData) throws Exception {
        return encryptionHelper.decrypt(encryptedData, iv);
    }

    public void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
