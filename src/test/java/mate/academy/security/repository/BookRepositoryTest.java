package mate.academy.security.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.security.model.Book;
import mate.academy.security.model.Category;
import mate.academy.security.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("""
            Verify that save creates book entity successfully
            and returns book with generated id
            """)
    void save_success_returnsSavedEntityWithId() {
        Category category = categoryRepository.save(
                TestUtil.categoryEntity("category-1", "desc")
        );

        Book book = TestUtil.bookEntity("Book-1", "Author-1",
                new BigDecimal("100.00"), category);

        Book saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Book-1");
        assertThat(saved.getCategories()).hasSize(1);
    }

    @Test
    @DisplayName("""
            Verify that findById returns book entity
            when book with given id exists
            """)
    void findById_existingId_returnsBook() {
        Category category = categoryRepository.save(
                TestUtil.categoryEntity("category-1", "desc")
        );

        Book saved = bookRepository.save(
                TestUtil.bookEntity("Book-1", "Author-1",
                        new BigDecimal("100.00"), category)
        );

        Book found = bookRepository.findById(saved.getId()).orElseThrow();

        assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("id", "categories.id")
                .isEqualTo(saved);
    }

    @Test
    @DisplayName("""
            Verify that findById returns empty Optional
            when book with given id does not exist
            """)
    void findById_nonExistingId_returnsEmptyOptional() {
        assertThat(bookRepository.findById(9999L)).isEmpty();
    }

    @Test
    @DisplayName("""
            Verify that deleteById removes book entity
            when book with given id exists
            """)
    void deleteById_existingId_deletesEntity() {
        Category category = categoryRepository.save(
                TestUtil.categoryEntity("category-1", "desc")
        );

        Book saved = bookRepository.save(
                TestUtil.bookEntity("Book-1", "Author-1",
                        new BigDecimal("100.00"), category)
        );

        bookRepository.deleteById(saved.getId());

        assertThat(bookRepository.findById(saved.getId())).isEmpty();
    }
}
