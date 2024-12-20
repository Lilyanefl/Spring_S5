package it.epicode.gestione_prenotazioni.repo;

import it.epicode.gestione_prenotazioni.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByUtenteUsernameAndDataPrenotazione(String username, LocalDate data);
}
