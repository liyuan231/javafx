package t.service.impl;

import t.service.Client;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class HttpsClientImpl implements Client {
    private SSLSocket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public HttpsClientImpl(String ip, Integer port) throws IOException {
        socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(ip, port);
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
