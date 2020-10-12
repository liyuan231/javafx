import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private static final int port = 8008;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        printWriter.println("Response from server:");
                        printWriter.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
