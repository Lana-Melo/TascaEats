package pt.ul.fc.css.tascaeats.config;

import java.time.LocalTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.ul.fc.css.tascaeats.entities.*;
import pt.ul.fc.css.tascaeats.entities.enums.*;
import pt.ul.fc.css.tascaeats.repository.*;

/**
 * Popula a base de dados com dados iniciais ao arrancar a aplicacao.
 *
 * <p>Dados criados: Clientes : moscateira1 (id 1), moscateira2 (id 2), moscateira3 (id 3) Admins :
 * diabinho_joelho (id 4), lana_dracarys (id 5), totia123 (id 6) Entregadores: entregador1 (id 7),
 * entregador2 (id 8) Restaurantes: Tasca do Brunao (id 1, OPEN), Cantinho da budega (id 2, OPEN)
 * Produtos : arroz doce (id 1), batata frita (id 2, SOLD_OUT), arroz (id 3)
 *
 * <p>Nota: o produto 2 (batata frita) fica SOLD_OUT para poder demonstrar essa validacao no video.
 */
@Configuration
public class BdLoader {

  @Bean
  CommandLineRunner seedDatabase(
      UserRepository userRepository,
      RestaurantRepository restaurantRepository,
      ProductRepository productRepository,
      MenuRepository menuRepository,
      ProductItemRepository productItemRepository) {
    return args -> {

      // so executa se a bd estiver vazia
      if (userRepository.count() > 0) {
        System.out.println(
            "\u001B[33m[DataLoader] Base de dados ja populada, a saltar seed.\u001B[0m");
        return;
      }

      System.out.println("\u001B[32m[DataLoader] A popular base de dados...\u001B[0m");

      // ── CLIENTES ──────────────────────────────────────────────────
      Client moscateira1 =
          userRepository.save(
              new Client(
                  "moscateira1",
                  "123",
                  "Lana Melo",
                  "912456322",
                  "234509221",
                  List.of(new Address("avenida mal penteada", "Ilha do Sol", "2352-123"))));

      Client moscateira2 =
          userRepository.save(
              new Client(
                  "moscateira2",
                  "456",
                  "Bruno Antunes",
                  "933333333",
                  "123456333",
                  List.of(new Address("rua alto da charneca", "Aldeia dos Fininhos", "2090-123"))));

      Client moscateira3 =
          userRepository.save(
              new Client(
                  "moscateira3",
                  "789",
                  "Sofia Rodrigues",
                  "955456357",
                  "23477777",
                  List.of(new Address("rua das minas", "Mangualde", "2050-391"))));

      // ── ADMINS ────────────────────────────────────────────────────
      Admin admin1 =
          userRepository.save(
              new Admin("diabinho_joelho", "hehehe", "Brunao Panettonao", "911111111", "12225976"));

      Admin admin2 =
          userRepository.save(
              new Admin("lana_dracarys", "wee", "Laninha Bruschetta", "998765123", "199254424"));

      Admin admin3 =
          userRepository.save(
              new Admin("totia123", "opa2", "Sofi Ravioli", "945222333", "28925970"));

      // ── ENTREGADORES ──────────────────────────────────────────────
      DeliveryPerson entregador1 =
          userRepository.save(
              new DeliveryPerson("entregador1", "222", "Bruno Turbo", "945678901", "100000004"));

      DeliveryPerson entregador2 =
          userRepository.save(
              new DeliveryPerson("entregador2", "777", "Lana Foguete", "945678902", "100000005"));

      // ── MENUS ──────────────────────────────────────────────
      Menu menuBrunao =
          menuRepository.save(
              new Menu(
                  "Menu do Brunao",
                  "Britanico",
                  "So good"));

      Menu menuBudega =
          menuRepository.save(
              new Menu(
                  "Menu da Budega",
                  "Italiano",
                  "Mama mia"));
      
      // ── PRODUTOS — Tasca do Brunao ─────────────────────────────
      // produto 1: disponivel — vai ser usado no pedido de demo
      Product arrozDoce =
          productRepository.save(
              new Product(
                  2.50, "sobremesa simples que brunao recomenda", "arroz doce", menuBrunao));

      // produto 2: SOLD_OUT — para demonstrar validacao de produto esgotado
      Product batatafrita =
          productRepository.save(
              new Product(
                  4.50,
                  "acompanhamento que brunao nao aprecia",
                  "dose de batata frita",
                  menuBrunao));

      // produto 3: disponivel — vai ser usado no pedido de demo
      Product arroz =
          productRepository.save(new Product(4.50, "arroz branco soltinho", "arroz", menuBrunao));

      // ── PRODUTOS — Cantinho da budega ────────────────────────────
      Product francesinha =
          productRepository.save(
              new Product(9.00, "a classica do norte", "francesinha", menuBudega));

      // ── RESTAURANTES ──────────────────────────────────────────────
      Restaurant tascaBrunao =
          restaurantRepository.save(
              new Restaurant(
                  "6060606060",
                  "Tasca do Brunao",
                  new Address("rua sesamo", "Almada", "2078-317"),
                  admin1, 
                  LocalTime.of(11,0), 
                  LocalTime.of(22,0)));
        
      tascaBrunao.setMenu(menuBrunao);

      ProductItem piArrozDoce = productItemRepository.save(new ProductItem(arrozDoce, tascaBrunao));
      ProductItem piBatatas   = productItemRepository.save(new ProductItem(batatafrita, tascaBrunao));
      ProductItem piArroz     = productItemRepository.save(new ProductItem(arroz, tascaBrunao));

      piBatatas.setStatus(ProductStatus.SOLD_OUT);
      productItemRepository.save(piBatatas);

      restaurantRepository.save(tascaBrunao);

      Restaurant cantinhoBudega =
          restaurantRepository.save(
              new Restaurant(
                  "81818181818",
                  "Cantinho da budega",
                  new Address("rua barriga cheia", "Porto", "1034-123"),
                  admin2,
                  LocalTime.of(11,0), 
                  LocalTime.of(22,0)));

      cantinhoBudega.setMenu(menuBudega);
      productItemRepository.save(new ProductItem(francesinha, cantinhoBudega));
      restaurantRepository.save(cantinhoBudega);

      

      System.out.println("\u001B[32m[DataLoader] Script pra popular bd concluido!\u001B[0m");
      System.out.println(
          "\u001B[32m[DataLoader] Clientes: "
              + moscateira1.getUsername()
              + " (id="
              + moscateira1.getId()
              + "), "
              + moscateira2.getUsername()
              + " (id="
              + moscateira2.getId()
              + "), "
              + moscateira3.getUsername()
              + " (id="
              + moscateira3.getId()
              + ")\u001B[0m");
      System.out.println(
          "\u001B[32m[DataLoader] Admins: "
              + admin1.getUsername()
              + " (id="
              + admin1.getId()
              + "), "
              + admin2.getUsername()
              + " (id="
              + admin2.getId()
              + ")\u001B[0m");
      System.out.println(
          "\u001B[32m[DataLoader] Entregadores: "
              + entregador1.getUsername()
              + " (id="
              + entregador1.getId()
              + "), "
              + entregador2.getUsername()
              + " (id="
              + entregador2.getId()
              + ")\u001B[0m");
      System.out.println(
          "\u001B[32m[DataLoader] Restaurantes: "
              + tascaBrunao.getDesignation()
              + " (id="
              + tascaBrunao.getId()
              + "), "
              + cantinhoBudega.getDesignation()
              + " (id="
              + cantinhoBudega.getId()
              + ")\u001B[0m");
      System.out.println(
          "\u001B[32m[DataLoader] Produtos: arroz doce (id="
              + arrozDoce.getId()
              + "), batata frita SOLD_OUT (id="
              + batatafrita.getId()
              + "), arroz (id="
              + arroz.getId()
              + "), francesinha (id="
              + francesinha.getId()
              + ")\u001B[0m");
    };
  }
}
