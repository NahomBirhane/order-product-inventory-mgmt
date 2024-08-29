package miu.edu.orderservice.service;

import lombok.RequiredArgsConstructor;
import miu.edu.orderservice.dtos.InventoryResponse;
import miu.edu.orderservice.dtos.OrderLineItemsDto;
import miu.edu.orderservice.dtos.OrderRequest;
import miu.edu.orderservice.model.Order;
import miu.edu.orderservice.model.OrderLineItems;
import miu.edu.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){

        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtosList().stream().map(this::mapToDto).toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStoke);
        if(allProductsInStock) {
            orderRepository.save(order);
        }else throw new IllegalArgumentException("Product is Not in Stock. Please try again.");
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

       OrderLineItems orderLineItems = new OrderLineItems();

       orderLineItems.setId(orderLineItemsDto.getId());
       orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
       orderLineItems.setPrice(orderLineItemsDto.getPrice());
       orderLineItems.setQuantity(orderLineItemsDto.getQuantity());

       return orderLineItems;
    }
}
