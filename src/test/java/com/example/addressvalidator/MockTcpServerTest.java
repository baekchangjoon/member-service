package com.example.addressvalidator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

// BugFix, This testcase doesn't work.
// MockTcpServer started well, But Testcase can't catch the server started.
public class MockTcpServerTest {

    private MockTcpServer mockTcpServer;
    private Thread serverThread;

    @BeforeEach
    public void setUp() {
        mockTcpServer = new MockTcpServer(9999);
        mockTcpServer.addStub("Hello Server", "Hello Client\n");
        serverThread = new Thread(() -> mockTcpServer.start());
        serverThread.start();
        // 서버가 띄워지길 잠시 대기 (간단히 0.5초 정도)
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        // 실제 코드라면 서버 종료 로직 필요
        // 예) serverSocket.close()를 위해, MockTcpServer에 stop() 메서드 구현 가능
        serverThread.interrupt();
        serverThread.join();
    }

    @Test
    public void testMockTcpServerResponse() throws IOException {
        try (Socket clientSocket = new Socket("localhost", 9999);
             BufferedReader reader =
                 new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter writer =
                 new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            writer.write("Hello Server\n");
            writer.flush();

            String response = reader.readLine();
            assertEquals("Hello Client", response);
        }
    }
}
