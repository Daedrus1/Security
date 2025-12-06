package mate.academy.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.BookDto;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.mapper.BookMapper;
import mate.academy.security.model.Book;
import mate.academy.security.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> findAll() {
        return bookMapper.toDtoList(bookRepository.findAll());
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElse(null));
    }

    @Override
    public BookDto create(BookRequestDto requestDto) {
        return bookMapper.toDto(bookRepository.save(bookMapper.toEntity(requestDto)));
    }

    @Override
    public BookDto update(Long id, BookRequestDto requestDto) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() ->
                new EntityNotFoundException("Book with id " + id + " not found"));
        existBook.setTitle(requestDto.getTitle());
        existBook.setAuthor(requestDto.getAuthor());
        existBook.setPrice(requestDto.getPrice());
        existBook.setDescription(requestDto.getDescription());
        existBook.setIsbn(requestDto.getIsbn());
        return bookMapper.toDto(bookRepository.save(existBook));
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");

        }
        bookRepository.deleteById(id);

    }
}
