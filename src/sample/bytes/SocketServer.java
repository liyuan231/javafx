package sample.bytes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    private static final int port = 8008;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(socket.getOutputStream())));
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        printWriter.println("欢迎！！！");
//                        printWriter.write(String.valueOf("欢迎！"));
//                        printWriter.write();
//                        bufferedWriter.flush();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            System.out.println(line);
                            printWriter.println("[LIYUAN]" + line);
//                            printWriter.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };
            threadPool.submit(runnable);
        }
    }

}
