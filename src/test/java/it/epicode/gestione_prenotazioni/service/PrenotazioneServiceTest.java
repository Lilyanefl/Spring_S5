package it.epicode.gestione_prenotazioni.service;

import it.epicode.gestione_prenotazioni.model.Prenotazione;
import it.epicode.gestione_prenotazioni.model.Postazione;
import it.epicode.gestione_prenotazioni.model.TipoPostazione;
import it.epicode.gestione_prenotazioni.model.Utente;

import it.epicode.gestione_prenotazioni.repo.PostazioneRepository;
import it.epicode.gestione_prenotazioni.repo.PrenotazioneRepository;
import it.epicode.gestione_prenotazioni.repo.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PrenotazioneServiceTest {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PostazioneRepository postazioneRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Test
    public void testEffettuaPrenotazioneConH2() {
        Utente utente = new Utente();
        utente.setUsername("user1");
        utente.setNomeCompleto("Mario Rossi");
        utente.setEmail("mario.rossi@example.com");
        utenteRepository.save(utente);

        Postazione postazione = new Postazione();
        postazione.setDescrizione("Postazione Open Space");
        postazione.setTipo(TipoPostazione.OPENSPACE);
        postazione.setNumeroMassimoOccupanti(5);
        postazioneRepository.save(postazione);
        //String risultato = prenotazioneService.effettuaPrenotazione(postazione.getId(), utente.getUsername(), LocalDate.now());

        //assertEquals("Prenotazione effettuata con successo.", risultato);
        assertEquals(1, prenotazioneRepository.findAll().size());
    }
}
