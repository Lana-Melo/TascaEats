package pt.ul.fc.css.tascaeats.delivery.kafka;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.common.dto.KafkaResponse;

@Component
public class KafkaResponseStore {

  private final SynchronousQueue<KafkaResponse<String>> deleteDecisionQueue =
      new SynchronousQueue<>();

  public void putDeleteDecision(KafkaResponse<String> decision) throws InterruptedException {
    deleteDecisionQueue.put(decision);
  }

  public KafkaResponse<String> waitForDeleteDecision(long timeoutMs) throws InterruptedException {
    return deleteDecisionQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
  }
}
