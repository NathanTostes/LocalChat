import org.junit.jupiter.api.Test;

import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void applicationOpenDoorCorrectly() {
        ServerSocket serverSocket = Server.openServerSocket();
        assertNotNull(serverSocket);
    }
}