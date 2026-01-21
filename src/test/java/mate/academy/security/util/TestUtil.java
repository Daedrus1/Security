package mate.academy.security.util;

import mate.academy.security.dto.CategoryDto;
import mate.academy.security.dto.CategoryRequestDto;

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
}