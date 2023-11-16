package Application;

import javafx.event.ActionEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainUtilityTest {

    @Test
    void testChangeScene() {
        ActionEvent event = new ActionEvent();
        assertDoesNotThrow(() -> MainUtility.changeScene(event, "/View/Dashboard.fxml", "Tittle", "Thompson", "Wasshington", "VIP"));
    }

    @Test
    void testSignupUser() {
        ActionEvent event = new ActionEvent();
        assertDoesNotThrow(() -> MainUtility.signupUser(event, "Leonardo", "DiCaprio", "leo", "inception", "No"));
        
        assertDoesNotThrow(() -> MainUtility.signupUser(event, "Brad", "Pitt", "brad", "fightclub", "No"));
    }

    @Test
    void testLoginUser() {
        ActionEvent event = new ActionEvent();
        assertDoesNotThrow(() -> MainUtility.loginUser(event, "leo", "inception"));
        
        assertDoesNotThrow(() -> MainUtility.loginUser(event, "nonexistentuser", "wrongpassword"));
    }

    @Test
    void testUpdateUserProfile() {
        ActionEvent event = new ActionEvent();
        assertDoesNotThrow(() -> MainUtility.updateUserProfile(event, "leo", "Leonardo", "DiCaprio", "newpassword"));

        assertDoesNotThrow(() -> MainUtility.updateUserProfile(event, "nonexistentuser", "Updated", "User", "newpassword"));
    }

    @Test
    void testFindPostDetails() {
        assertNotNull(MainUtility.findPostDetails("1"));

        assertNull(MainUtility.findPostDetails("nonexistent"));
    }
}


