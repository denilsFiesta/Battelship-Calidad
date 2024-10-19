package com.modelTest.shipstest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.model.ships.Cruiser;

public class CruiserTest {

    private Cruiser cruiser;

    @BeforeEach
    public void setUp() {
        cruiser = spy(new Cruiser());
    }

    @Test
    public void testGetLength() {
        assertEquals(3, cruiser.getLength(), "El largo del Cruiser debería ser 3");
    }

    @Test
    public void testRestoreHealthInRecoveryMode() {
        cruiser.restoreHealthInRecoveryMode();
        verify(cruiser).restoreHealthInRecoveryMode();
        assertEquals(3, cruiser.getLength(), "La salud debería ser igual a MAX_HEALTH después de la restauración.");
    }

    @Test
    public void testToString() {
        assertEquals("Cruiser", cruiser.toString(), "El nombre del barco debería ser 'Cruiser'");
    }
}
