package com.kopylov.echo.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(3000);
             Socket socket = serverSocket.accept();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {

            while (true) {
                String massage = getMassageFromClient(bufferedReader);
                sendMassageToClient(massage, bufferedWriter);
            }
        }
    }

    private static String getMassageFromClient(BufferedReader bufferedReader) throws IOException {
        return bufferedReader.readLine();
    }

    private static void sendMassageToClient(String massage, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("ECHO " + massage);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
