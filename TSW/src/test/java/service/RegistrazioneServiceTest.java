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

    /**
     * TC_GA_1 – Nome non valido
     */
    @Test
    void TC_GA_1_nomeNonValido() throws Exception{
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "", "Rossi", "mario.rossi@email.it", "Password123")
        );

        assertEquals("Il nome è obbligatorio.", ex.getMessage());
    }

    /**
     * TC_GA_2 – Cognome non valido
     */
    @Test
    void TC_GA_2_cognomeNonValido() throws Exception{
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario", "   ", "mario.rossi@email.it", "Password123")
        );

        assertEquals("Il cognome è obbligatorio.", ex.getMessage());
    }

    /**
     * TC_GA_3 – Email non valida
     */
    @Test
    void TC_GA_3_emailNonValida() throws Exception{
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario", "Rossi", "emailNonValida", "Password123")
        );

        assertEquals("Email non valida.", ex.getMessage());
    }

    /**
     * TC_GA_4 – Password troppo corta
     */
    @Test
    void TC_GA_4_passwordTroppoCorta() throws Exception{
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> registrazioneService.validaDatiRegistrazione(
                        "Mario", "Rossi", "mario.rossi@email.it", "123")
        );

        assertEquals("La password deve essere di almeno 6 caratteri.", ex.getMessage());
    }

    /**
     * TC_GA_5 – Email già esistente
     */
    @Test
    void TC_GA_5_emailGiaEsistente() throws Exception{
        when(utenteDAO.emailEsistente("mario.rossi@email.it"))
                .thenReturn(true);

        boolean risultato = registrazioneService.emailEsistente("mario.rossi@email.it");

        assertTrue(risultato);
        verify(utenteDAO, times(1))
                .emailEsistente("mario.rossi@email.it");
    }

    /**
     * TC_GA_6 – Registrazione corretta
     */
    @Test
    void TC_GA_6_registrazioneCorretta() throws Exception{
        when(utenteDAO.registraUtente(any(Utente.class)))
                .thenReturn(true);

        assertDoesNotThrow(() ->
                registrazioneService.registraUtente(utenteValido)
        );

        verify(utenteDAO, times(1))
                .registraUtente(utenteValido);
    }
}
