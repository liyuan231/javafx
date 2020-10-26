package t.service.impl;

import t.service.Client;

import java.io.*;
import java.net.Socket;

public class HttpClientImpl implements Client {

    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public HttpClientImpl(String ip, Integer port) throws IOException {
        socket = new Socket(ip, port);
        printWriter = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


    @Override
    public String receive() throws IOException {
        return bufferedReader.readLine();
    }

    @Override
    public void send(String message) {
        printWriter.println(message);
    }

    @Override
    public void close() throws IOException {
        bufferedReader.close();
        printWriter.close();
        socket.close();
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }
}
