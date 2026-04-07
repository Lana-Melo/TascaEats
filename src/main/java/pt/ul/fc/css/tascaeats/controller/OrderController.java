package pt.ul.fc.css.tascaeats.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponse;

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
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        //OrderResponse order = orderHandler.createOrder(request); // chamo func createOrder(request) do OrderService / handler
        return null; //order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }


}
