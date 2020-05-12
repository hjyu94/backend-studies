package me.hjeong._1_ioc_container_and_bean;

import org.springframework.beans.factory.annotation.Autowired;

public class BookService {
    @Autowired
    BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
