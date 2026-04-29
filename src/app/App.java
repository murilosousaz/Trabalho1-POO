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
                getClass().getResource("/fxml/menu.fxml")
        );
        Parent raiz = loader.load();

        Scene scene = new Scene(raiz, 400, 560);

        // Configurações da janela
        stage.setTitle("Robô Game — PET Computação UECE");
        stage.setScene(scene);

        stage.setMinWidth(400);
        stage.setMinHeight(560);

        stage.centerOnScreen();

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}