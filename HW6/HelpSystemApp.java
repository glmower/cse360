package HelpSystemApp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class HelpSystemApp {
	
	private final DatabaseHelper dbHelper;
    private final Scanner in;

    public HelpSystemApp() throws Exception {
        dbHelper = new DatabaseHelper();
        in = new Scanner(System.in);
        System.out.println("Welcome to the Help System.");
    }
    
    public void start() {
        while (true) {
        	System.out.println();
            System.out.println("1. Create Article");
            System.out.println("2. List Articles");
            System.out.println("3. Delete Article");
            System.out.println("4. Backup Articles");
            System.out.println("5. Restore Articles");
            System.out.println("6. Exit");
            System.out.print("\nYour choice: ");
            int choice = in.nextInt();
            in.nextLine();

            switch (choice) {
            case 1 -> createArticle();
            case 2 -> listArticles();
            case 3 -> deleteArticle();
            case 4 -> backupArticles();
            case 5 -> restoreArticles();
            case 6 -> exit();
            default -> System.out.println("Invalid choice. Please try again.");
        }
        }
    }
    
    private void backupArticles() {
        System.out.print("Enter backup file name (e.g., articles_backup.dat): ");
        String fileName = in.nextLine();

        String filePath = "/Users/glmower/ASU-CSE360/HW6/backups/" + fileName;

        // Print the absolute path for clarity
        System.out.println("Saving backup to: " + filePath);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            List<Article> articles = dbHelper.loadAllArticles();  // Already decrypted content
            oos.writeObject(articles);  // Write the decrypted articles to the backup file
            System.out.println("Articles backed up successfully to " + fileName);
        } catch (Exception e) {
            System.err.println("Error during backup:");
            e.printStackTrace();
        }
    }
    
    private void restoreArticles() {
        System.out.print("Enter backup file name (e.g., articles_backup.dat): ");
        String fileName = in.nextLine();

        String filePath = "/Users/glmower/ASU-CSE360/HW6/backups/" + fileName;

        // Print the absolute path for clarity
        System.out.println("Restoring from backup: " + filePath);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<Article> articles = (List<Article>) ois.readObject();  // Read decrypted articles
            dbHelper.deleteAllArticles();  // Clear the database

            // Save each article back into the database
            for (Article article : articles) {
                dbHelper.saveArticle(
                        article.getId(),
                        article.getTitle(),
                        article.getAuthor(),
                        article.getAbstractText(),
                        article.getKeywords(),
                        article.getBody(),
                        article.getReferences()
                );
            }
            System.out.println("Articles restored successfully from " + fileName);
        } catch (Exception e) {
            System.err.println("Error during restore:");
            e.printStackTrace();
        }
    }
    
    private void createArticle() {
    	System.out.println();
    	System.out.print("Enter title: ");
        String title = in.nextLine();
        System.out.print("Enter author(s): ");
        String authors = in.nextLine();
        System.out.print("Enter abstract: ");
        String abstractText = in.nextLine();
        System.out.print("Enter keywords: ");
        String keywords = in.nextLine();
        System.out.print("Enter body: ");
        String body = in.nextLine();
        System.out.print("Enter references: ");
        String references = in.nextLine();

        try {
            int id = new java.util.Random().nextInt(1000);
            dbHelper.saveArticle(id, title, authors, abstractText, keywords, body, references);
            System.out.println();
            System.out.println("Article saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }
    
    private void listArticles() {
    	System.out.println();
    	try {
            ResultSet rs = dbHelper.loadArticles();
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No articles found.");
                return;
            }
            
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Title: " + dbHelper.getArticleField(rs, "title"));
                System.out.println("Author: " + dbHelper.getArticleField(rs, "author"));
                System.out.println("-----------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void deleteArticle() {
    	System.out.println();
    	System.out.print("Enter article ID to delete: ");
        int id = in.nextInt();
        try {
            dbHelper.deleteArticle(id);
            System.out.println();
            System.out.println("Article deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void exit() {
        System.out.println("Exiting the program...");
        dbHelper.closeConnection();
        System.out.println("Goodbye!");
        System.exit(0);
    }

	public static void main(String[] args) throws Exception {
		new HelpSystemApp().start();
	}
}
