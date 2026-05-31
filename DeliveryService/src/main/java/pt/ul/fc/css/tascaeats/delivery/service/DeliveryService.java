package pt.ul.fc.css.tascaeats.delivery.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.kafka.common.errors.ResourceNotFoundException;

import jakarta.transaction.Transactional;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.delivery.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.delivery.kafka.Sender;
import pt.ul.fc.css.tascaeats.delivery.model.Delivery;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryStatus;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryPersonRepository;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryRepository;
import pt.ul.fc.css.tascaeats.delivery.util.Mapper;

public class DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final Sender sender;

    public DeliveryService(DeliveryRepository deliveryRepository, DeliveryPersonRepository deliveryPersonRepository, Sender sender) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.sender = sender;
    }

    public DeliveryResponseDto createDelivery(Long orderId) {
        Delivery delivery = new Delivery(LocalDateTime.now(), orderId);
        Delivery saved = deliveryRepository.save(delivery);
        return Mapper.mapDeliveryToDeliveryResponse(saved);
    }

    public List<DeliveryResponseDto> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(Mapper::mapDeliveryToDeliveryResponse)
                .toList();
    }

    @Transactional
    public DeliveryResponseDto assignDeliveryPerson(Long id, Long deliveryPersonId) {
        Optional<Delivery> deliveryOpt = deliveryRepository.findById(id);
        if (deliveryOpt.isEmpty()) {
            throw new ResourceNotFoundException("Delivery not found");
        }
        Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOpt.isEmpty()) {
            throw new ResourceNotFoundException("Delivery person not found");
        }

        DeliveryPerson del = (DeliveryPerson) deliveryPersonOpt.get();
        Delivery delivery = deliveryOpt.get();
        if (del.getDeliveryPersonStatus() == DeliveryPersonStatus.BUSY) {
            throw new RequirementsNotMetException("Delivery person must be available");
        }
        delivery.setDeliveryPerson(del);
        del.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);

        deliveryPersonRepository.save(del);
        Delivery saved = deliveryRepository.save(delivery);
        return Mapper.mapDeliveryToDeliveryResponse(saved);
    }

    @Transactional
    public DeliveryResponseDto updateDeliveryStatus(Long id) {
        Optional<Delivery> ordOpt = deliveryRepository.findById(id);
        if (ordOpt.isEmpty()) {
            throw new ResourceNotFoundException("Delivery not found");
        }
        Delivery delivery = ordOpt.get();

        if (delivery.getStatus() != DeliveryStatus.INCOMING) {
            throw new RequirementsNotMetException("Delivery has already been delivered");
        }
        delivery.setDeliveryTimeStamp();
        DeliveryPerson del = delivery.getDeliveryPerson();
        del.setDeliveryPersonStatus(DeliveryPersonStatus.AVAILABLE);
        deliveryPersonRepository.save(del);

        delivery.setStatus(DeliveryStatus.DELIVERED);
        Delivery saved = deliveryRepository.save(delivery);
        return Mapper.mapDeliveryToDeliveryResponse(saved);
    }
}
