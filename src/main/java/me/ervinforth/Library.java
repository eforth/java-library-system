package me.ervinforth;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Library {

    private final String FILENAME = "library.bat";
    private List<Book> books = new ArrayList<>();


    public void start() {

        load();

        System.out.println("Welcome to Vector Library!");

        try (Scanner input = new Scanner(System.in)) {
            while(true) {
                menu();
                System.out.print("Enter a menu option: ");
                int menuOption = input.nextInt();

                // skip remaining content from the input buffer
                input.nextLine();

                switch(menuOption) {
                    case 1:
                        addBook(input);
                        break;
                    case 2:
                        updateBook(input);
                        break;
                    case 3:
                        removeBook(input);
                        break;
                    case 4:
                        viewBooks();
                        break;
                    case 5:
                        viewBook(input);
                        break;
                    case 6:
                        removeBooks(input);
                        break;
                    case 7:
                        return;
                    default:
                        return;
                }
                
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void menu() {
        System.out.println("\n\n---- Menu ----");
        System.out.println("1. Add a Book");
        System.out.println("2. Update a Book");
        System.out.println("3. Delete a Book");
        System.out.println("4. View All Books");
        System.out.println("5. View Book");
        System.out.println("6. Delete All Books");
        System.out.println("7. Exit\n");
    }

    public void addBook(Scanner input) {
        List<Book> temp = new ArrayList<>();

        String answer = "No";

        do {

            Book book = new Book();

            System.out.print("\nEnter isbn: ");
            book.setIsbn(input.nextLine());
            System.out.print("Enter title: ");
            book.setTitle(input.nextLine());
            System.out.print("Enter edition: ");
            book.setEdition(input.nextLine());
            System.out.print("Enter categories (comma separated values): ");
            book.setCategories(input.nextLine());
            System.out.print("Enter author: ");
            book.setAuthor(input.nextLine());
            System.out.print("Enter publisher: ");
            book.setPublisher(input.nextLine());

            try {
                findBook(book.getIsbn());
                answer = "Yes";
                continue;
            } catch (InvalidISBNException ie) {
                answer = "Yes";
                continue;
            } catch (BookNotFoundException e) {

            }

            temp.add(book);

            System.out.print("\nDo you want to add more books? (Yes/No): ");
            answer = input.nextLine();

        } while(answer.equalsIgnoreCase("yes"));

        books.addAll(temp);

        //System.out.println(books);

        save();

        System.out.println("Books were successfully saved!");
    }
    
    public void viewBooks() {
        System.out.println("\n--- Books ---\n");
        System.out.printf("%-15s%-30s%-15s\n", "ISBN", "Title", "Author");

        for (Book book : books) {
            System.out.printf("%-15s%-30s%-15s\n", book.getIsbn(), 
                book.getTitle(), book.getAuthor());
        }
    }
    
    public void save() {
        try (
            // Open file stream to modify file
            FileOutputStream fOutputStream = new FileOutputStream(FILENAME);
            // Open stream to writing purposes
            ObjectOutputStream outputStream = new ObjectOutputStream(fOutputStream);
        ) {
            // Cycle through each book in the list
            for (Book book : books) {
                // Write book to stream
                outputStream.writeObject(book);
            }

        } catch (Exception e) {
            System.out.println("Unable to save data.");
        }
    }
    
    public void load() {

        File file = new File(FILENAME);

        // Only create if doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                System.out.println("Unable to create library file");
                // Exit the system gracefully
                System.exit(0);
            }
        }

        // Open file as a stream of bytes
        try (FileInputStream fInputStream = new FileInputStream(FILENAME)) {
            // Convert stream of bytes to stream of objects
            try (ObjectInputStream oInputStream = new ObjectInputStream(fInputStream)) {
                boolean hasMore = true;
                // Accept objects while avaiable
                while (hasMore) {
                    // Reading an object from the stream
                    Object obj = oInputStream.readObject();
                    // Only add book to the list if not null
                    if (obj != null) {
                        // down cast object to a Book
                        Book book = (Book) obj;
                        // add book to the list
                        books.add(book);
                        // set "hasMore" to true
                        hasMore = true;
                    } else {
                        hasMore = false;
                    }
                }
            } catch (EOFException ioe) {

            } catch (Exception ioe) {
                ioe.printStackTrace();
                System.out.println("Unable to read data from the file.");
            }
        } catch (Exception e) {
            System.out.println("Unable to access file.");
        }
    }
    
    public void updateBook(Scanner in) {

        Book book = null;

        System.out.print("\nEnter isbn: ");
        String isbn = in.nextLine();

        try {
            book = findBook(isbn);
        } catch (InvalidISBNException e) {
            System.out.println("Book not updated due to invalid isbn.");
        } catch (BookNotFoundException b) {
            System.out.println(b.getMessage());
        }

        int index = books.indexOf(book);

        System.out.printf("Enter title (%s): ", book.getTitle());
        book.setTitle(in.nextLine());
        System.out.printf("Enter edition (%s): ", book.getEdition());
        book.setEdition(in.nextLine());
        System.out.printf("Enter categories (comma separated values) (%s): ", book.getCategories());
        book.setCategories(in.nextLine());
        System.out.printf("Enter author (%s): ", book.getAuthor());
        book.setAuthor(in.nextLine());
        System.out.printf("Enter publisher (%s): ", book.getPublisher());
        book.setPublisher(in.nextLine());
        
        books.set(index, book);

        save();
        System.out.println("Book was successfully updated!");
    }
    
    public void removeBook(Scanner in) {

        Book book = null;

        System.out.print("\nEnter isbn: ");
        String isbn = in.nextLine();

        try {
            book = findBook(isbn);
        } catch (InvalidISBNException e) {
            System.out.println("Book not deleted due to invalid isbn.");
        } catch (BookNotFoundException b) {
            System.out.println(b.getMessage());
        }

        System.out.print("\nAre you sure you want to delete this book? (Yes/No): ");
        String answer = in.nextLine();

        if (answer.equalsIgnoreCase("yes")) {
            books.remove(book);
            save();
            System.out.printf("Book with isbn: %s was deleted!", isbn);
        }
    }

    public void removeBooks(Scanner in) {

        System.out.print("\nAre you want to delete all books? (Yes/No): ");
        String answer = in.nextLine();

        if (answer.equalsIgnoreCase("yes")) {
            books.clear();
            save();
            System.out.println("Deleted all books!");
        }
    }
    
    private Book findBook(String isbn) throws InvalidISBNException, BookNotFoundException {

        if (isbn == null || isbn.isBlank()) throw new InvalidISBNException();

        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return book;
            }
        }

        throw new BookNotFoundException();
    }
    
    public void viewBook(Scanner in) {

        System.out.print("\nEnter the isbn: ");
        String isbn = in.nextLine();

        try {

            Book book = findBook(isbn);

            System.out.println("\n--- Book ---\n");
            System.out.printf("%-15s:%s\n", "ISBN", book.getIsbn());
            System.out.printf("%-15s:%s\n", "Title", book.getTitle());
            System.out.printf("%-15s:%s\n", "Edition", book.getEdition());
            System.out.printf("%-15s:%s\n", "Categories", book.getCategories());
            System.out.printf("%-15s:%s\n", "Author", book.getAuthor());
            System.out.printf("%-15s:%s\n", "Publisher", book.getPublisher());

        } catch (BookNotFoundException nf) {
            System.out.println(nf.getMessage());
        } catch (InvalidISBNException e) {
            System.out.println(e.getMessage());
        }
    }
}
