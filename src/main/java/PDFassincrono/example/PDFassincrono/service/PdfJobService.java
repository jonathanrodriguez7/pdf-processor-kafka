package PDFassincrono.example.PDFassincrono.service;

import PDFassincrono.example.PDFassincrono.dto.PdfJobResponseDTO;
import PDFassincrono.example.PDFassincrono.model.PdfJob;
import PDFassincrono.example.PDFassincrono.repository.PdfJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfJobService {

    private final PdfJobRepository pdfJobRepository;

    public PdfJobResponseDTO createJob(MultipartFile file) {
        log.info("Criando job para arquivo: {}", file.getOriginalFilename());

        PdfJob job = PdfJob.builder()
                .fileName(file.getOriginalFilename())
                .build();

        PdfJob saved = pdfJobRepository.save(job);

        log.info("Job criado com ID: {}", saved.getId());

        return toDTO(saved);
    }

    public PdfJobResponseDTO getJob(UUID id) {
        PdfJob job = pdfJobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job não encontrado: " + id));

        return toDTO(job);
    }

    private PdfJobResponseDTO toDTO(PdfJob job) {
        return PdfJobResponseDTO.builder()
                .id(job.getId())
                .fileName(job.getFileName())
                .status(job.getStatus())
                .extractedText(job.getExtractedText())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}