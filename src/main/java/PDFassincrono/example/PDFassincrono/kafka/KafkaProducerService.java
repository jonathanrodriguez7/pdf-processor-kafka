package PDFassincrono.example.PDFassincrono.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "pdf-processing";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(UUID jobId) {
        log.info("Publicando job no Kafka: {}", jobId);
        kafkaTemplate.send(TOPIC, jobId.toString());
        log.info("Job publicado com sucesso: {}", jobId);
    }
}