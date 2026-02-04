package mate.academy.security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.security.security.status.OrderStatus;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private OrderStatus status;
}
