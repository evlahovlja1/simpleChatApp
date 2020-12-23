package ba.unsa.etf.rpr;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnection {
    private String ip;
    private int port;

    public Client(Consumer<Serializable> onReceiveCallback, String ip, int port) {
        super(onReceiveCallback);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
