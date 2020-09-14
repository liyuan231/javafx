package sample.bytes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
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
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream)));
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Hello, this is a response from server->");
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        System.out.println(socket.getInetAddress() + ":" + stringBuilder.toString());
                        socket.shutdownInput();

                        byte[] bytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
                        socket.getOutputStream().write(bytes);
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
