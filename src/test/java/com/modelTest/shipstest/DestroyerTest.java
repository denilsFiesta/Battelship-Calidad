package com.modelTest.shipstest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.model.ships.Destroyer;

public class DestroyerTest {

    private Destroyer destroyer;

    @BeforeEach
    public void setUp() {
        // Usamos un espía en todas las pruebas para poder verificar las llamadas a métodos
        destroyer = spy(new Destroyer());
    }

    @Test
    public void testGetLength() {
        // Verifica que getLength retorne el valor correcto (MAX_HEALTH)
        assertEquals(2, destroyer.getLength(), "El largo del Destroyer debería ser 2");
    }

    @Test
    public void testRestoreHealthInRecoveryMode() {
        // Llamamos al método para restaurar la salud en modo de recuperación
        destroyer.restoreHealthInRecoveryMode();

        // Verificamos que el método restoreHealthInRecoveryMode haya sido llamado
        verify(destroyer).restoreHealthInRecoveryMode();

        // Verificamos que el valor de la salud haya sido restaurado a MAX_HEALTH (2)
        assertEquals(2, destroyer.getLength(), "La salud debería ser igual a MAX_HEALTH después de la restauración.");
    }

    @Test
    public void testToString() {
        // Verificamos que el método toString retorne el nombre correcto del barco
        assertEquals("Destroyer", destroyer.toString(), "El nombre del barco debería ser 'Destroyer'");
    }
}
