package ba.unsa.etf.rpr;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnection {
    private ConnectionThread connectionThread = new ConnectionThread();
    private Consumer<Serializable> onReceiveCallback;

    public NetworkConnection(Consumer<Serializable> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
        //da ConnectionThread ne blokira JVM da se ne moze zatvoriti
        connectionThread.setDaemon(true);
    }

    public void startConnection() throws Exception {
        connectionThread.start();
    }

    public void send(Serializable data) throws Exception {
        connectionThread.out.writeObject(data);
    }

    public void closeConnection() throws Exception {
        connectionThread.socket.close();
    }'

    //po pravilu trebalo bi napraviti 2 klase za server i klijent
    //ali za ovaj jednostavan slucaj koristimo jednu
    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    //primanje i slanje sadrzaja se MORAJU desavati na odvojenim nitima
    //jer ce jedna operacija cekati drugu i nece se moci izvrsiti
    private class ConnectionThread extends Thread {
        private Socket socket;
        private ObjectOutputStream out;

        //serverSocket prelazi u obicni socket kada se uspostavi komunikacija sa klijentom
        //zato je .accept() blokirajuci poziv
        @Override
        public void run() {
            try (ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                 Socket socket = isServer() ? server.accept() : new Socket(getIP(), getPort());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                this.socket = socket;
                this.out = out;
                //nemoj cekati da se buffer napuni = brzi protok
                this.socket.setTcpNoDelay(true);

                while (true) {
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallback.accept(data);
                }
            }
            catch (Exception e) {
                onReceiveCallback.accept("Connection closed");
            }
        }
    }
}
