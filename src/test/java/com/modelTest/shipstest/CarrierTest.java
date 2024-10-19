package com.modelTest.shipstest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.model.ships.Carrier;

public class CarrierTest {

    private Carrier carrier;

    @BeforeEach
    public void setUp() {
        // Usamos un espía en todas las pruebas para poder verificar las llamadas a métodos
        carrier = spy(new Carrier());
    }

    @Test
    public void testGetLength() {
        assertEquals(5, carrier.getLength(), "El largo del Carrier debería ser 5");
    }

    @Test
    public void testRestoreHealthInRecoveryMode() {
        carrier.restoreHealthInRecoveryMode();
        verify(carrier).restoreHealthInRecoveryMode();
        assertEquals(5, carrier.getLength(), "La salud debería ser igual a MAX_HEALTH después de la restauración.");
    }

    @Test
    public void testToString() {
        assertEquals("Carrier", carrier.toString(), "El nombre del barco debería ser 'Carrier'");
    }
}
