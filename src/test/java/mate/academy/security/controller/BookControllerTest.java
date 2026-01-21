package mate.academy.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.security.dto.BookRequestDto;
import mate.academy.security.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/category/add-one-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBook_Valid_Returns201AndReturnsExpectedDto() throws Exception {
        BookRequestDto bookRequestDto = TestUtil.bookRequestDto(
                "Book Title",
                "Author",
                null,
                new BigDecimal("10.50"),
                null,
                List.of(1L)
        );

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.author").value("Author"))
                .andExpect(jsonPath("$.price").value(10.50));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/clear-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBook_Invalid_400() throws Exception {
        BookRequestDto notValidDto = TestUtil.bookRequestDto(
                "",
                "",
                null,
                new BigDecimal("-1"),
                null,
                null
        );

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notValidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:database/clear-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBook_Unauthenticated_403() throws Exception {
        BookRequestDto requestDto = TestUtil.bookRequestDto(
                "X",
                "Y",
                null,
                new BigDecimal("5.00"),
                null,
                List.of(1L)
        );

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/clear-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createBook_ForbiddenForUser_403() throws Exception {
        BookRequestDto requestDto = TestUtil.bookRequestDto(
                "X",
                "Y",
                null,
                new BigDecimal("5.00"),
                null,
                List.of(1L)
        );

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/books/add-two-books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllBooks_returns200AndList() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", not(blankOrNullString())));
    }

    @Test
    @WithMockUser(roles = "USER")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/books/add-one-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getBookById_returns200AndBody() throws Exception {
        mockMvc.perform(get("/api/books/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Martin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/category/add-one-category.sql",
            "classpath:database/books/add-one-book.sql",
            "classpath:database/books/link-book-to-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateBook_returns200AndUpdatedBody() throws Exception {
        BookRequestDto update = TestUtil.bookRequestDto(
                "New",
                "Martin",
                null,
                new BigDecimal("15.00"),
                null,
                List.of(1L)
        );

        mockMvc.perform(put("/api/books/{id}", 10L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("New"))
                .andExpect(jsonPath("$.author").value("Martin"))
                .andExpect(jsonPath("$.price").value(15.00));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/books/add-one-book.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteBook_returns200() throws Exception {
        mockMvc.perform(delete("/api/books/{id}", 10L).with(csrf()))
                .andExpect(status().isOk());
    }
}
