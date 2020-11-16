import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CapturePacket {
    public static void main(String[] args) throws IOException {
        NetworkInterface[] networkInterfaces = JpcapCaptor.getDeviceList();
        JpcapCaptor jpcapCaptor = JpcapCaptor.openDevice(networkInterfaces[0], 65535, true, 20);
//        jpcapCaptor.setFilter("port 443 and (src host www.gdufs.edu.cn or dst host www.gdufs.edu.cn)", true);
        jpcapCaptor.setFilter("port 80 and (tcp and ip)", true);

        String ketData = null;
        while (true) {
            jpcapCaptor.processPacket(-1, new PacketReceiver() {
                @Override
                public void receivePacket(Packet packet) {
//                    System.out.println(packet);
                    String message = new String(packet.data, 0, packet.data.length, StandardCharsets.UTF_8);
                    if (message.contains("李源")) {
                        System.out.println(message);
                    }
                }
            });
        }
    }

}
