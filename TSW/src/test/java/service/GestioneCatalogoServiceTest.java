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
import java.util.Collections;
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
    void TC_GCR_2_1_prezzoMinimoNegativo() throws Exception{
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
    void TC_GCR_2_2_annoNonValido() throws Exception{
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
    void TC_GCR_2_3_numeroPagineNonValido() throws Exception{
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
    void TC_GCR_2_4_genereNonValido() throws Exception{
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
}
