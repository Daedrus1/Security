package mate.academy.security.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.junit.jupiter.api.Assertions.assertThrows;


import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.security.dto.BookDto;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.mapper.BookMapper;
import mate.academy.security.model.Book;
import mate.academy.security.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
        Verify that book is created successfully
        and BookDto is returned
        """)
    void create_shouldReturnBookDto() {
        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setAuthor("author");
        requestDto.setTitle("title");
        requestDto.setPrice(BigDecimal.valueOf(10.50));
        requestDto.setCategoryIds(List.of(1L));

        Book entity = new Book();
        entity.setAuthor("author");
        entity.setTitle("title");
        entity.setPrice(BigDecimal.valueOf(10.50));

        Book saved = new Book();
        saved.setAuthor("author");
        saved.setTitle("title");
        saved.setPrice(BigDecimal.valueOf(10.50));
        saved.setId(1L);

        BookDto expected = new BookDto();
        expected.setId(1L);

        when(bookMapper.toEntity(requestDto)).thenReturn(entity);
        when(bookRepository.save(entity)).thenReturn(saved);
        when(bookMapper.toDto(saved)).thenReturn(expected);

        BookDto actual = bookService.create(requestDto);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(1L);

        verify(bookMapper).toEntity(requestDto);
        verify(bookMapper).toDto(saved);
        verify(bookRepository).save(entity);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
    @Test
    @DisplayName("""
        Verify that update throws EntityNotFoundException
        when book with given id does not exist
        """)
    void update_whenBookNotFound_shouldThrowException() {
        Long bookId = 100L;

        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setAuthor("author");
        requestDto.setTitle("title");
        requestDto.setPrice(BigDecimal.valueOf(10.50));
        requestDto.setCategoryIds(List.of(1L));

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.update(bookId ,requestDto));

        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("""
        Verify that delete throws EntityNotFoundException
        when book with given id does not exist
        """)
    void delete_whenBookNotFound_shouldThrowException() {
        Long bookId = 123L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(
                EntityNotFoundException.class,
                () -> bookService.delete(bookId)
        );

        verify(bookRepository).existsById(bookId);
        verify(bookRepository, never()).deleteById(anyLong());
    }


}
