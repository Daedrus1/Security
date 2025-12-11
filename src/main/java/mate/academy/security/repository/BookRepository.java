package mate.academy.security.repository;

import java.util.List;
import mate.academy.security.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByCategories_Id(Long categoryId, Pageable pageable);
}
