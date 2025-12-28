package service;

import dao.UtenteDAO;
import model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrazioneServiceTest {

    @Mock
    private UtenteDAO utenteDAO;

    @InjectMocks
    private RegistrazioneService registrazioneService;

    private Utente utenteValido;

    @BeforeEach
    void setUp() {
        utenteValido = new Utente();
        utenteValido.setNome("Mario");
        utenteValido.setCognome("Rossi");
        utenteValido.setEmail("mario.rossi@email.it");
        utenteValido.setPassword("Password123");
    }

    // =====================================================
    // 1.2.1 Registrazione — TC_GA_1 ... TC_GA_12
    // =====================================================

    /**
     * TC_GA_1 – Utente null (Test Frame VU1)
     */
    @Test
    void TC_GA_1_utenteNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.registraUtente(null)
        );

        assertEquals("Utente nullo.", ex.getMessage());
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_2 – Nome vuoto o null (Test Frame VU2, CNO1)
     */
    @Test
    void TC_GA_2_nomeVuoto() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "   ",
                        "Rossi",
                        "mario.rossi@email.it",
                        "Password123"
                )
        );

        assertTrue(ex.getMessage().toLowerCase().contains("nome"));
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_3 – Nome non rispetta formato (Test Frame VU2, CNO2, FNO1)
     * Esempio input: "123@"
     */
    @Test
    void TC_GA_3_nomeFormatoNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "123@",
                        "Rossi",
                        "mario.rossi@email.it",
                        "Password123"
                )
        );

        assertTrue(ex.getMessage().toLowerCase().contains("nome"));
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_4 – Cognome vuoto o null (Test Frame VU2, CNO2, FNO2, CCO1)
     */
    @Test
    void TC_GA_4_cognomeVuoto() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario",
                        "",
                        "mario.rossi@email.it",
                        "Password123"
                )
        );

        assertTrue(ex.getMessage().toLowerCase().contains("cognome"));
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_5 – Cognome non rispetta formato (Test Frame VU2, CNO2, FNO2, CCO2, FCO1)
     * Esempio input: "123@"
     */
    @Test
    void TC_GA_5_cognomeFormatoNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario",
                        "123@",
                        "mario.rossi@email.it",
                        "Password123"
                )
        );

        assertTrue(ex.getMessage().toLowerCase().contains("cognome"));
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_6 – Email vuota o null (Test Frame ... CE1)
     */
    @Test
    void TC_GA_6_emailVuota() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario",
                        "Rossi",
                        "",
                        "Password123"
                )
        );

        assertTrue(ex.getMessage().toLowerCase().contains("email"));
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_7 – Email non rispetta formato (Test Frame ... FE1)
     * Esempio input: "mario.rossi@"
     */
    @Test
    void TC_GA_7_emailFormatoNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario",
                        "Rossi",
                        "mario.rossi@",
                        "Password123"
                )
        );

        assertTrue(ex.getMessage().toLowerCase().contains("email"));
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_8 – Email già registrata (Test Frame ... EE1)
     */
    @Test
    void TC_GA_8_emailGiaRegistrata() throws Exception{
        when(utenteDAO.emailEsistente("mario.rossi@email.it"))
                .thenReturn(true);

        assertTrue(registrazioneService.emailEsistente("mario.rossi@email.it"));

        verify(utenteDAO, times(1))
                .emailEsistente("mario.rossi@email.it");
    }

    /**
     * TC_GA_9 – Password vuota o null (Test Frame ... CP1)
     */
    @Test
    void TC_GA_9_passwordVuota() throws Exception{
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario",
                        "Rossi",
                        "mario.rossi@email.it",
                        " "
                )
        );

        assertTrue(ex.getMessage().toLowerCase().contains("password"));
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_10 – Password troppo corta (Test Frame ... LP1)
     * Esempio input: "Pass1"
     */
    @Test
    void TC_GA_10_passwordTroppoCorta() throws Exception{
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario",
                        "Rossi",
                        "mario.rossi@email.it",
                        "Pass1"
                )
        );

        assertTrue(ex.getMessage().toLowerCase().contains("password"));
        verifyNoInteractions(utenteDAO);
    }

    /**
     * TC_GA_11 – Errore DB durante salvataggio (Test Frame ... ED1)
     */
    @Test
    void TC_GA_11_erroreDatabaseDuranteSalvataggio() throws Exception{
        // Mock: il salvataggio lancia eccezione
        doThrow(new RuntimeException("Errore nella registrazione utente"))
                .when(utenteDAO)
                .registraUtente(any(Utente.class));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> registrazioneService.registraUtente(utenteValido)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("errore"));
        verify(utenteDAO, times(1)).registraUtente(any(Utente.class));
    }

    /**
     * TC_GA_12 – Registrazione corretta (Test Frame ... ED2)
     */
    @Test
    void TC_GA_12_registrazioneCorretta() throws Exception{
        // Test validazione (deve passare)
        assertDoesNotThrow(() ->
                registrazioneService.validaDatiRegistrazione(
                        "Mario",
                        "Rossi",
                        "nuovo.utente@email.it",
                        "Password123"
                )
        );

        // Test registrazione (deve passare)
        assertDoesNotThrow(() ->
                registrazioneService.registraUtente(utenteValido)
        );

        verify(utenteDAO, times(1)).registraUtente(utenteValido);
    }
}