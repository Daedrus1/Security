package mate.academy.security.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;
import mate.academy.security.model.Book;
import mate.academy.security.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindById_shouldWork() {
        Category category = new Category();
        category.setName("cat-for-book");
        Category savedCategory = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(BigDecimal.valueOf(100));
        book.getCategories().add(savedCategory);

        Book savedBook = bookRepository.save(book);
        Optional<Book> found = bookRepository.findById(savedBook.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Book Title");
        assertThat(found.get().getAuthor()).isEqualTo("Author");
        assertThat(found.get().getPrice()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(found.get().getCategories()).hasSize(1);
        assertThat(found.get().getCategories().iterator().next().getName()).isEqualTo("cat-for-book");

    }
    }
