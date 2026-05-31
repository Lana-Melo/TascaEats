package pt.ul.fc.css.tascaeats.delivery.service;

import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.delivery.dto.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.delivery.dto.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.delivery.kafka.Sender;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryPersonRepository;
import pt.ul.fc.css.tascaeats.delivery.util.Mapper;

@Service
public class DeliveryPersonService {

  private final DeliveryPersonRepository deliveryPersonRepository;
  private final Sender sender;

  public DeliveryPersonService(DeliveryPersonRepository deliveryPersonRepository, Sender sender) {
    this.deliveryPersonRepository = deliveryPersonRepository;
    this.sender = sender;
  }

  public void registerNewDeliveryPerson(DeliveryPersonRequestDto request) {
    if (deliveryPersonRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new IllegalArgumentException("Username already exists.");
    }
    DeliveryPerson deliveryPerson =
        new DeliveryPerson(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getPhoneNumber(),
            request.getNif());
    DeliveryPerson saved = deliveryPersonRepository.save(deliveryPerson);

    DeliveryPersonResponseDto response = Mapper.mapDeliveryToDeliveryPersonResponse(saved);
    sender.sendAsJson("delivery-person-created", response);
  }
}
