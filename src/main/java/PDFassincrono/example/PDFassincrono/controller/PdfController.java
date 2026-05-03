package PDFassincrono.example.PDFassincrono.controller;

import PDFassincrono.example.PDFassincrono.dto.PdfJobResponseDTO;
import PDFassincrono.example.PDFassincrono.kafka.KafkaProducerService;
import PDFassincrono.example.PDFassincrono.service.PdfJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfJobService pdfJobService;
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/upload")
    public ResponseEntity<PdfJobResponseDTO> upload(@RequestParam("file") MultipartFile file) {
        log.info("Recebendo upload: {}", file.getOriginalFilename());

        PdfJobResponseDTO job = pdfJobService.createJob(file);
        kafkaProducerService.sendMessage(job.getId());

        return ResponseEntity.accepted().body(job);
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<PdfJobResponseDTO> getStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(pdfJobService.getJob(id));
    }
}