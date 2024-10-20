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
import com.model.game.user.UserState;
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
        Method method = User.class.getDeclaredMethod("lastShipIsEqualToCurrent", Ship.class);
        method.setAccessible(true);
        return (boolean) method.invoke(user, ship);
    }

    // Método auxiliar para invocar 'undoRecentActions' usando reflexión
    private void invokeUndoRecentActions(User user) throws Exception {
        Method method = User.class.getDeclaredMethod("undoRecentActions");
        method.setAccessible(true);
        method.invoke(user);
    }

    // Pruebas para onStatusChanged
    @Test
    public void testOnStatusChanged_MissWithLastReports() throws Exception {
        when(mockAttackReport.getResult()).thenReturn(AttackReport.HitResult.MISS);
        when(mockAttackReport.getShip()).thenReturn(mockShip);

        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> mockLastReports = new ArrayList<>();
        mockLastReports.add(mockAttackReport);
        lastReportsField.set(user, mockLastReports);

        user.onStatusChanged(mockAttackReport);

        verify(mockGame, times(1)).restorePreviousHealthFleet(anyInt());
        assertEquals(UserState.GENERAL, getUserState(user), "El estado debería ser GENERAL después de un MISS.");

        List<AttackReport> updatedLastReports = (List<AttackReport>) lastReportsField.get(user);
        assertTrue(updatedLastReports.isEmpty(), "lastReports debería estar vacío después de un MISS.");
    }

    @Test
    public void testOnStatusChanged_AttackingDifferentShip() throws Exception {
        when(mockAttackReport.getResult()).thenReturn(AttackReport.HitResult.HIT);
        when(mockAttackReport.getShip()).thenReturn(differentMockShip);

        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> mockLastReports = new ArrayList<>();
        AttackReport previousReport = mock(AttackReport.class);
        when(previousReport.getShip()).thenReturn(mockShip);
        mockLastReports.add(previousReport);
        lastReportsField.set(user, mockLastReports);

        Field actualStateField = User.class.getDeclaredField("actualState");
        actualStateField.setAccessible(true);
        actualStateField.set(user, UserState.ATTACKING_SHIP);

        user.onStatusChanged(mockAttackReport);

        verify(mockGame, times(1)).restorePreviousHealthFleet(anyInt());
        assertEquals(UserState.GENERAL, getUserState(user), "El estado debería cambiar a GENERAL después de atacar un barco diferente.");

        List<AttackReport> updatedLastReports = (List<AttackReport>) lastReportsField.get(user);
        assertTrue(updatedLastReports.isEmpty(), "lastReports debería estar vacío después de atacar un barco diferente.");
    }

    @Test
    public void testOnStatusChanged_Hit() throws Exception {
        when(mockAttackReport.getResult()).thenReturn(AttackReport.HitResult.HIT);
        when(mockAttackReport.getShip()).thenReturn(mockShip);

        user.onStatusChanged(mockAttackReport);

        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> lastReports = (List<AttackReport>) lastReportsField.get(user);
        assertEquals(1, lastReports.size(), "Debería haber un reporte en lastReports después de un HIT.");
        assertEquals(UserState.ATTACKING_SHIP, getUserState(user), "El estado debería cambiar a ATTACKING_SHIP después de un HIT.");
    }

    @Test
    public void testOnStatusChanged_Sunk() throws Exception {
        when(mockAttackReport.getResult()).thenReturn(AttackReport.HitResult.SUNK);

        user.onStatusChanged(mockAttackReport);

        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> lastReports = (List<AttackReport>) lastReportsField.get(user);
        assertTrue(lastReports.isEmpty(), "lastReports debería estar vacío después de un SUNK.");
    }

    @Test
    public void testOnStatusChanged_MissWithoutLastReports() throws Exception {
        when(mockAttackReport.getResult()).thenReturn(AttackReport.HitResult.MISS);
        when(mockAttackReport.getShip()).thenReturn(mockShip);

        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> emptyLastReports = new ArrayList<>();
        lastReportsField.set(user, emptyLastReports); // lastReports vacío

        user.onStatusChanged(mockAttackReport);

        verify(mockGame, times(0)).restorePreviousHealthFleet(anyInt());
        assertEquals(UserState.GENERAL, getUserState(user), "El estado no debería cambiar ya que no había reportes previos.");
        List<AttackReport> updatedLastReports = (List<AttackReport>) lastReportsField.get(user);
        assertTrue(updatedLastReports.isEmpty(), "lastReports debería seguir vacío.");
    }

    @Test
    public void testOnStatusChanged_SameShipWhileAttacking() throws Exception {
        when(mockAttackReport.getResult()).thenReturn(AttackReport.HitResult.HIT);
        when(mockAttackReport.getShip()).thenReturn(mockShip);

        Field actualStateField = User.class.getDeclaredField("actualState");
        actualStateField.setAccessible(true);
        actualStateField.set(user, UserState.ATTACKING_SHIP);

        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> mockLastReports = new ArrayList<>();
        AttackReport previousReport = mock(AttackReport.class);
        when(previousReport.getShip()).thenReturn(mockShip); 
        mockLastReports.add(previousReport);
        lastReportsField.set(user, mockLastReports);

        user.onStatusChanged(mockAttackReport);

        verify(mockGame, times(0)).restorePreviousHealthFleet(anyInt());
        assertEquals(UserState.ATTACKING_SHIP, getUserState(user), "El estado no debería cambiar porque el barco es el mismo.");
        List<AttackReport> updatedLastReports = (List<AttackReport>) lastReportsField.get(user);
        assertEquals(2, updatedLastReports.size(), "lastReports no debería vaciarse porque el barco es el mismo.");
    }

    // Métodos de reflexión para acceder al estado privado 'actualState'
    private UserState getUserState(User user) throws Exception {
        Field actualStateField = User.class.getDeclaredField("actualState");
        actualStateField.setAccessible(true);
        return (UserState) actualStateField.get(user);
    }

    // Pruebas para hitOnPlace
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

    @Test
    public void testHitOnPlaceThrowsIllegalArgumentException() {
        Point position = new Point(3, 5);
        FiringMode mode = FiringMode.TORPEDO_FIRING_MODE;
        when(mockGame.hitOnPlace(position, mode)).thenThrow(new IllegalArgumentException("No hay torpedos disponibles."));
        assertThrows(IllegalArgumentException.class, () -> user.hitOnPlace(position, mode), "Debería lanzar IllegalArgumentException si no hay torpedos.");
    }

    // Pruebas para lastShipIsEqualToCurrent
    @Test
    public void testLastShipIsEqualToCurrent_NullShip() throws Exception {
        boolean result = invokeLastShipIsEqualToCurrent(user, null);
        assertFalse(result, "Debería devolver false cuando el barco es null.");
    }

    @Test
    public void testLastShipIsEqualToCurrent_EmptyLastReports() throws Exception {
        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        lastReportsField.set(user, new ArrayList<>());

        boolean result = invokeLastShipIsEqualToCurrent(user, mockShip);
        assertFalse(result, "Debería devolver false cuando lastReports está vacío.");
    }

    @Test
    public void testLastShipIsEqualToCurrent_SameShip() throws Exception {
        AttackReport mockAttackReport = mock(AttackReport.class);
        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> mockLastReports = new ArrayList<>();
        mockLastReports.add(mockAttackReport);

        when(mockAttackReport.getShip()).thenReturn(mockShip);
        lastReportsField.set(user, mockLastReports);

        boolean result = invokeLastShipIsEqualToCurrent(user, mockShip);
        assertTrue(result, "Debería devolver true cuando el barco es el mismo que el último en lastReports.");
    }

    @Test
    public void testLastShipIsEqualToCurrent_DifferentShip() throws Exception {
        AttackReport mockAttackReport = mock(AttackReport.class);
        Field lastReportsField = User.class.getDeclaredField("lastReports");
        lastReportsField.setAccessible(true);
        List<AttackReport> mockLastReports = new ArrayList<>();
        mockLastReports.add(mockAttackReport);

        when(mockAttackReport.getShip()).thenReturn(differentMockShip);
        lastReportsField.set(user, mockLastReports);

        boolean result = invokeLastShipIsEqualToCurrent(user, mockShip);
        assertFalse(result, "Debería devolver false cuando el barco es diferente al último en lastReports.");
    }

    // Pruebas para undoRecentActions
    @Test
    public void testUndoRecentActions_RemovesDuplicates() throws Exception {
        AttackReport duplicateAttackReport = mock(AttackReport.class);
        AttackReport uniqueAttackReport = mock(AttackReport.class);

        when(duplicateAttackReport.getShip()).thenReturn(mockShip);
        when(uniqueAttackReport.getShip()).thenReturn(mockShip);

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

    @Test
    public void testUndoRecentActions_NoActionsIfLastReportsIsEmpty() throws Exception {
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
        Game mockGame = mock(Game.class);
        user.setCurrentSession(mockGame);
        assertEquals(mockGame, user.getCurrentSession(), "getCurrentSession debería devolver la sesión de juego actual.");
    }
}
