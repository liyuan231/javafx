package sample.chars;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用字符流Reader和Writer
 */
public class SocketServer_ {
    private static final int port = 8008;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        while (bufferedReader.ready()) {
                            stringBuilder.append(bufferedReader.readLine());
                        }
                        System.out.println(socket.getInetAddress() + ":" + stringBuilder.toString());
                        socket.shutdownInput();

                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bufferedWriter.write("From Server:" + stringBuilder.toString());
                        socket.shutdownOutput();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };
            threadPool.submit(runnable);
        }
    }
}
