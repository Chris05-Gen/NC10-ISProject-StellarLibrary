package service;

import dao.GenereDAO;
import dao.LibroDAO;
import model.Libro;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestioneCatalogoServiceTest {

    @Mock
    private LibroDAO libroDAO;

    @Mock
    private GenereDAO genereDAO;

    @InjectMocks
    private GestioneCatalogoService service;

    // =====================================================
    // VALIDAZIONE FILTRI
    // =====================================================

    /**
     * TC_GCR_2_1 – Prezzo minimo negativo
     */
    @Test
    void TC_GCR_2_1_prezzoMinimoNegativo() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        null,
                        "-10",
                        "20",
                        null)
        );

        assertEquals("Il prezzo minimo non può essere negativo.", ex.getMessage());
    }

    /**
     * TC_GCR_2_2 – Anno di pubblicazione non valido
     */
    @Test
    void TC_GCR_2_2_annoNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        "3000",
                        null,
                        null,
                        null)
        );

        assertEquals("Anno non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_3 – Numero di pagine non valido
     */
    @Test
    void TC_GCR_2_3_numeroPagineNonValido(){
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        null,
                        null,
                        null,
                        "-50")
        );

        assertEquals("Numero di pagine non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_4 – Genere non valido
     */
    @Test
    void TC_GCR_2_4_genereNonValido(){
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        "-1",
                        null,
                        null,
                        null,
                        null)
        );

        assertEquals("Genere non valido.", ex.getMessage());
    }

    // =====================================================
    // TC_GCR_2_5 – Nessun filtro applicato
    // =====================================================
    @Test
    void TC_GCR_2_5_nessunFiltro() throws Exception{
        List<Libro> mockRisultato = List.of();

        when(libroDAO.cercaConFiltri(null, null, null, null, null))
                .thenReturn(mockRisultato);

        List<Libro> risultato = service.cercaLibriConFiltri(
                null, null, null, null, null);

        assertNotNull(risultato);
        assertSame(mockRisultato, risultato);

        verify(libroDAO, times(1))
                .cercaConFiltri(null, null, null, null, null);
        verifyNoMoreInteractions(libroDAO);
    }

    // =====================================================
    // TC_GCR_2_6 – Filtro per genere
    // =====================================================
    @Test
    void TC_GCR_2_6_filtroGenere() throws Exception{
        when(libroDAO.cercaConFiltri(1, null, null, null, null))
                .thenReturn(List.of());

        List<Libro> risultato = service.cercaLibriConFiltri(
                1, null, null, null, null);

        assertNotNull(risultato);

        verify(libroDAO)
                .cercaConFiltri(1, null, null, null, null);
    }

    // =====================================================
    // TC_GCR_2_7 – Filtro per prezzo
    // =====================================================
    @Test
    void TC_GCR_2_7_filtroPrezzo() throws Exception{
        BigDecimal min = BigDecimal.valueOf(10);
        BigDecimal max = BigDecimal.valueOf(30);

        when(libroDAO.cercaConFiltri(null, null, min, max, null))
                .thenReturn(List.of());

        List<Libro> risultato = service.cercaLibriConFiltri(
                null, null, min, max, null);

        assertNotNull(risultato);

        verify(libroDAO)
                .cercaConFiltri(null, null, min, max, null);
    }

    // =====================================================
    // TC_GCR_2_8 – Filtro per anno
    // =====================================================
    @Test
    void TC_GCR_2_8_filtroAnno() throws Exception{
        when(libroDAO.cercaConFiltri(null, 2018, null, null, null))
                .thenReturn(List.of());

        List<Libro> risultato = service.cercaLibriConFiltri(
                null, 2018, null, null, null);

        assertNotNull(risultato);

        verify(libroDAO)
                .cercaConFiltri(null, 2018, null, null, null);
    }

    // =====================================================
    // TC_GCR_2_9 – Filtro per pagine
    // =====================================================
    @Test
    void TC_GCR_2_9_filtroPagine() throws Exception{
        when(libroDAO.cercaConFiltri(null, null, null, null, 350))
                .thenReturn(List.of());

        List<Libro> risultato = service.cercaLibriConFiltri(
                null, null, null, null, 350);

        assertNotNull(risultato);

        verify(libroDAO)
                .cercaConFiltri(null, null, null, null, 350);
    }

    // =====================================================
    // TC_GCR_2_10 – Combinazione completa di filtri
    // =====================================================
    @Test
    void TC_GCR_2_10_filtriCombinati() throws Exception{
        BigDecimal min = BigDecimal.valueOf(10);
        BigDecimal max = BigDecimal.valueOf(40);

        when(libroDAO.cercaConFiltri(2, 2020, min, max, 300))
                .thenReturn(List.of());

        List<Libro> risultato = service.cercaLibriConFiltri(
                2, 2020, min, max, 300);

        assertNotNull(risultato);

        verify(libroDAO)
                .cercaConFiltri(2, 2020, min, max, 300);
    }

    /**
     * TC_GCR_2_11 - Prezzo massimo negativo
     * Copre la linea: if (filtri.prezzoMax.compareTo(BigDecimal.ZERO) < 0)
     */
    @Test
    void TC_GCR_2_11_prezzoMassimoNegativo() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        null,
                        "10",
                        "-20",  // Prezzo massimo negativo
                        null)
        );

        assertEquals("Il prezzo massimo non può essere negativo.", ex.getMessage());
    }

    /**
     * TC_GCR_2_12 - Prezzo minimo > Prezzo massimo
     * Copre la linea: if (filtri.prezzoMin.compareTo(filtri.prezzoMax) > 0)
     */
    @Test
    void TC_GCR_2_12_prezzoMinimoMaggioreDiMassimo() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        null,
                        "50",   // Min maggiore di Max
                        "20",
                        null)
        );

        assertEquals("Il prezzo minimo non può essere maggiore del massimo.", ex.getMessage());
    }

    /**
     * TC_GCR_2_13 - Formato prezzo minimo non valido
     * Copre la linea del catch (NumberFormatException) per prezzoMin
     */
    @Test
    void TC_GCR_2_13_formatoPrezzoMinimoNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        null,
                        "abc",  // Formato non valido
                        "20",
                        null)
        );

        assertEquals("Prezzo minimo non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_14 - Formato prezzo massimo non valido
     * Copre la linea del catch (NumberFormatException) per prezzoMax
     */
    @Test
    void TC_GCR_2_14_formattoPrezzoMassimoNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        null,
                        "10",
                        "xyz",  // Formato non valido
                        null)
        );

        assertEquals("Prezzo massimo non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_15 - Formato anno non valido
     * Copre la linea del catch (NumberFormatException) per anno
     */
    @Test
    void TC_GCR_2_15_formatoAnnoNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        "duemila",  // Formato non valido
                        null,
                        null,
                        null)
        );

        assertEquals("Anno non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_16 - Anno < 1000
     * Copre la linea: if (filtri.anno < 1000 || filtri.anno > currentYear)
     */
    @Test
    void TC_GCR_2_16_annoTroppoAntico() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        "999",  // Anno < 1000
                        null,
                        null,
                        null)
        );

        assertEquals("Anno non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_17 - Formato numero pagine non valido
     * Copre la linea del catch (NumberFormatException) per minPagine
     */
    @Test
    void TC_GCR_2_17_formatoPagineNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        null,
                        null,
                        null,
                        null,
                        "trecento")  // Formato non valido
        );

        assertEquals("Numero di pagine non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_18 - Formato genere non valido
     * Copre la linea del catch (NumberFormatException) per idGenere
     */
    @Test
    void TC_GCR_2_18_formatoGenereNonValido() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        "abc",  // Formato non valido
                        null,
                        null,
                        null,
                        null)
        );

        assertEquals("Genere non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_19 - Genere = 0 (non valido)
     * Copre la linea: if (filtri.idGenere <= 0)
     */
    @Test
    void TC_GCR_2_19_genereZero() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.validaEPreparaFiltri(
                        "0",  // Genere = 0
                        null,
                        null,
                        null,
                        null)
        );

        assertEquals("Genere non valido.", ex.getMessage());
    }

    /**
     * TC_GCR_2_20 - Eccezione durante la ricerca nel DAO
     * Copre la linea del catch (Exception) nel metodo cercaLibriConFiltri
     */
    @Test
    void TC_GCR_2_20_erroreDAO() throws Exception {
        when(libroDAO.cercaConFiltri(null, null, null, null, null))
                .thenThrow(new RuntimeException("Errore database"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.cercaLibriConFiltri(null, null, null, null, null)
        );

        assertEquals("Errore nella ricerca dei libri con filtri", ex.getMessage());
        verify(libroDAO, times(1))
                .cercaConFiltri(null, null, null, null, null);
    }

    /**
     * TC_GCR_2_21 - Solo prezzo minimo fornito (senza massimo)
     * Test per copertura di branch quando solo prezzoMin è presente
     */
    @Test
    void TC_GCR_2_21_soloPrezzoMinimo() throws Exception {
        BigDecimal min = BigDecimal.valueOf(15);

        when(libroDAO.cercaConFiltri(null, null, min, null, null))
                .thenReturn(List.of());

        List<Libro> risultato = service.cercaLibriConFiltri(
                null, null, min, null, null);

        assertNotNull(risultato);
        verify(libroDAO).cercaConFiltri(null, null, min, null, null);
    }

    /**
     * TC_GCR_2_22 - Solo prezzo massimo fornito (senza minimo)
     * Test per copertura di branch quando solo prezzoMax è presente
     */
    @Test
    void TC_GCR_2_22_soloPrezzoMassimo() throws Exception {
        BigDecimal max = BigDecimal.valueOf(25);

        when(libroDAO.cercaConFiltri(null, null, null, max, null))
                .thenReturn(List.of());

        List<Libro> risultato = service.cercaLibriConFiltri(
                null, null, null, max, null);

        assertNotNull(risultato);
        verify(libroDAO).cercaConFiltri(null, null, null, max, null);
    }
}
