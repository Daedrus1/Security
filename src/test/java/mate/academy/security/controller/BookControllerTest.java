package mate.academy.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.model.Book;
import mate.academy.security.model.Category;
import mate.academy.security.repository.BookRepository;
import mate.academy.security.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;



@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired BookRepository bookRepository;
    @Autowired CategoryRepository categoryRepository;

    @AfterEach
    void clean() {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_Valid_Returns200AndPersists() throws Exception {
        Category cat = new Category();
        cat.setName("Default");
        cat.setDescription("Test");
        Category savedCat = categoryRepository.save(cat);

        BookRequestDto req = new BookRequestDto();
        req.setTitle("Book Title");
        req.setAuthor("Author");
        req.setPrice(new BigDecimal("10.50"));
        req.setCategoryIds(List.of(savedCat.getId()));

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()) // контроллер сейчас отдаёт 200
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Book Title"));

        assertEquals(1, bookRepository.count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_Invalid_400() throws Exception {
        BookRequestDto bad = new BookRequestDto();
        bad.setTitle("");
        bad.setAuthor("");
        bad.setPrice(new BigDecimal("-1")); // нарушаем @Positive

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBook_Unauthenticated_403() throws Exception {

        BookRequestDto req = new BookRequestDto();
        req.setTitle("X");
        req.setAuthor("Y");
        req.setPrice(new BigDecimal("5.00"));

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void createBook_ForbiddenForUser_403() throws Exception {
        BookRequestDto req = new BookRequestDto();
        req.setTitle("X");
        req.setAuthor("Y");
        req.setPrice(new BigDecimal("5.00"));

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllBooks_returns200AndList() throws Exception {
        saveBook("DDD", "Evans", new BigDecimal("30.00"));
        saveBook("Refactoring", "Fowler", new BigDecimal("27.50"));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", not(blankOrNullString())));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBookById_returns200AndBody() throws Exception {
        Long id = saveBook("Clean Code", "Martin", new BigDecimal("25.99"));

        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Martin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_returns200AndId() throws Exception {
        Long catId = createCategory("Default");

        BookRequestDto req = new BookRequestDto();
        req.setTitle("Book Title");
        req.setAuthor("Author");
        req.setPrice(new BigDecimal("10.50"));
        req.setCategoryIds(List.of(catId));

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Book Title"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBook_returns200AndUpdatedBody() throws Exception {
        Long id = saveBook("Old", "A", new BigDecimal("10.00"));
        Long catId = createCategory("Tech");

        BookRequestDto update = new BookRequestDto();
        update.setTitle("New");
        update.setAuthor("A");
        update.setPrice(new BigDecimal("15.00"));
        update.setCategoryIds(List.of(catId));

        mockMvc.perform(put("/api/books/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New"))
                .andExpect(jsonPath("$.price").value(15.00));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBook_returns200() throws Exception {
        Long id = saveBook("Tmp", "B", new BigDecimal("9.00"));

        mockMvc.perform(delete("/api/books/{id}", id).with(csrf()))
                .andExpect(status().isOk());
    }

    private Long createCategory(String base) {
        String name = base + "-" + System.nanoTime();
        Category c = new Category();
        c.setName(name);
        c.setDescription(name + " desc");
        return categoryRepository.save(c).getId();
    }

    private Long saveBook(String title, String author, BigDecimal price) {
        Book b = new Book();
        b.setTitle(title);
        b.setAuthor(author);
        b.setPrice(price);
        return bookRepository.save(b).getId();
    }
}
