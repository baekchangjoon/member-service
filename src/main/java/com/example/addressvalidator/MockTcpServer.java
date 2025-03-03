package com.example.addressvalidator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MockTcpServer {

    private final int port;
    private final Map<String, String> stubs;

    public MockTcpServer(int port) {
        this.port = port;
        this.stubs = new HashMap<>();
    }

    public void addStub(String request, String response) {
        stubs.put(request, response);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("MockTcpServer started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter writer =
                     new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String requestData = reader.readLine(); // 간단히 한 줄만 읽는다고 가정
            if (requestData != null) {
                String responseData = stubs.getOrDefault(
                        requestData,
                        "No matching stub\n"
                );
                writer.write(responseData);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // main()은 10줄 이하를 넘어가지 않도록 간단하게
    public static void main(String[] args) {
        MockTcpServer mockServer = new MockTcpServer(9999);
        mockServer.addStub("Hello Server", "Hello Client\n");
        mockServer.addStub("Seoul", "가능\n");
        mockServer.addStub("Busan", "불가능\n");
        mockServer.start();
    }
}
