package mate.academy.security.util;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.dto.CategoryDto;
import mate.academy.security.dto.CategoryRequestDto;
import mate.academy.security.model.Book;
import mate.academy.security.model.Category;

public class TestUtil {
    private TestUtil() {
    }

    public static CategoryDto categoryDto(Long id, String name, String description) {
        CategoryDto dto = new CategoryDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        return dto;
    }

    public static CategoryRequestDto categoryRequestDto(String name, String description) {
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName(name);
        dto.setDescription(description);
        return dto;
    }

    public static BookRequestDto bookRequestDto(String title, String author, String description,
                                                BigDecimal price, String isbn, List<Long> categoryIds) {
        BookRequestDto dto = new BookRequestDto();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setDescription(description);
        dto.setPrice(price);
        dto.setIsbn(isbn);
        dto.setCategoryIds(categoryIds);
        return dto;
    }
    public static Category categoryEntity(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return category;
    }

    public static Book bookEntity(String title, String author, BigDecimal price, Category category) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        if (category != null) {
            book.getCategories().add(category);
        }
        return book;
    }

    public static Book bookEntity(String title, String author, BigDecimal price, List<Category> categories) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        if (categories != null) {
            book.getCategories().addAll(categories);
        }
        return book;
    }
}