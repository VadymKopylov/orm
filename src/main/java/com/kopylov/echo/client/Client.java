package com.kopylov.echo.client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        try (Socket clientSocket = new Socket("localhost", 3000);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter bufferedOutput = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));) {

            while (true) {
                String massage = readClientInput(bufferedReader);
                if (!massage.isEmpty()) {
                    bufferedOutput.write(massage);
                    bufferedOutput.newLine();
                    bufferedOutput.flush();

                    String echo = bufferedInput.readLine();
                    System.out.println(echo);
                }
            }
        }
    }

    private static String readClientInput(BufferedReader reader) throws IOException {
        System.out.println("Hello,please enter massage : ");
        return reader.readLine();
    }
}
