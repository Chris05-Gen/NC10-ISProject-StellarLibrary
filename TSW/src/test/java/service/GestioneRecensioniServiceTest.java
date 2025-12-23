package service;

import dao.RecensioneDAO;
import model.Libro;
import model.Recensione;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestioneRecensioniServiceTest {

    @Mock
    private RecensioneDAO recensioneDAO;

    @InjectMocks
    private GestioneRecensioniService service;

    private Utente utente;

    @BeforeEach
    void setup() {
        utente = new Utente();
        utente.setId(1);
        utente.setNome("Mario");
        utente.setCognome("Rossi");
    }

    /* ======================================================
       TEST VALIDA RECENSIONE (Test Frame CT / CX / CR)
       ====================================================== */

    @Test
    void validaRecensione_fallisce_seTitoloMancante() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaRecensione(
                        "ISBN123",
                        "",
                        "Testo valido",
                        "5"
                )
        );

        assertEquals("Titolo recensione obbligatorio.", ex.getMessage());
    }

    @Test
    void validaRecensione_fallisce_seTestoMancante() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaRecensione(
                        "ISBN123",
                        "Titolo",
                        "",
                        "5"
                )
        );

        assertEquals("Il testo della recensione è obbligatorio.", ex.getMessage());
    }

    @Test
    void validaRecensione_fallisce_seRatingMancante() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaRecensione(
                        "ISBN123",
                        "Titolo",
                        "Testo",
                        null
                )
        );

        assertEquals("Valutazione mancante.", ex.getMessage());
    }

    @Test
    void validaRecensione_fallisce_seRatingFuoriRange() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaRecensione(
                        "ISBN123",
                        "Titolo",
                        "Testo",
                        "10"
                )
        );

        assertEquals("Valutazione fuori range (1-5).", ex.getMessage());
    }

    @Test
    void validaRecensione_ok_seInputValidi() {
        assertDoesNotThrow(() ->
                service.validaRecensione(
                        "ISBN123",
                        "Titolo",
                        "Testo valido",
                        "5"
                )
        );
    }

    /* ======================================================
       TEST AGGIUNGI RECENSIONE
       ====================================================== */

    @Test
    void aggiungiRecensione_fallisce_seUtenteNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.aggiungiRecensione(
                        null,
                        "ISBN123",
                        "Titolo",
                        "Testo",
                        5
                )
        );

        assertEquals("Utente non autenticato.", ex.getMessage());
        verifyNoInteractions(recensioneDAO);
    }

    @Test
    void aggiungiRecensione_fallisce_seDaoLanciaEccezione() throws SQLException {
        doThrow(new SQLException("DB error"))
                .when(recensioneDAO)
                .addRecensione(any(Recensione.class));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.aggiungiRecensione(
                        utente,
                        "ISBN123",
                        "Titolo",
                        "Testo",
                        4
                )
        );

        assertTrue(ex.getMessage().contains("Errore durante il salvataggio"));
    }

    @Test
    void aggiungiRecensione_ok_seDatiValidi() throws SQLException {
        doNothing().when(recensioneDAO).addRecensione(any(Recensione.class));

        assertDoesNotThrow(() ->
                service.aggiungiRecensione(
                        utente,
                        "ISBN123",
                        "Libro fantastico",
                        "Consigliatissimo",
                        5
                )
        );

        verify(recensioneDAO, times(1))
                .addRecensione(any(Recensione.class));
    }
}
