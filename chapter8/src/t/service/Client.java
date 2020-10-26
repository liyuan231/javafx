package t.service;

import java.io.IOException;

public interface Client {
    public String receive() throws IOException;

    public void send(String message);

    public void close() throws IOException;

    public boolean isClosed();
}
