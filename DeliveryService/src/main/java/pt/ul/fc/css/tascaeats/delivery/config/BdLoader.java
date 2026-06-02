package pt.ul.fc.css.tascaeats.delivery.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryPersonRepository;

@Configuration
public class BdLoader {

  @Bean
  CommandLineRunner seedDatabase(DeliveryPersonRepository deliveryPersonRepository) {
    return args -> {
      if (deliveryPersonRepository.count() > 0) {
        System.out.println("[33m[DataLoader] Base de dados ja populada, a saltar seed.[0m");
        return;
      }

      System.out.println("[32m[DataLoader] A popular base de dados...[0m");

      DeliveryPerson entregador1 =
          deliveryPersonRepository.save(
              new DeliveryPerson("entregador1", "222", "Bruno Turbo", "945678901", "100000004"));

      DeliveryPerson entregador2 =
          deliveryPersonRepository.save(
              new DeliveryPerson("entregador2", "777", "Lana Foguete", "945678902", "100000005"));

      System.out.println("[32m[DataLoader] Script pra popular bd concluido![0m");
      System.out.println(
          "[32m[DataLoader] Entregadores: "
              + entregador1.getUsername()
              + " (id="
              + entregador1.getId()
              + "), "
              + entregador2.getUsername()
              + " (id="
              + entregador2.getId()
              + ")[0m");
    };
  }
}
