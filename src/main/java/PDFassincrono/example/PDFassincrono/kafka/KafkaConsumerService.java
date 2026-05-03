package PDFassincrono.example.PDFassincrono.kafka;

import PDFassincrono.example.PDFassincrono.model.JobStatus;
import PDFassincrono.example.PDFassincrono.model.PdfJob;
import PDFassincrono.example.PDFassincrono.repository.PdfJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final PdfJobRepository pdfJobRepository;

    @KafkaListener(topics = "pdf-processing", groupId = "pdf-processor-group")
    public void consume(ConsumerRecord<String, String> record) {
        UUID jobId = UUID.fromString(record.value());
        log.info("Mensagem recebida do Kafka - jobId: {}", jobId);

        PdfJob job = pdfJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job não encontrado: " + jobId));

        try {
            log.info("Iniciando processamento do job: {}", jobId);
            job.setStatus(JobStatus.PROCESSING);
            pdfJobRepository.save(job);

            // Por enquanto simula o processamento
            // Na próxima etapa vamos integrar o PDFBox aqui
            Thread.sleep(2000);

            job.setStatus(JobStatus.DONE);
            job.setExtractedText("Texto extraído com sucesso - PDFBox será integrado na próxima etapa");
            pdfJobRepository.save(job);

            log.info("Job finalizado com sucesso: {}", jobId);

        } catch (Exception e) {
            log.error("Erro ao processar job {}: {}", jobId, e.getMessage());
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());
            pdfJobRepository.save(job);
        }
    }
}