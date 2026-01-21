package mate.academy.security.repository;

import java.math.BigDecimal;
import mate.academy.security.model.Book;
import mate.academy.security.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindById_shouldWork() {
        Category category = new Category();
        category.setName("category-for-book");
        category.setDescription("desc");
        Category savedCategory = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setPrice(BigDecimal.valueOf(100));
        book.getCategories().add(savedCategory);

        Book expected = bookRepository.save(book);

        Book actual = bookRepository.findById(expected.getId()).orElseThrow();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "categories.id")
                .isEqualTo(expected);
    }
}
