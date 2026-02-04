package mate.academy.security.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.BookDto;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.mapper.BookMapper;
import mate.academy.security.model.Book;
import mate.academy.security.model.Category;
import mate.academy.security.repository.BookRepository;
import mate.academy.security.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> findAll() {
        return bookMapper.toDtoList(bookRepository.findAll());
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto create(BookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        book.setCategories(new HashSet<>(getCategoriesOrThrow(requestDto.getCategoryIds())));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto update(Long id, BookRequestDto requestDto) {
        Book existBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));

        existBook.setTitle(requestDto.getTitle());
        existBook.setAuthor(requestDto.getAuthor());
        existBook.setPrice(requestDto.getPrice());
        existBook.setDescription(requestDto.getDescription());
        existBook.setIsbn(requestDto.getIsbn());

        existBook.getCategories().clear();
        existBook.getCategories().addAll(getCategoriesOrThrow(requestDto.getCategoryIds()));

        return bookMapper.toDto(bookRepository.save(existBook));
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    private List<Category> getCategoriesOrThrow(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return List.of();
        }
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new EntityNotFoundException("One or more categories not found");
        }
        return categories;
    }
}
