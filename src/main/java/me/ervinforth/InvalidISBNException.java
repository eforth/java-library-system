package me.ervinforth;

public class InvalidISBNException extends RuntimeException {
    public InvalidISBNException() {
        super("Invalid book isbn code.");
    }

    public String getMessage() {
        return super.getMessage();
    }
}
