package com.example.addressvalidator;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MockTcpClient {
    private final String host;
    private final int port;

    public MockTcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendRequest(String request) throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            // 요청 전송
            writer.write(request + "\n");
            writer.flush();

            // 응답 수신
            return reader.readLine();
        }
    }

    public static void main(String[] args) {
        MockTcpClient client = new MockTcpClient("localhost", 9999);
        Scanner scanner = new Scanner(System.in);

        System.out.println("MockTcpClient started. Type 'exit' to quit.");
        System.out.println("Available commands:");
        System.out.println("- Hello Server");
        System.out.println("- Seoul");
        System.out.println("- Busan");

        while (true) {
            System.out.print("\nEnter your message: ");
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                break;
            }

            try {
                String response = client.sendRequest(input);
                System.out.println("Server response: " + response);
            } catch (IOException e) {
                System.err.println("Error communicating with server: " + e.getMessage());
            }
        }

        scanner.close();
    }
} 