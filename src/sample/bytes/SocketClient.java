package sample.bytes;

import java.io.*;
import java.net.Socket;

/**
 * 传输字节流
 */
public class SocketClient {
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private Socket socket;

    public SocketClient(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(bufferedReader.readLine());
    }

    public void send(String message) {
        printWriter.println(message);
        printWriter.flush();
    }

    public String receive() throws IOException {
        String s = bufferedReader.readLine();
        return s;
    }

    public void close() throws IOException {
        bufferedReader.close();
        printWriter.close();
        socket.close();
    }
}
