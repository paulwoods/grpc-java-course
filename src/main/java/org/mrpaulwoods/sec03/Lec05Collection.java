package org.mrpaulwoods.sec03;

import org.mrpaulwoods.models.sec03.Book;
import org.mrpaulwoods.models.sec03.Library;
import org.slf4j.Logger;

import java.util.List;

public class Lec05Collection {

    public static final Logger log = org.slf4j.LoggerFactory.getLogger(Lec05Collection.class);

    public static void main(String[] args) {

        // create books

        var book1 = Book.newBuilder()
                .setTitle("Harry Potter - Part 1")
                .setAuthor("JK Rowling")
                .setPublicationYear(1997)
                .build();

        var book2 = Book.newBuilder()
                .setTitle("Harry Potter - Part 2")
                .setAuthor("JK Rowling")
                .setPublicationYear(1998)
                .build();


        var book3 = Book.newBuilder()
                .setTitle("Harry Potter - Part 3")
                .setAuthor("JK Rowling")
                .setPublicationYear(1999)
                .build();

        var library = Library.newBuilder()
                .setName("Fantasy Library")
//                .addBooks(book1)
//                .addBooks(book2)
//                .addBooks(book3)
                .addAllBooks(List.of(book1, book2, book3))
                .build();

        log.info("library {}", library);

    }
}
