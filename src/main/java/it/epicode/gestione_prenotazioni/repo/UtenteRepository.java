package it.epicode.gestione_prenotazioni.repo;
import java.util.Optional;
import it.epicode.gestione_prenotazioni.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByUsername(String username);
    boolean existsByUsername(String username);
}
