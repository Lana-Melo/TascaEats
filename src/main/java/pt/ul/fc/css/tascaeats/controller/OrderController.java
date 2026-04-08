package pt.ul.fc.css.tascaeats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponse;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequest;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    //@Autowired
    //private OrderService orderHandler;

    /**
     *  POST   /api/orders                                          criar nova pedido (caso de uso H)                          
     *  POST   /api/orders/{id}/payment                             registar e procesar??? pagamento (caso de uso I)                
     *  PATCH  /api/orders/{id}/status                              atualizar estado do pedido (caso de uso J) (é só um campo por isso patch ao inves de put)         
     *  PATCH  /api/orders/{id}/deliveryPerson/{deliveryPersonId}   atribuir entregador ao pedido (caso de uso K) 
     *  DELETE /api/orders/{id}                                     cancelar pedido (caso de uso L)                             
     */


    //POST   /api/orders -> criar nova pedido (caso de uso H)
    @PostMapping
    @ApiOperation(value = "Create order", notes = "Creates a new order.")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse order = null; //orderHandler.createOrder(request); // chamo func createOrder(request) do OrderService / handler
        //restaurant tem de existir e estar aberto, client tem de existir, produtos tem de existir, estar disponiveis e pertencer ao restaurante 
        //faz se set da orderStatus pra created (penso eu)
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    //POST   /api/orders/{id}/payment ->registar e procesar??? pagamento (caso de uso I)
    @PostMapping("/{id}/payment")
    @ApiOperation(value = "Process payment", notes = "Registers and processes payment of an order.")
    public ResponseEntity<OrderResponse> processPayment(@PathVariable Long id, @RequestBody PaymentRequest request) {
        OrderResponse order = null; //orderHandler.processPayment(id); // chamo func processPayment(id) do OrderService / handler
        //order nao pode estar com estado paid ou cancelled (nao vamos pagar algo q ja foi pago ou que ja foi cancelado) 
        //ammount findo do payment request tem de ser igual ao totalValue da order
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    //PATCH  /api/orders/{id}/status -> atualizar estado do pedido (caso de uso J) (é só um campo por isso patch ao inves de put)
    @PatchMapping("/{id}/status")
    @ApiOperation(value = "Update order status", notes = "Updates the status of an order.")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        OrderResponse order = null; //orderHandler.updateOrderStatus(id, status); // chamo func updateOrderStatus(id, status) do OrderService / handler
        //a ordem do status tem de ser coerente (ex: nao se pode passar de created para delivered, tem de passar por paid e depois por preparing e depois por ready ou algo do genero)
        //se for o estado delivered guarda se o timestamp da entrega e torna se o deliveryPerson available again????? n sei se sera aqui (mas acho q este é o unico estado q se deve fazer aglguma coisa)
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    //PATCH  /api/orders/{id}/deliveryPerson/{deliveryPersonId} -> atribuir entregador ao pedido (caso de uso K)
    @PatchMapping("/{id}/deliveryPerson/{deliveryPersonId}")
    @ApiOperation(value = "Assign delivery person", notes = "Assigns a delivery person to an order.")
    public ResponseEntity<OrderResponse> assignDeliveryPerson(@PathVariable Long id, @PathVariable Long deliveryPersonId) {
        OrderResponse order = null; //orderHandler.assignDeliveryPerson(id, deliveryPersonId); // chamo func assignDeliveryPerson(id, deliveryPersonId) do OrderService / handler
        //order tem de existir e estar com estado ready (nao se pode atribuir um entregador a uma order que ainda nao esta pronta, certo?)
        //delivery person tem de existir e estar available 
        //registar o picked up timestamp e mudar deliveryPerson pra busy 
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
        return null; //orderHandler.cancelOrder(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
