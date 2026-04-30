package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                Caminhos.ANCORA.getResource(Caminhos.FXML_MENU)
        );
        Parent raiz = loader.load();

        stage.setTitle("Robô Game — POO UECE");
        stage.setScene(new Scene(raiz, 400, 560));
        stage.setMinWidth(400);
        stage.setMinHeight(560);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}