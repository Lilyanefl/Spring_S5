package it.epicode.gestione_prenotazioni.repo;

import it.epicode.gestione_prenotazioni.model.Postazione;
import it.epicode.gestione_prenotazioni.model.TipoPostazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostazioneRepository extends JpaRepository<Postazione, Long> {
    List<Postazione> findByTipoAndEdificio_Citta(TipoPostazione tipo, String citta);
}
