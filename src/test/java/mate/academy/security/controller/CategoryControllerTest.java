package mate.academy.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.academy.security.dto.BookDtoWithoutCategoryIds;
import mate.academy.security.dto.CategoryDto;
import mate.academy.security.dto.CategoryRequestDto;
import mate.academy.security.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private CategoryService categoryService;
    @MockitoBean private mate.academy.security.security.JwtUtil jwtUtil;

    @Test
    @DisplayName("GET /categories — 200")
    void getAllCategories_ok() throws Exception {
        CategoryDto first = categoryDto(1L, "Fiction", "Desc1");
        CategoryDto second = categoryDto(2L, "Drama", "Desc2");

        Page<CategoryDto> page = new PageImpl<>(
                List.of(first, second),
                PageRequest.of(0, 20),
                2
        );

        when(categoryService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));

        verify(categoryService).findAll(any(Pageable.class));
    }


    @Test
    @DisplayName("GET /categories/{id} — 200")
    void getCategoryById_ok() throws Exception {
        CategoryDto expected = categoryDto(1L, "Fiction", "Desc");
        when(categoryService.getById(1L)).thenReturn(expected);

        MvcResult result = mockMvc.perform(get("/categories/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );

        assertEquals(expected, actual);

        verify(categoryService).getById(1L);
    }


    @Test
    @DisplayName("POST /categories — 201")
    @WithMockUser(roles = "ADMIN") // если эндпоинт защищён
    void createCategory_created() throws Exception {
        CategoryRequestDto req = categoryRequestDto("New", "New desc");

        // POST
        MvcResult postResult = mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto created = objectMapper.readValue(
                postResult.getResponse().getContentAsString(),
                CategoryDto.class
        );

        CategoryDto expected = categoryDto(created.getId(), "New", "New desc");
        assertEquals(expected, created);

        MvcResult getResult = mockMvc.perform(get("/categories/{id}", created.getId()))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto fetched = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                CategoryDto.class
        );

        assertEquals(created, fetched);
    }

    @Test
    @DisplayName("PUT /categories/{id} — 200 ")
    void updateCategory_ok() throws Exception {
        CategoryRequestDto req = categoryRequestDto("Updated", "Upd desc");
        CategoryDto updated = categoryDto(1L, "Updated", "Upd desc");
        when(categoryService.update(eq(1L), any(CategoryRequestDto.class))).thenReturn(updated);

        mockMvc.perform(put("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.description").value("Upd desc"));

        verify(categoryService).update(eq(1L), any(CategoryRequestDto.class));
    }

    @Test
    @DisplayName("DELETE /categories/{id} — 200")
    void deleteCategory_ok() throws Exception {
        doNothing().when(categoryService).deleteById(1L);

        mockMvc.perform(delete("/categories/{id}", 1L))
                .andExpect(status().isOk());

        verify(categoryService).deleteById(1L);
    }

    @Test
    @DisplayName("GET /categories/{id}/books — 200 и страница книг")
    void getBooksByCategoryId_ok() throws Exception {
        BookDtoWithoutCategoryIds b = bookDtoWithoutCategoryIds(5L, "Title", "Author", "isbn", "desc");
        Page<BookDtoWithoutCategoryIds> page = new PageImpl<>(List.of(b), PageRequest.of(0, 20), 1);
        when(categoryService.getBooksByCategoryId(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/categories/{id}/books", 1L)
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(5))
                .andExpect(jsonPath("$.content[0].title").value("Title"));

        verify(categoryService).getBooksByCategoryId(eq(1L), any(Pageable.class));
    }

    private CategoryDto categoryDto(Long id, String name, String description) {
        CategoryDto dto = new CategoryDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        return dto;
    }

    private CategoryRequestDto categoryRequestDto(String name, String description) {
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName(name);
        dto.setDescription(description);
        return dto;
    }

    private BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds(Long id, String title, String author, String isbn, String description) {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setId(id);
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setIsbn(isbn);
        dto.setDescription(description);
        return dto;
    }
}
