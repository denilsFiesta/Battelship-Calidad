package com.modelTest.gameTest.userTest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.model.game.AttackReport;
import com.model.game.FiringMode;
import com.model.game.Game;
import com.model.game.ocean.Point;
import com.model.game.user.User;
import com.model.ships.Ship;

public class UserTest {

    private User user;
    private Game mockGame;
    private AttackReport mockAttackReport;
    private Ship mockShip;
    private Ship differentMockShip;

    @BeforeEach
    public void setUp() {
        user = new User();
        mockGame = mock(Game.class); // Simulamos la sesión de juego
        mockAttackReport = mock(AttackReport.class); // Simulamos el reporte de ataque
        mockShip = mock(Ship.class); // Simulamos un barco
        differentMockShip = mock(Ship.class); 

        // Establecemos la sesión de juego actual en el mock
        user.setCurrentSession(mockGame);
    }

    // Método auxiliar para acceder al método privado 'lastShipIsEqualToCurrent'
    private boolean invokeLastShipIsEqualToCurrent(User user, Ship ship) throws Exception {
        // Accedemos al método privado usando reflexión
        Method method = User.class.getDeclaredMethod("lastShipIsEqualToCurrent", Ship.class);
        method.setAccessible(true); // Hacemos que el método sea accesible
        return (boolean) method.invoke(user, ship); // Ejecutamos el método y devolvemos el resultado
    }

    // Método auxiliar para invocar 'undoRecentActions' usando reflexión
    private void invokeUndoRecentActions(User user) throws Exception {
        Method method = User.class.getDeclaredMethod("undoRecentActions");
        method.setAccessible(true); 
        method.invoke(user);
    }

    //  Verificar que el contador de acciones aumenta 
    @Test
    public void testHitOnPlaceIncrementsHitOnPlace() {
        Point position = new Point(3, 5);
        FiringMode mode = FiringMode.GENERAL_FIRING_MODE;
        when(mockGame.hitOnPlace(position, mode)).thenReturn(mockAttackReport);
        AttackReport result = user.hitOnPlace(position, mode);
        assertEquals(1, user.getActionsCounter(), "El contador de acciones debería haber aumentado a 1");
        verify(mockGame, times(1)).hitOnPlace(position, mode);
        assertEquals(mockAttackReport, result, "Debería devolver el reporte de ataque simulado.");
    }

    //Verificar que lanza IllegalArgumentException cuando no hay torpedos
    @Test
    public void testHitOnPlaceThrowsIllegalArgumentException() {
        // Simulamos una posición y el modo de disparo torpedo
        Point position = new Point(3, 5);
        FiringMode mode = FiringMode.TORPEDO_FIRING_MODE;

        when(mockGame.hitOnPlace(position, mode)).thenThrow(new IllegalArgumentException("No hay torpedos disponibles."));

        // Verificamos que se lanza la excepción
        assertThrows(IllegalArgumentException.class, () -> user.hitOnPlace(position, mode), "Debería lanzar IllegalArgumentException si no hay torpedos.");
    }

    // ¿ Verificar que devuelve false cuando el barco es null
    @Test
    public void testLastShipIsEqualToCurrent() throws Exception {
        boolean result = invokeLastShipIsEqualToCurrent(user, null);
        assertFalse(result, "Debería devolver false cuando el barco es null.");
    }

    // Verificar que devuelve false cuando lastReports está vacío
    @Test
    public void testLastShipIsEqualToCurrent_EmptyLastReports() throws Exception {
        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        lastReportsField.set(user, new ArrayList<>()); // Establecemos una lista vacía

        boolean result = invokeLastShipIsEqualToCurrent(user, mockShip);
        assertFalse(result, "Debería devolver false cuando lastReports está vacío.");
    }

    //  Verificar que devuelve true cuando el barco es igual al último barco en lastReports
    @Test
    public void testLastShipIsEqualToCurrent_SameShip() throws Exception {
        // Creamos un reporte de ataque simulado
        AttackReport mockAttackReport = mock(AttackReport.class);

        // Usamos reflexión para acceder al campo privado 'lastReports'
        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> mockLastReports = new ArrayList<>();
        mockLastReports.add(mockAttackReport);

        when(mockAttackReport.getShip()).thenReturn(mockShip);
        lastReportsField.set(user, mockLastReports);

        boolean result = invokeLastShipIsEqualToCurrent(user, mockShip);
        assertTrue(result, "Debería devolver true cuando el barco es el mismo que el último en lastReports.");
    }

    // Verificar que devuelve false cuando el barco es diferente al último barco en lastReports
    @Test
    public void testLastShipIsEqualToCurrent_DifferentShip() throws Exception {
        AttackReport mockAttackReport = mock(AttackReport.class);

        // Usamos reflexión para acceder al campo privado 'lastReports'
        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> mockLastReports = new ArrayList<>();
        mockLastReports.add(mockAttackReport);

        // Simulamos que el último barco en lastReports es diferente al barco actual
        when(mockAttackReport.getShip()).thenReturn(differentMockShip);
        lastReportsField.set(user, mockLastReports);

        boolean result = invokeLastShipIsEqualToCurrent(user, mockShip);
        assertFalse(result, "Debería devolver false cuando el barco es diferente al último en lastReports.");
    }

    //  Verificar que 'undoRecentActions' remueve duplicados
    @Test
    public void testUndoRecentActions_RemovesDuplicates() throws Exception {
        // Simulamos reportes duplicados
        AttackReport duplicateAttackReport = mock(AttackReport.class);
        AttackReport uniqueAttackReport = mock(AttackReport.class);
        
        // Simulamos que cada reporte tiene un barco para evitar NullPointerException
        when(duplicateAttackReport.getShip()).thenReturn(mockShip);
        when(uniqueAttackReport.getShip()).thenReturn(mockShip);

        // Usamos reflexión para acceder al campo privado 'lastReports'
        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> mockLastReports = new ArrayList<>();
        mockLastReports.add(duplicateAttackReport);
        mockLastReports.add(duplicateAttackReport);
        mockLastReports.add(uniqueAttackReport);
        lastReportsField.set(user, mockLastReports);

        invokeUndoRecentActions(user);

        @SuppressWarnings("unchecked")
        List<AttackReport> lastReportsAfter = (List<AttackReport>) lastReportsField.get(user);
        assertEquals(2, lastReportsAfter.size(), "Deberían quedar solo dos reportes después de eliminar duplicados.");
    }

    

    // Verificar que 'undoRecentActions' no hace nada si lastReports está vacío
    @Test
    public void testUndoRecentActions_NoActionsIfLastReportsIsEmpty() throws Exception {
        // Usamos reflexión para acceder al campo privado 'lastReports' y establecerlo vacío
        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        lastReportsField.set(user, new ArrayList<>());
        invokeUndoRecentActions(user);

        verify(mockGame, times(0)).restorePreviousHealthFleet(anyInt());
        verify(mockShip, times(0)).restoreHealthInRecoveryMode();
        verify(mockGame, times(0)).updatePointsToRecover(null);
    }

    @Test
     public void testGetCurrentSession() {
     // Creamos un mock de la clase Game
        Game mockGame = mock(Game.class);

     // Usamos setCurrentSession para establecer el mock
     user.setCurrentSession(mockGame);
    assertEquals(mockGame, user.getCurrentSession(), "getCurrentSession debería devolver la sesión de juego actual.");
    }
}
