package multicast;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Multicast {
    private InetAddress inetAddress;
    private int port;
    private MulticastSocket multicastSocket;

    public Multicast() throws IOException {
        this.port = 8900;
        inetAddress = InetAddress.getByName("225.0.0.1");

        multicastSocket = new MulticastSocket(port);

        InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);

        multicastSocket.joinGroup(socketAddress, networkInterface);
    }

    public void send(String message) throws IOException {
        byte[] bytes = ("(20181003114:)" + message).getBytes(StandardCharsets.UTF_8);//发送的信息
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, inetAddress, port);
        multicastSocket.send(datagramPacket);
    }

    public String receive() throws IOException {
        byte[] bytes = new byte[1024];//接受信息时的缓冲
        DatagramPacket receiveDatagramPacket = new DatagramPacket(bytes, bytes.length);
        multicastSocket.receive(receiveDatagramPacket);
        String s = new String(receiveDatagramPacket.getData());
        return receiveDatagramPacket.getAddress() + " " + s;
    }
}
