package mate.academy.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.security.dto.OrderItemDto;
import mate.academy.security.service.OrderItemService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/{orderId}/items")
@Tag(name = "Order Items", description = "Order items within a specific order")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping
    @Operation(summary = "Get all order items for a specific order")
    public List<OrderItemDto> getItems(@PathVariable Long orderId) {
        return orderItemService.getItems(orderId);
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "Get a specific order item within an order")
    public OrderItemDto getItem(@PathVariable Long orderId,
                                @PathVariable Long itemId) {
        return orderItemService.getItem(orderId, itemId);
    }
}
