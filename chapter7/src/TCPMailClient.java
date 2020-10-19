import java.io.*;
import java.net.Socket;

public class TCPMailClient {
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public TCPMailClient(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    public void send(String message) {
        printWriter.println(message);
    }

    public String receive() throws IOException {
        return bufferedReader.readLine();
    }

    public void close() throws IOException {
        bufferedReader.close();
        printWriter.close();
        socket.close();
    }
}
