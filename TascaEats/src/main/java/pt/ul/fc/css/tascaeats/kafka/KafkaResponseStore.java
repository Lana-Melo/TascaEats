package pt.ul.fc.css.tascaeats.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.dto.delivery.DeliveryResponseDto;

@Component
public class KafkaResponseStore {

  private static final ObjectMapper mapper =
      new ObjectMapper().registerModule(new JavaTimeModule());

  private final SynchronousQueue<List<DeliveryResponseDto>> deliveriesQueue =
      new SynchronousQueue<>();
  private final SynchronousQueue<DeliveryResponseDto> deliveryQueue = new SynchronousQueue<>();

  public void putDeliveries(String json) throws Exception {
    List<DeliveryResponseDto> list = mapper.readValue(json, new TypeReference<>() {});
    deliveriesQueue.put(list);
  }

  public List<DeliveryResponseDto> waitForDeliveries(long timeoutMs) throws Exception {
    return deliveriesQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
  }

  public void putDelivery(String json) throws Exception {
    DeliveryResponseDto dto = mapper.readValue(json, DeliveryResponseDto.class);
    deliveryQueue.put(dto);
  }

  public DeliveryResponseDto waitForDelivery(long timeoutMs) throws Exception {
    return deliveryQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
  }
}
