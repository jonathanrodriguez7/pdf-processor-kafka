package PDFassincrono.example.PDFassincrono.repository;

import PDFassincrono.example.PDFassincrono.model.PdfJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PdfJobRepository extends JpaRepository<PdfJob, UUID> {
}