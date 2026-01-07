package service;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestioneOrdiniServiceTest {

    @Mock
    private OrdineDAO ordineDAO;

    @Mock
    private MetodoPagamentoDAO metodoPagamentoDAO;

    @Mock
    private IndirizzoDAO indirizzoDAO;

    @Mock
    private CarrelloDAO carrelloDAO;

    @Mock
    private ContieneDAO contieneDAO;

    @Mock
    private LibroDAO libroDAO;

    @InjectMocks
    private GestioneOrdiniService service;

    private final int ID_UTENTE = 1;
    private final int ID_CARRELLO = 10;
    private final int ID_INDIRIZZO = 5;
    private final int ID_METODO = 3;

    private Carrello carrello;
    private Contiene contiene;

    @BeforeEach
    void setUp() {
        carrello = new Carrello();
        carrello.setId(ID_CARRELLO);

        Libro libro = new Libro();
        libro.setIsbn("ISBN123");
        libro.setPrezzo(BigDecimal.valueOf(20));

        contiene = new Contiene();
        contiene.setLibro(libro);
        contiene.setQuantita(2);
    }

    /**
     * TC_GCC7_1 – Carrello vuoto
     */
    @Test
    void TC_GCC7_1_carrelloVuoto() throws Exception{
        when(indirizzoDAO.AppartieneA(ID_INDIRIZZO, ID_UTENTE)).thenReturn(true);
        when(metodoPagamentoDAO.Esiste(ID_METODO)).thenReturn(true);
        when(carrelloDAO.findByUtenteId(ID_UTENTE)).thenReturn(carrello);
        when(contieneDAO.getContenuto(ID_CARRELLO)).thenReturn(Collections.emptyList());

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.creaOrdineCompleto(
                        ID_UTENTE,
                        String.valueOf(ID_INDIRIZZO),
                        String.valueOf(ID_METODO))
        );

        assertEquals("Carrello vuoto.", ex.getMessage());
    }

    /**
     * TC_GCC7_2 – Indirizzo non valido / non appartenente
     */
    @Test
    void TC_GCC7_2_indirizzoNonValido() throws Exception{
        when(indirizzoDAO.AppartieneA(ID_INDIRIZZO, ID_UTENTE))
                .thenReturn(false);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.creaOrdineCompleto(
                        ID_UTENTE,
                        String.valueOf(ID_INDIRIZZO),
                        String.valueOf(ID_METODO))
        );

        assertEquals("Indirizzo non valido.", ex.getMessage());
    }

    /**
     * TC_GCC7_3 – Metodo di pagamento non valido
     */
    @Test
    void TC_GCC7_3_metodoPagamentoNonValido() throws Exception{

        when(indirizzoDAO.AppartieneA(ID_INDIRIZZO, ID_UTENTE)).thenReturn(true);
        when(metodoPagamentoDAO.Esiste(ID_METODO)).thenReturn(false);


        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.creaOrdineCompleto(
                        ID_UTENTE,
                        String.valueOf(ID_INDIRIZZO),
                        String.valueOf(ID_METODO))
        );

        assertEquals("Metodo di pagamento non valido.", ex.getMessage());
    }


    /**
     * TC_GCC7_4 – Parametri non numerici
     */
    @Test
    void TC_GCC7_4_parametriNonNumerici() throws Exception{
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.creaOrdineCompleto(
                        ID_UTENTE,
                        "abc",
                        "xyz")
        );

        assertEquals("Valori non numerici.", ex.getMessage());
    }

    /**
     * TC_GCC7_5 – Conferma ordine corretta
     */
    @Test
    void TC_GCC7_5_confermaOrdineCorretta() throws Exception{
        when(indirizzoDAO.AppartieneA(ID_INDIRIZZO, ID_UTENTE)).thenReturn(true);
        when(metodoPagamentoDAO.Esiste(ID_METODO)).thenReturn(true);
        when(carrelloDAO.findByUtenteId(ID_UTENTE)).thenReturn(carrello);
        when(contieneDAO.getContenuto(ID_CARRELLO)).thenReturn(List.of(contiene));
        when(ordineDAO.creaOrdine(any(Ordine.class))).thenReturn(100);

        int idOrdine = service.creaOrdineCompleto(
                ID_UTENTE,
                String.valueOf(ID_INDIRIZZO),
                String.valueOf(ID_METODO)
        );

        assertEquals(100, idOrdine);

        verify(ordineDAO, times(1)).creaOrdine(any(Ordine.class));
        verify(contieneDAO, times(1)).svuotaCarrello(ID_CARRELLO);
    }
}
