package client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class FileDataClient {
    private Socket dataSocket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public FileDataClient(String ip, Integer port) throws IOException {
        dataSocket = new Socket(ip, port);
        printWriter = new PrintWriter(new OutputStreamWriter(dataSocket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
    }

    public void getFile(File savedFile) throws IOException {
        if (dataSocket != null) {
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savedFile)));
            InputStream inputStream = dataSocket.getInputStream();
            OutputStream outputStream = dataSocket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            printWriter.println(savedFile.getName());
            printWriter.flush();
            int val = 0;
            while ((val = inputStream.read()) != -1) {
                dataOutputStream.write(val);
            }
            dataOutputStream.close();
            if (dataSocket != null) {
                dataSocket.close();
            }
        }
    }
}
