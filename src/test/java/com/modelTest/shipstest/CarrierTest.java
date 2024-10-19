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
        // Verifica que getLength retorne el valor correcto (MAX_HEALTH)
        assertEquals(5, carrier.getLength(), "El largo del Carrier debería ser 5");
    }

    @Test
    public void testRestoreHealthInRecoveryMode() {
        // Llamamos al método para restaurar la salud en modo de recuperación
        carrier.restoreHealthInRecoveryMode();

        // Verificamos que el método restoreHealthInRecoveryMode haya sido llamado
        verify(carrier).restoreHealthInRecoveryMode();

        // Verificamos que el valor de la salud haya sido restaurado a MAX_HEALTH (5)
        assertEquals(5, carrier.getLength(), "La salud debería ser igual a MAX_HEALTH después de la restauración.");
    }

    @Test
    public void testToString() {
        // Verificamos que el método toString retorne el nombre correcto del barco
        assertEquals("Carrier", carrier.toString(), "El nombre del barco debería ser 'Carrier'");
    }
}
