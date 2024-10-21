package HelpSystemApp;

import java.util.Arrays;
import java.io.Serializable;

public class Article implements Serializable {
	
	private int id;
	private char[] title;
	private char[]authors;
	private char[] abstractText;
	private char[] keywords;
	private char[] body;
	private char[] references;
	
	// Constructor
	public Article(int id, String title, String author, String abstractText, String keywords, String body, String references) {
        this.id = id;
        this.title = toCharArray(title);
		this.authors = toCharArray(author);
		this.abstractText = toCharArray(abstractText);
		this.keywords = toCharArray(keywords);
		this.body = toCharArray(body);
		this.references = toCharArray(references);
    }

	// Convert String inputs to char[]
	private char[] toCharArray(String s) {
        return s != null ? s.toCharArray() : new char[0];
    }
	
	// Getter methods
	public int getId() { return this.id; }
	public String getTitle() { return new String(this.title); }
    public String getAuthor() { return new String(this.authors); }
    public String getAbstractText() { return new String(this.abstractText); }
    public String getKeywords() { return new String(this.keywords); }
    public String getBody() { return new String(this.body); }
    public String getReferences() { return new String(this.references); }
	
	public void clearInfo() {
		Arrays.fill(this.title, '\u0000');
		Arrays.fill(this.authors, '\u0000');
		Arrays.fill(this.abstractText, '\u0000');
		Arrays.fill(this.keywords, '\u0000');
		Arrays.fill(this.body, '\u0000');
		Arrays.fill(this.references, '\u0000');
	}
}
