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
        destroyer = spy(new Destroyer());
    }

    @Test
    public void testGetLength() {
        assertEquals(2, destroyer.getLength(), "El largo del Destroyer debería ser 2");
    }

    @Test
    public void testRestoreHealthInRecoveryMode() {
        destroyer.restoreHealthInRecoveryMode();
        verify(destroyer).restoreHealthInRecoveryMode();
        assertEquals(2, destroyer.getLength(), "La salud debería ser igual a MAX_HEALTH después de la restauración.");
    }

    @Test
    public void testToString() {
        assertEquals("Destroyer", destroyer.toString(), "El nombre del barco debería ser 'Destroyer'");
    }
}
