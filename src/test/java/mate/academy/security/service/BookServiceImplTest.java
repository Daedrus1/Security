package mate.academy.security.service;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.security.dto.BookDto;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.mapper.BookMapper;
import mate.academy.security.model.Book;
import mate.academy.security.model.Category;
import mate.academy.security.repository.BookRepository;
import mate.academy.security.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("create: returns BookDto on success")
    void create_success() {
        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setAuthor("author");
        requestDto.setTitle("title");
        requestDto.setPrice(BigDecimal.valueOf(10.50));
        requestDto.setCategoryIds(List.of(1L));

        Category category = new Category();
        category.setId(1L);

        Book mappedEntity = new Book();
        Book savedEntity = new Book();
        savedEntity.setId(1L);

        BookDto expected = new BookDto();
        expected.setId(1L);

        when(categoryRepository.findAllById(requestDto.getCategoryIds())).thenReturn(List.of(category));
        when(bookMapper.toEntity(requestDto)).thenReturn(mappedEntity);
        when(bookRepository.save(any(Book.class))).thenReturn(savedEntity);
        when(bookMapper.toDto(savedEntity)).thenReturn(expected);

        BookDto actual = bookService.create(requestDto);

        assertThat(actual).isEqualTo(expected);

        verify(categoryRepository).findAllById(requestDto.getCategoryIds());
        verify(bookMapper).toEntity(requestDto);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertThat(captor.getValue().getCategories()).containsExactly(category);

        verify(bookMapper).toDto(savedEntity);
        verifyNoMoreInteractions(bookRepository, categoryRepository, bookMapper);
    }

    @Test
    @DisplayName("create: throws EntityNotFoundException when categoryIds contain missing category")
    void create_whenCategoryMissing_shouldThrow() {
        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setAuthor("author");
        requestDto.setTitle("title");
        requestDto.setPrice(BigDecimal.valueOf(10.50));
        requestDto.setCategoryIds(List.of(1L, 2L));

        when(bookMapper.toEntity(requestDto)).thenReturn(new Book());
        when(categoryRepository.findAllById(requestDto.getCategoryIds()))
                .thenReturn(List.of(new Category() {{
                    setId(1L);
                }}));

        assertThrows(EntityNotFoundException.class, () -> bookService.create(requestDto));

        verify(bookMapper).toEntity(requestDto);
        verify(categoryRepository).findAllById(requestDto.getCategoryIds());

        verifyNoInteractions(bookRepository);
        verify(bookMapper, never()).toDto(any(Book.class));
        verifyNoMoreInteractions(bookMapper, categoryRepository);
    }


    @Test
    @DisplayName("update: updates existing book and returns dto")
    void update_success() {
        Long bookId = 1L;

        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setAuthor("new author");
        requestDto.setTitle("new title");
        requestDto.setPrice(BigDecimal.valueOf(20));
        requestDto.setDescription("new desc");
        requestDto.setIsbn("new isbn");
        requestDto.setCategoryIds(List.of(2L));

        Book existing = new Book();
        existing.setId(bookId);

        Category category = new Category();
        category.setId(2L);

        Book saved = new Book();
        saved.setId(bookId);

        BookDto expected = new BookDto();
        expected.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existing));
        when(categoryRepository.findAllById(requestDto.getCategoryIds())).thenReturn(List.of(category));
        when(bookRepository.save(any(Book.class))).thenReturn(saved);
        when(bookMapper.toDto(saved)).thenReturn(expected);

        BookDto actual = bookService.update(bookId, requestDto);

        assertThat(actual).isEqualTo(expected);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).findById(bookId);
        verify(categoryRepository).findAllById(requestDto.getCategoryIds());
        verify(bookRepository).save(captor.capture());
        verify(bookMapper).toDto(saved);

        Book toSave = captor.getValue();
        assertEquals(bookId, toSave.getId());
        assertEquals("new title", toSave.getTitle());
        assertEquals("new author", toSave.getAuthor());
        assertEquals(BigDecimal.valueOf(20), toSave.getPrice());
        assertEquals("new desc", toSave.getDescription());
        assertEquals("new isbn", toSave.getIsbn());
        assertThat(toSave.getCategories()).containsExactly(category);

        verifyNoMoreInteractions(bookRepository, categoryRepository, bookMapper);
    }

    @Test
    @DisplayName("update: throws EntityNotFoundException when book not found")
    void update_whenBookNotFound_shouldThrowException() {
        Long bookId = 100L;
        BookRequestDto requestDto = new BookRequestDto();

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.update(bookId, requestDto));

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(categoryRepository, bookMapper);
    }

    @Test
    @DisplayName("findById: returns dto when exists")
    void findById_success() {
        Long bookId = 7L;

        Book entity = new Book();
        entity.setId(bookId);

        BookDto expected = new BookDto();
        expected.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(entity));
        when(bookMapper.toDto(entity)).thenReturn(expected);

        BookDto actual = bookService.findById(bookId);

        assertThat(actual).isEqualTo(expected);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(entity);
        verifyNoMoreInteractions(bookRepository, bookMapper);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    @DisplayName("findById: throws EntityNotFoundException when book not found")
    void findById_notFound() {
        Long id = 7L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findById(id));

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(categoryRepository, bookMapper);
    }

    @Test
    @DisplayName("findAll: returns mapped list")
    void findAll_success() {
        Book book1 = new Book();
        Book book2 = new Book();

        BookDto dto1 = new BookDto();
        dto1.setId(1L);
        BookDto dto2 = new BookDto();
        dto2.setId(2L);

        List<Book> books = List.of(book1, book2);
        List<BookDto> expected = List.of(dto1, dto2);

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDtoList(books)).thenReturn(expected);

        List<BookDto> actual = bookService.findAll();

        assertThat(actual).isEqualTo(expected);

        verify(bookRepository).findAll();
        verify(bookMapper).toDtoList(books);
        verifyNoMoreInteractions(bookRepository, bookMapper);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    @DisplayName("delete: calls deleteById on success")
    void delete_success() {
        Long bookId = 10L;

        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.delete(bookId);

        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(categoryRepository, bookMapper);
    }

    @Test
    @DisplayName("delete: throws EntityNotFoundException when book not found")
    void delete_whenBookNotFound_shouldThrowException() {
        Long bookId = 123L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> bookService.delete(bookId));

        verify(bookRepository).existsById(bookId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(categoryRepository, bookMapper);
    }
}
