import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.TCPPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class SendPacket {
    public static void main(String[] args) throws IOException {
        NetworkInterface[] networkInterfaces = JpcapCaptor.getDeviceList();
        JpcapSender jpcapSender = JpcapSender.openDevice(networkInterfaces[0]);
        TCPPacket tcp = new TCPPacket(8000, 80, 56, 78, false, false, false, false, true, false, true, true, 200, 10);
        //specify IPv4 header parameters
        tcp.setIPv4Parameter(0, false, false, false, 0, false, false, false, 0, 1010101, 100, IPPacket.IPPROTO_TCP, InetAddress.getByName("127.0.0.1"),
                InetAddress.getByName("175.24.4.196"));
        tcp.data = "20181003114&李源".getBytes(StandardCharsets.UTF_8);
        EthernetPacket ethernetPacket = new EthernetPacket();
        ethernetPacket.frametype = EthernetPacket.ETHERTYPE_IP;
        tcp.datalink = ethernetPacket;

        ethernetPacket.src_mac = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255};
        ethernetPacket.dst_mac = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255};

        jpcapSender.sendPacket(tcp);
    }
}
