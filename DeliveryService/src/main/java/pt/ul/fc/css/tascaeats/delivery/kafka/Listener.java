package pt.ul.fc.css.tascaeats.delivery.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.KafkaHeaders;

import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class Listener {

    @Autowired
    Sender sender;

    /*@Autowired
    private CustomerService customerService;*/

    @RetryableTopic(attempts = "4", // 1 + 3
            backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")

    /*@KafkaListener(topics = Topics.SALE_CREATE, groupId = "salesys-group")
    public void listenSaleEvents(String requestMsg) {
        System.out.println("Received sale event: " + requestMsg);
        Request<SaleCreateRequestDto> request = Sender.fromJson(
                requestMsg, new TypeReference<Request<SaleCreateRequestDto>>() {
                });

        CustomerResponseDto customerDto = customerService.getCustomerByVat(request.entityDto().vat());
        Request<CustomerResponseDto> response = new Request<>(request.callbackUrl(), request.correlationId(),
                customerDto);
        sender.sendAsJson(Topics.CONSUMER_VAT, response);
        System.out.println("Sent message: " + response);
    }*/

    @DltHandler
    public void handleDlt(String requestMsg,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String error) {
        System.out.println("Dead because: " + error);
        // warn the admin (e.g., e-email)
    }

}
