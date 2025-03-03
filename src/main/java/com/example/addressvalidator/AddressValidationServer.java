package com.example.addressvalidator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// 주소판정서버: 포트 9000에서 클라이언트의 주소 데이터를 받아 유효성을 판정합니다.
public class AddressValidationServer {
    public static final int PORT = 9000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("주소판정서버가 포트 " + PORT + "에서 시작되었습니다.");
            // 무한 루프를 돌면서 클라이언트 연결 수락
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("새로운 연결 수락됨: " + clientSocket.getInetAddress());
                // 별도 스레드로 클라이언트 요청 처리
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// 각 클라이언트 연결을 처리하는 핸들러 클래스
class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            // 클라이언트로부터 주소 데이터 읽기 (한 줄)
            String address = in.readLine();
            System.out.println("받은 주소: " + address);
            
            // 주소가 "Seoul"이면 가능, 그 외는 불가능 처리
            String response = "Seoul".equalsIgnoreCase(address != null ? address.trim() : "")
                    ? "가능"
                    : "불가능";
            
            // 결과 응답 전송
            out.write(response);
            out.newLine();
            out.flush();
            System.out.println("응답 전송: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
