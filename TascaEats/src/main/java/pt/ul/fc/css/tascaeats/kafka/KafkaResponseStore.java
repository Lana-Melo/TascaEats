package pt.ul.fc.css.tascaeats.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.common.dto.KafkaResponse;

@Component
public class KafkaResponseStore {

  private static final ObjectMapper mapper =
      new ObjectMapper().registerModule(new JavaTimeModule());

  private final SynchronousQueue<List<DeliveryResponseDto>> deliveriesQueue =
      new SynchronousQueue<>();
  private final SynchronousQueue<KafkaResponse<DeliveryResponseDto>> deliveryQueue =
      new SynchronousQueue<>();
  private final SynchronousQueue<KafkaResponse<DeliveryPersonResponseDto>> deliveryPersonQueue =
      new SynchronousQueue<>();
  // void pq nao ha conteudo efetivo, mas importa saber o status da operacao (sucesso ou falha)
  private final SynchronousQueue<KafkaResponse<String>> deliveryPersonDeleteQueue =
      new SynchronousQueue<>();
  private final SynchronousQueue<KafkaResponse<List<DeliveryPersonResponseDto>>>
      deliveryPersonSearchQueue = new SynchronousQueue<>();

  public void putDeliveries(String json) throws Exception {
    List<DeliveryResponseDto> list = mapper.readValue(json, new TypeReference<>() {});
    deliveriesQueue.put(list);
  }

  public List<DeliveryResponseDto> waitForDeliveries(long timeoutMs) throws Exception {
    return deliveriesQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
  }

  public void putDelivery(String json) throws Exception {
    KafkaResponse<DeliveryResponseDto> response =
        mapper.readValue(json, new TypeReference<KafkaResponse<DeliveryResponseDto>>() {});
    deliveryQueue.put(response);
  }

  public KafkaResponse<DeliveryResponseDto> waitForDelivery(long timeoutMs) throws Exception {
    return deliveryQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
  }

  public void putDeliveryPerson(String json) throws Exception {
    KafkaResponse<DeliveryPersonResponseDto> dto =
        mapper.readValue(json, new TypeReference<KafkaResponse<DeliveryPersonResponseDto>>() {});
    deliveryPersonQueue.put(dto);
  }

  public KafkaResponse<DeliveryPersonResponseDto> waitForDeliveryPerson(long timeoutMs)
      throws Exception {
    return deliveryPersonQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
  }

  public void putDeliveryPersonDelete(String json) throws Exception {
    KafkaResponse<String> dto =
        mapper.readValue(json, new TypeReference<KafkaResponse<String>>() {});
    deliveryPersonDeleteQueue.put(dto);
  }

  public KafkaResponse<String> waitForDeliveryPersonDelete(long timeoutMs) throws Exception {
    return deliveryPersonDeleteQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
  }

  public void putDeliveryPersonSearch(String json) throws Exception {
    KafkaResponse<List<DeliveryPersonResponseDto>> dto =
        mapper.readValue(
            json, new TypeReference<KafkaResponse<List<DeliveryPersonResponseDto>>>() {});
    deliveryPersonSearchQueue.put(dto);
  }

  public KafkaResponse<List<DeliveryPersonResponseDto>> waitForDeliveryPersonSearch(long timeoutMs)
      throws Exception {
    return deliveryPersonSearchQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
  }
}
