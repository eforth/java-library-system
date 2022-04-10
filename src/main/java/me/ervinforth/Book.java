package me.ervinforth;

import java.io.Serializable;

public class Book implements Serializable {
    private String isbn;
    private String title; 
    private String edition; 
    private String categories;
    private String author;
    private String publisher;

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getEdition() {
        return edition;
    }
    public void setEdition(String edition) {
        this.edition = edition;
    }
    public String getCategories() {
        return categories;
    }
    public void setCategories(String categories) {
        this.categories = categories;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return String.format("Book{isbn=%s,title=%s,edition=%s,categories=%s,author=%s,publisher=%s}", 
            isbn,title,edition,categories,author,publisher);
    }
}
