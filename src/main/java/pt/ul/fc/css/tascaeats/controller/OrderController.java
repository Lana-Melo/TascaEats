package pt.ul.fc.css.tascaeats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponse;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequest;

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
        //order tem de estar com status created (nao vamos pagar algo q ja foi pago ou que ja foi cancelado)
        //nao sei quando vamos usar o status preparing 
        //order response depois vem com o status da order atualizado para paid
        //
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

}
