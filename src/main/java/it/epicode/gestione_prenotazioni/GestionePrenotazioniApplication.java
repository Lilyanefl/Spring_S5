package it.epicode.gestione_prenotazioni;

import it.epicode.gestione_prenotazioni.model.Edificio;
import it.epicode.gestione_prenotazioni.model.Postazione;
import it.epicode.gestione_prenotazioni.model.TipoPostazione;
import it.epicode.gestione_prenotazioni.model.Utente;
import it.epicode.gestione_prenotazioni.repo.EdificioRepository;
import it.epicode.gestione_prenotazioni.service.PrenotazioneService;
import it.epicode.gestione_prenotazioni.repo.UtenteRepository;
import it.epicode.gestione_prenotazioni.repo.PostazioneRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class GestionePrenotazioniApplication implements CommandLineRunner {

	@Autowired
	private PrenotazioneService prenotazioneService;
	@Autowired
	private UtenteRepository utenteRepository;
	@Autowired
	private PostazioneRepository postazioneRepository;
	@Autowired
	private EdificioRepository edificioRepository;

	public static void main(String[] args) {
		SpringApplication.run(GestionePrenotazioniApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		boolean exit = false;

		while (!exit) {
			System.out.println("\n--- MENU GESTIONE PRENOTAZIONI ---");
			System.out.println("1. Aggiungi un nuovo utente");
			System.out.println("2. Aggiungi una nuova postazione");
			System.out.println("3. Effettua una prenotazione");
			System.out.println("4. Visualizza tutte le prenotazioni");
			System.out.println("5. Aggiungi un nuovo edificio");
			System.out.println("6. Visualizza tutti gli edifici");
			System.out.println("7. Cerca postazioni per tipo e città");
			System.out.println("8. Esci");
			System.out.print("Seleziona un'opzione: ");

			int scelta = scanner.nextInt();
			scanner.nextLine();

			switch (scelta) {
				case 1:
					aggiungiUtente(scanner);
					break;
				case 2:
					aggiungiPostazione(scanner);
					break;
				case 3:
					effettuaPrenotazione(scanner);
					break;
				case 4:
					visualizzaPrenotazioni();
					break;
				case 5:
					aggiungiEdificio(scanner);
					break;
				case 6:
					visualizzaEdifici();
					break;
				case 7:
					cercaPostazioniPerTipoECitta(scanner);
					break;
				case 8:
					System.out.println("Uscita in corso, ciau!");
					exit = true;
					break;
				default:
					System.out.println("Scelta non valida. Riprova.");
			}
		}
		scanner.close();
	}

	private void aggiungiUtente(Scanner scanner) {
		System.out.println("\n--- Aggiungi nuovo utente ---");
		try {
			System.out.print("Username: ");
			String username = scanner.nextLine().trim();
			if (username.isEmpty()) {
				System.out.println("Errore: Il campo username non può essere vuoto.");
				return;
			}
			if (utenteRepository.existsByUsername(username)) {
				System.out.println("Errore: Username già esistente.");
				return;
			}

			System.out.print("Nome Completo: ");
			String nomeCompleto = scanner.nextLine().trim();
			if (nomeCompleto.isEmpty()) {
				System.out.println("Errore: Il nome completo non può essere vuoto.");
				return;
			}

			System.out.print("Email: ");
			String email = scanner.nextLine().trim();
			if (email.isEmpty() || !email.contains("@")) {
				System.out.println("Errore: Inserisci un'email valida.");
				return;
			}

			Utente utente = new Utente();
			utente.setUsername(username);
			utente.setNomeCompleto(nomeCompleto);
			utente.setEmail(email);

			utenteRepository.save(utente);
			System.out.println("Utente aggiunto con successo!");
		} catch (Exception e) {
			System.out.println("Errore durante l'aggiunta dell'utente: " + e.getMessage());
		}
	}

	private void aggiungiPostazione(Scanner scanner) {
		System.out.println("\n--- Aggiungi nuova postazione ---");
		try {
			System.out.print("Descrizione: ");
			String descrizione = scanner.nextLine().trim();
			if (descrizione.isEmpty()) {
				System.out.println("Errore: La descrizione non può essere vuota.");
				return;
			}

			TipoPostazione tipoPostazione = null;
			boolean tipoValido = false;
			while (!tipoValido) {
				System.out.print("Tipo (OPENSPACE/PRIVATO/SALA_RIUNIONI): ");
				String tipoInput = scanner.nextLine().toUpperCase().trim();
				try {
					tipoPostazione = TipoPostazione.valueOf(tipoInput);
					tipoValido = true;
				} catch (IllegalArgumentException e) {
					System.out.println("Errore: Tipo non valido. Inserisci uno tra OPENSPACE, PRIVATO o SALA_RIUNIONI.");
				}
			}

			System.out.print("Numero massimo di occupanti: ");
			int numeroOccupanti;
			try {
				numeroOccupanti = Integer.parseInt(scanner.nextLine());
				if (numeroOccupanti <= 0) {
					System.out.println("Errore: Il numero di occupanti deve essere maggiore di 0.");
					return;
				}
			} catch (NumberFormatException e) {
				System.out.println("Errore: Inserisci un numero valido.");
				return;
			}

			System.out.println("Seleziona un edificio dall'elenco:");
			visualizzaEdifici();
			System.out.print("ID Edificio: ");
			Long edificioId;
			try {
				edificioId = Long.parseLong(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Errore: Inserisci un ID valido.");
				return;
			}

			Edificio edificio = edificioRepository.findById(edificioId)
					.orElseThrow(() -> new RuntimeException("Errore: Edificio non trovato."));

			Postazione postazione = new Postazione();
			postazione.setDescrizione(descrizione);
			postazione.setTipo(tipoPostazione);
			postazione.setNumeroMassimoOccupanti(numeroOccupanti);
			postazione.setEdificio(edificio);

			postazioneRepository.save(postazione);
			System.out.println("Postazione aggiunta con successo!");
		} catch (Exception e) {
			System.out.println("Errore durante l'aggiunta della postazione: " + e.getMessage());
		}
	}


	private void aggiungiEdificio(Scanner scanner) {
		System.out.println("\n--- Aggiungi Nuovo Edificio ---");
		System.out.print("Nome Edificio: ");
		String nome = scanner.nextLine();
		System.out.print("Indirizzo: ");
		String indirizzo = scanner.nextLine();
		System.out.print("Città: ");
		String citta = scanner.nextLine();

		Edificio edificio = new Edificio();
		edificio.setNome(nome);
		edificio.setIndirizzo(indirizzo);
		edificio.setCitta(citta);

		edificioRepository.save(edificio);
		System.out.println("Edificio aggiunto con successo!");
	}

	private void effettuaPrenotazione(Scanner scanner) {
		System.out.println("\n--- Effettua prenotazione ---");
		try {
			System.out.print("Inserisci Username: ");
			String username = scanner.nextLine().trim();
			if (username.isEmpty()) {
				System.out.println("Errore: Lo username non può essere vuoto.");
				return;
			}

			System.out.print("Inserisci ID Postazione: ");
			Long postazioneId;
			try {
				postazioneId = Long.parseLong(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Errore: Inserisci un ID valido per la postazione.");
				return;
			}

			System.out.print("Inserisci Data Prenotazione (AAAA-MM-GG): ");
			LocalDate dataPrenotazione;
			try {
				dataPrenotazione = LocalDate.parse(scanner.nextLine().trim());

				if (dataPrenotazione.isBefore(LocalDate.now())) {
					System.out.println("Errore: Questa data è già passata.");
					return;
				}
			} catch (Exception e) {
				System.out.println("Errore: Inserisci una data valida nel formato AAAA-MM-GG.");
				return;
			}


			String risultato = prenotazioneService.effettuaPrenotazione(postazioneId, username, dataPrenotazione);
			System.out.println(risultato);
		} catch (Exception e) {
			System.out.println("Errore durante l'effettuazione della prenotazione: " + e.getMessage());
		}
	}

	private void visualizzaPrenotazioni() {
		System.out.println("\n--- Visualizza Tutte le Prenotazioni ---");
		prenotazioneService.visualizzaTutteLePrenotazioni()
				.forEach(p -> System.out.println("Prenotazione ID: " + p.getId()
						+ ", Utente: " + p.getUtente().getUsername()
						+ ", Postazione: " + p.getPostazione().getDescrizione()
						+ ", Data: " + p.getDataPrenotazione()));
	}

	private void visualizzaEdifici() {
		System.out.println("\n--- Elenco Edifici ---");
		edificioRepository.findAll()
				.forEach(e -> System.out.println("ID: " + e.getId() + ", Nome: " + e.getNome()
						+ ", Indirizzo: " + e.getIndirizzo() + ", Città: " + e.getCitta()));
	}
	private void cercaPostazioniPerTipoECitta(Scanner scanner) {
		System.out.println("\n--- Ricerca Postazioni per Tipo e Città ---");

		TipoPostazione tipoPostazione = null;
		boolean tipoValido = false;

		while (!tipoValido) {
			System.out.print("Inserisci il tipo di postazione desiderata (OPENSPACE/PRIVATO/SALA_RIUNIONI): ");
			String tipoInput = scanner.nextLine().toUpperCase();
			try {
				tipoPostazione = TipoPostazione.valueOf(tipoInput);
				tipoValido = true;
			} catch (IllegalArgumentException e) {
				System.out.println("Errore: Tipo non valido. Inserisci uno tra OPENSPACE, PRIVATO o SALA_RIUNIONI.");
			}
		}

		System.out.print("Inserisci la città di interesse: ");
		String citta = scanner.nextLine();

		List<Postazione> postazioni = postazioneRepository.findByTipoAndEdificio_Citta(tipoPostazione, citta);

		if (postazioni.isEmpty()) {
			System.out.println("Nessuna postazione trovata per il tipo '" + tipoPostazione + "' e la città '" + citta + "'.");
		} else {
			System.out.println("Postazioni trovate:");
			postazioni.forEach(p -> System.out.println("ID: " + p.getId()
					+ ", Descrizione: " + p.getDescrizione()
					+ ", Edificio: " + p.getEdificio().getNome()
					+ ", Città: " + p.getEdificio().getCitta()));
		}
	}

}
