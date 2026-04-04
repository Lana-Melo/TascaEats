package pt.ul.fc.css.tascaeats.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.tascaeats.dto.RestaurantResponse;

public class RestaurantController {
    /**
     * GET    /api/restaurants              buscar por restaurante por nome ou cidade (caso de uso D)
     * POST   /api/restaurants              criar restaurante (caso de uso E)
     * PATCH  /api/restaurants/{id}/status   abrir/fechar restaurante (caso de uso E) (é só um campo por isso patch ao inves de put)
     * POST   /api/restaurants/{id}/products        adicionar prduto (caso de uso F)
     * GET    /api/restaurants/{id}/products        buscar produtos de um restaurante (caso de uso G)
     * PUT    /api/restaurants/{id}/products/{produtoid}  atualizar produto (caso de uso F)
     * DELETE /api/restaurants/{id}/products/{produtoid}  remover produto (caso de uso F)
     */

    @GetMapping
    @ApiOperation(value = "Search restaurants", notes = "Returns a list of all restaurants filtered by name or city.")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurant(@RequestParam String filter, @RequestParam String type) { // type se é name ou city
    //a pessoa q chama a api é q decide se quer pesquisar por cidade ou por nome e depois dentro do service / handler é q consoante o filtro chama filterByName ou filterByCity
        List<RestaurantResponse> restaurants = null; // chamo func searchRestaurant(filter, type) do RestaurantService / handler
        return restaurants != null ? ResponseEntity.ok(restaurants) : ResponseEntity.notFound().build();
    }

}
