package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApp extends Application {
    ChatAppController kontroler;

    @Override
    public void start(Stage primaryStage) throws Exception{
        ChatAppController kontroler = new ChatAppController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        loader.setController(kontroler);
        this.kontroler = kontroler;
        kontroler.createConnection();

        Parent root = loader.load();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        this.kontroler.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
