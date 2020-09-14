package sample.chars;

import java.io.*;
import java.net.Socket;

public class SocketClient_ {
    public static void main(String[] args) throws IOException {
        String ip = "127.0.0.1";
        int port = 8008;
        Socket socket = new Socket(ip, port);
//        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("Liyuan123李源！");
        //上述为发消息
        socket.shutdownOutput();//发完消息后关闭发消息功能
        //下述为接受服务端消息
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        while (bufferedReader.ready()) {
            stringBuilder.append(bufferedReader.readLine());
        }
        System.out.println(stringBuilder.toString());
        socket.close();
    }

}
