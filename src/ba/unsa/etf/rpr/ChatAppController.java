package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatAppController {
    @FXML private TextArea messages;
    @FXML private TextField input;
    //false za klijenta i rerun app
    private boolean isServer = false;
    private NetworkConnection connection = isServer ? createServer() : createClient();


    private Server createServer() {
        return new Server(data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        }, 55555);
    }

    private Client createClient() {
        return new Client(data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        }, "127.0.0.1", 55555);
    }


    public void createConnection() throws Exception {
        connection.startConnection();
    }

    public void closeConnection() throws Exception {
        connection.closeConnection();
    }

    public void sendMessage(ActionEvent actionEvent) {
        String message = isServer ? "Server: " : "Client: ";
        message += input.getText();
        input.clear();
        messages.appendText(message + "\n");
        try {
            connection.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
