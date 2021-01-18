package mainTests;

import com.jcraft.jsch.*;
import org.testng.annotations.Test;
import services.connectionHelper;

import java.io.IOException;
import java.io.InputStream;

public class DevicesTest {
    connectionHelper connection;

    DevicesTest() {
        connection = new connectionHelper();
    }

    @Test
    public void readResponse() throws IOException, JSchException, InterruptedException {
        connection.readResponse("ifconfig");
    }
}
