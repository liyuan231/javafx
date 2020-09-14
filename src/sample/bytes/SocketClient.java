package sample.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 传输字节流
 */
public class SocketClient {
    public static void main(String[] args) throws IOException {
        String ip = "127.0.0.1";
        int port = 8008;
        Socket socket = new Socket(ip, port);
//        OutputStream outputStream = socket.getOutputStream();
        socket.getOutputStream().write("liyuanHello".getBytes(StandardCharsets.UTF_8));
        //上述为发消息
        socket.shutdownOutput();//发完消息后关闭发消息功能
        //下述为接受服务端消息
        InputStream inputStream = socket.getInputStream();
        int val = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((val = inputStream.read()) != -1) {
            stringBuilder.append((char) val);
        }
        System.out.println(stringBuilder.toString());
        socket.close();
    }
}
