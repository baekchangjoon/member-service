package com.example.addressvalidator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MockTcpServerTest {

    private static final int PORT = 9999;
    private static MockTcpServer server;
    private static Thread serverThread;

    @BeforeAll
    static void setUp() {
        server = new MockTcpServer(PORT);
        server.addStub("Hello Server", "Hello Client\n");
        server.addStub("Seoul", "가능\n");
        server.addStub("Busan", "불가능\n");

        serverThread = new Thread(() -> server.start());
        serverThread.start();

        // 서버가 시작될 때까지 잠시 대기
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
    }

    @AfterAll
    static void tearDown() {
        serverThread.interrupt();
    }

    @Test
    void testHelloServer() throws IOException {
        try (Socket socket = new Socket("localhost", PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            writer.write("Hello Server\n");
            writer.flush();

            String response = reader.readLine();
            assertEquals("Hello Client", response);
        }
    }

    @Test
    void testSeoul() throws IOException {
        try (Socket socket = new Socket("localhost", PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            writer.write("Seoul\n");
            writer.flush();

            String response = reader.readLine();
            assertEquals("가능", response);
        }
    }

    @Test
    void testBusan() throws IOException {
        try (Socket socket = new Socket("localhost", PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            writer.write("Busan\n");
            writer.flush();

            String response = reader.readLine();
            assertEquals("불가능", response);
        }
    }

    @Test
    void testUnknownRequest() throws IOException {
        try (Socket socket = new Socket("localhost", PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            writer.write("Unknown City\n");
            writer.flush();

            String response = reader.readLine();
            assertEquals("No matching stub", response);
        }
    }
}
