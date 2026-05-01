package PDFassincrono.example.PDFassincrono.dto;

import PDFassincrono.example.PDFassincrono.model.JobStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PdfJobResponseDTO {

    private UUID id;
    private String fileName;
    private JobStatus status;
    private String extractedText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}