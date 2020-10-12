import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class UDPServer {
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(8081);
        DatagramSocket datagramSocket = new DatagramSocket(8081);
        byte[] buf = new byte[1024];
        DatagramPacket receiveDatagramPacket = new DatagramPacket(buf, buf.length);
        while (true) {
            datagramSocket.receive(receiveDatagramPacket);
            String s = new String(receiveDatagramPacket.getData());
            clean(buf, s.length());//取完数据就清空byte缓冲数组
            DatagramPacket serverDatagramPacket = new DatagramPacket(buf, buf.length, receiveDatagramPacket.getSocketAddress());
            String tmp = "20181003114&李源&"+new Date().toString()+"&"+s;
            System.out.println(tmp);
            serverDatagramPacket.setData(tmp.getBytes(StandardCharsets.UTF_8));
            datagramSocket.send(serverDatagramPacket);
            receiveDatagramPacket.setLength(buf.length);
        }
    }

    private static void clean(byte[] buf, int length) {
        for (int i = 0; i < length; i++) {
            buf[i] = 0;
        }
    }
    @Test
    public void test(){
        System.out.println(new Date().toString());
    }
}
