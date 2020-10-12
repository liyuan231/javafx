import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPClient {
    private int remotePort;
    private InetAddress remoteIP;
    private DatagramSocket datagramSocket;
    private static final int MAX_PACKET_SIZE = 512;

    public UDPClient(int remotePort, String remoteIP) throws UnknownHostException, SocketException {
        this.remotePort = remotePort;
        this.remoteIP = InetAddress.getByName(remoteIP);
        datagramSocket = new DatagramSocket();
    }

    public void send(String message) throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        //数据报
        DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, remoteIP, remotePort);
        datagramSocket.send(datagramPacket);
    }

    public String receive() {
        String message = null;
        DatagramPacket datagramPacket = new DatagramPacket(new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);
        try {
            datagramSocket.receive(datagramPacket);
            message = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            message = null;
        }
        return message;
    }
}
