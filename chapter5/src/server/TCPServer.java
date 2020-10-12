package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    private static final String port = "8008";
    private static ServerSocket serverSocket;
//    private static ExecutorService executorService;

    public TCPServer() throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(port));
        System.out.println("服务器启动监听在：" + port);
//        this.serverSocket = serverSocket;
    }

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(port));
//        executorService = Executors.newFixedThreadPool(12);
        while (true) {
            Socket socket = null;
            socket = serverSocket.accept();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("New connection accepted: " + socket.getInetAddress());
            printWriter.println("From 服务器：欢迎使用本服务!");

            String message = null;
            while ((message = bufferedReader.readLine()) != null) {
                if (message.equals("bye")) {
                    printWriter.println("From 服务器：服务器断开连接结束服务！");
                    System.out.println("客户端离开！");
                    break;
                }
                printWriter.println("[From 服务器!]");
                printWriter.println(message);
            }
        }
    }

}
