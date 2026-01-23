package mate.academy.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.security.dto.CategoryDto;
import mate.academy.security.dto.CategoryRequestDto;
import mate.academy.security.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /categories — 200")
    @WithMockUser(roles = "USER")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/category/add-two-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllCategories_ok() throws Exception {
        mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Fiction"))
                .andExpect(jsonPath("$.content[0].description").value("Desc1"))
                .andExpect(jsonPath("$.content[1].name").value("Drama"))
                .andExpect(jsonPath("$.content[1].description").value("Desc2"));
    }

    @Test
    @DisplayName("GET /categories/{id} — 200")
    @WithMockUser(roles = "USER")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/category/add-two-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getCategoryById_ok() throws Exception {
        CategoryDto expected = TestUtil.categoryDto(1L, "Fiction", "Desc1");

        String json = mockMvc.perform(get("/categories/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        CategoryDto actual = objectMapper.readValue(json, CategoryDto.class);
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("POST /categories — 201")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/clear-db.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createCategory_created() throws Exception {
        CategoryRequestDto requestDto = TestUtil.categoryRequestDto("New", "New desc");

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New"))
                .andExpect(jsonPath("$.description").value("New desc"));
    }

    @Test
    @DisplayName("PUT /categories/{id} — 200")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/category/add-two-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateCategory_ok() throws Exception {
        CategoryRequestDto requestDto = TestUtil.categoryRequestDto("Updated", "Upd desc");

        mockMvc.perform(put("/categories/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.description").value("Upd desc"));
    }

    @Test
    @DisplayName("DELETE /categories/{id} — 200")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = {
            "classpath:database/clear-db.sql",
            "classpath:database/category/add-two-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategory_ok() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

}