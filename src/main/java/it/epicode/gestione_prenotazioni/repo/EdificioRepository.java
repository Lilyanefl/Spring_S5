package it.epicode.gestione_prenotazioni.repo;

import it.epicode.gestione_prenotazioni.model.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EdificioRepository extends JpaRepository<Edificio, Long> {
}
