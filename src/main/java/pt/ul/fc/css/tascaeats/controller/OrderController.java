package pt.ul.fc.css.tascaeats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponse;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequest;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    //@Autowired
    private final OrderService orderHandler;

    /**
     *  POST   /api/orders                                          criar nova pedido (caso de uso H)                          
     *  POST   /api/orders/{id}/payment                             registar e processar??? pagamento (caso de uso I)                
     *  PATCH  /api/orders/{id}/status                              atualizar estado do pedido (caso de uso J) (é só um campo por isso patch ao inves de put)         
     *  PATCH  /api/orders/{id}/deliveryPerson/{deliveryPersonId}   atribuir entregador ao pedido (caso de uso K) 
     *  DELETE /api/orders/{id}                                     cancelar pedido (caso de uso L)                             
     */

    public OrderController(OrderService orderService) {
        this.orderHandler = orderService;
    }

    //POST   /api/orders -> criar nova pedido (caso de uso H)
    @PostMapping
    @ApiOperation(value = "Create order", notes = "Creates a new order.")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse order = orderHandler.createOrder(request);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    //POST   /api/orders/{id}/payment ->registar e processar??? pagamento (caso de uso I)
    @PostMapping("/payment")
    @ApiOperation(value = "Process payment", notes = "Registers and processes payment of an order.")
    public ResponseEntity<OrderResponse> processPayment(@RequestBody PaymentRequest request) {
        OrderResponse order = orderHandler.processPayment(request);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    //PATCH  /api/orders/{id}/status -> atualizar estado do pedido (caso de uso J) (é só um campo por isso patch ao inves de put)
    @PatchMapping("/{id}/status")
    @ApiOperation(value = "Update order status", notes = "Updates the status of an order.")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        OrderResponse order = orderHandler.updateOrderStatus(id, status.toString()); 
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    //PATCH  /api/orders/{id}/deliveryPerson/{deliveryPersonId} -> atribuir entregador ao pedido (caso de uso K)
    @PatchMapping("/{id}/deliveryPerson/{deliveryPersonId}")
    @ApiOperation(value = "Assign delivery person", notes = "Assigns a delivery person to an order.")
    public ResponseEntity<OrderResponse> assignDeliveryPerson(@PathVariable Long id, @PathVariable Long deliveryPersonId) {
        OrderResponse order = orderHandler.assignDeliveryPerson(id, deliveryPersonId);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

     //DELETE /api/orders/{id} -> cancelar pedido (caso de uso L)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Cancel order", notes = "Cancels an order.")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        //chamo func cancelOrder(id) do OrderService / handler
        //order tem de existir e nao pode estar com estado preparing 
        //mudar status da order pra cancelled 
        //por o deliveryPerson como available
        return orderHandler.cancelOrder(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
