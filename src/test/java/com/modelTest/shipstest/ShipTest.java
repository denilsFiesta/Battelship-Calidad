package com.modelTest.shipstest;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import com.model.game.FiringMode;
import com.model.game.HealthReport;
import com.model.ships.Battleship;
import com.model.ships.Carrier;
import com.model.ships.Cruiser;
import com.model.ships.Destroyer;
import com.model.ships.Ship;
import com.model.ships.Submarine;

public class ShipTest {

    private Ship ship;
    private HealthReport mockHealthReport;  // Mock de HealthReport

    @BeforeEach
    public void setUp() {
        ship = new Carrier();

        // Mockeamos la clase HealthReport 
        mockHealthReport = mock(HealthReport.class);
    }

    @Test
    public void testHitTheShipWithTorpedoFiringMode() throws Exception {
        // Usamos reflexión para modificar la variable 'health' protegida
        Field healthField = Ship.class.getDeclaredField("health");
        healthField.setAccessible(true);  // Hacemos accesible la variable protegida

        healthField.set(ship, 5);
        ship.hitTheShip(FiringMode.TORPEDO_FIRING_MODE);
        int health = (int) healthField.get(ship);
        assertEquals(0, health, "La salud del barco debería ser 0 después de un ataque con torpedo");

        assertNotNull(mockHealthReport);
    }

    @Test
    public void testHitTheShipWithGeneralFiringMode() throws Exception {
        Field healthField = Ship.class.getDeclaredField("health");
        healthField.setAccessible(true);  // Hacemos accesible la variable protegida
        healthField.set(ship, 3);
        ship.hitTheShip(FiringMode.GENERAL_FIRING_MODE);
        int health = (int) healthField.get(ship);
        assertEquals(2, health, "La salud del barco debería haberse reducido en 1 tras el ataque");

        assertNotNull(mockHealthReport);
    }

    @Test
    public void testHitTheShipWhenAlreadySunk() throws Exception {
        Field healthField = Ship.class.getDeclaredField("health");
        healthField.set(ship, 0);
        ship.hitTheShip(FiringMode.GENERAL_FIRING_MODE);
        int health = (int) healthField.get(ship);
        assertEquals(0, health, "La salud del barco debería permanecer en 0 tras estar hundido");

        assertNotNull(mockHealthReport);
    }

    
    @Test
    public void testConvertInputIntegersToShips_CreatesCarrier() {
        int[] counters = {1, 0, 0, 0, 0};  
        List<Ship> ships = Ship.convertInputIntegersToShips(counters);

       
        assertEquals(1, ships.size(), "Debería haberse creado 1 barco");

        
        assertTrue(ships.get(0) instanceof Carrier, "El barco debería ser un Carrier");
    }

    @Test
    public void testConvertInputIntegersToShips_CreatesBattleship() {
        int[] counters = {0, 1, 0, 0, 0};  
        List<Ship> ships = Ship.convertInputIntegersToShips(counters);

     
        assertEquals(1, ships.size(), "Debería haberse creado 1 barco");

        
        assertTrue(ships.get(0) instanceof Battleship, "El barco debería ser un Battleship");
    }

    @Test
    public void testConvertInputIntegersToShips_CreatesCruiser() {
        int[] counters = {0, 0, 1, 0, 0}; 
        List<Ship> ships = Ship.convertInputIntegersToShips(counters);
        assertEquals(1, ships.size(), "Debería haberse creado 1 barco");
        assertTrue(ships.get(0) instanceof Cruiser, "El barco debería ser un Cruiser");
    }

    @Test
    public void testConvertInputIntegersToShips_CreatesDestroyer() {
        int[] counters = {0, 0, 0, 1, 0};  
        List<Ship> ships = Ship.convertInputIntegersToShips(counters);

        assertEquals(1, ships.size(), "Debería haberse creado 1 barco");
        assertTrue(ships.get(0) instanceof Destroyer, "El barco debería ser un Destroyer");
    }

    @Test
    public void testConvertInputIntegersToShips_CreatesSubmarine() {
        int[] counters = {0, 0, 0, 0, 1};  
        List<Ship> ships = Ship.convertInputIntegersToShips(counters);
        assertEquals(1, ships.size(), "Debería haberse creado 1 barco");
        assertTrue(ships.get(0) instanceof Submarine, "El barco debería ser un Submarine");
    }

    @Test
    public void testConvertInputIntegersToShips_DefaultCase() {
        int[] counters = {0, 0, 0, 0, 0, 2};  // El índice 5 

        List<Ship> ships = Ship.convertInputIntegersToShips(counters);
        assertEquals(0, ships.size(), "No debería haberse creado ningún barco para índices fuera de rango");
    }
}

