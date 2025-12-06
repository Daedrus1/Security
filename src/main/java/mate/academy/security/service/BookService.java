package mate.academy.security.service;

import mate.academy.security.dto.BookDto;
import mate.academy.security.dto.BookRequestDto;

import java.util.List;

public interface BookService {
    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto create(BookRequestDto requestDto);

    BookDto update(Long id, BookRequestDto requestDto);

    void delete(Long id);
}
