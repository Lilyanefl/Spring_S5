package it.epicode.gestione_prenotazioni.service;

import it.epicode.gestione_prenotazioni.model.Postazione;
import it.epicode.gestione_prenotazioni.model.Prenotazione;
import it.epicode.gestione_prenotazioni.model.Utente;
import it.epicode.gestione_prenotazioni.repo.PostazioneRepository;
import it.epicode.gestione_prenotazioni.repo.PrenotazioneRepository;
import it.epicode.gestione_prenotazioni.repo.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private PostazioneRepository postazioneRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    public String effettuaPrenotazione(Long postazioneId, String username, LocalDate dataPrenotazione) {

        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Errore: Utente non trovato."));

        Postazione postazione = postazioneRepository.findById(postazioneId)
                .orElseThrow(() -> new RuntimeException("Errore: Postazione non trovata."));

        if (postazione.getEdificio() == null) {
            return "Errore: La postazione non è associata ad alcun edificio.";
        }

        boolean isPostazioneOccupata = postazione.getPrenotazioni().stream()
                .anyMatch(p -> p.getDataPrenotazione().equals(dataPrenotazione));
        if (isPostazioneOccupata) {
            return "Errore: La postazione è già occupata per questa data.";
        }

        boolean utenteHaPrenotazione = !prenotazioneRepository
                .findByUtenteUsernameAndDataPrenotazione(username, dataPrenotazione).isEmpty();
        if (utenteHaPrenotazione) {
            return "Errore: Non puoi prenotare più di una postazione per la stessa data.";
        }

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setPostazione(postazione);
        prenotazione.setUtente(utente);
        prenotazione.setDataPrenotazione(dataPrenotazione);

        prenotazioneRepository.save(prenotazione);
        return "Prenotazione effettuata con successo nell'edificio: " + postazione.getEdificio().getNome();
    }
    public List<Prenotazione> visualizzaTutteLePrenotazioni() {
        return prenotazioneRepository.findAll();
    }





}
