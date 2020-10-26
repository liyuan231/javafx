package t;

import t.service.Client;
import t.service.impl.HttpClientImpl;
import t.service.impl.HttpsClientImpl;

import java.io.IOException;

public class ClientFactory {
    public static Client retrieveClient(String type, String ip, Integer port) throws IOException {
        switch (type.toLowerCase()) {
            case "http":
                return new HttpClientImpl(ip, port);
            case "https":
                return new HttpsClientImpl(ip, port);
        }
        throw new RuntimeException("类型对应的socket不存在！");
    }
}
