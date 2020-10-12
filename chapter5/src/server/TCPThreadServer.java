package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPThreadServer {
    private static ServerSocket serverSocket;
    private static int port = 8008;
    private static ExecutorService executorService;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(port);
        executorService = Executors.newFixedThreadPool(100);
        while (true) {
            Socket socket = serverSocket.accept();
            executorService.submit(() -> {
                try {
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.trim().equals("bye")) {
                            break;
                        }
                        printWriter.println("From 服务器：");
                        printWriter.println(line);
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }
}
