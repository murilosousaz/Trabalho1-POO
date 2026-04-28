package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    public void onModoSimples(ActionEvent event) {
        navegarParaJogo(event, "simples");
    }

    @FXML
    public void onModoCompetitivo(ActionEvent event) {
        navegarParaJogo(event, "competitivo");
    }

    @FXML
    public void onModoInteligente(ActionEvent event) {
        navegarParaJogo(event, "inteligente");
    }

    @FXML
    public void onModoObstaculos(ActionEvent event) {
        navegarParaJogo(event, "obstaculos");
    }

    private void navegarParaJogo(ActionEvent event, String modo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/tabuleiro.fxml")
            );
            Parent raiz = loader.load();

            JogoController jogoController = loader.getController();
            jogoController.iniciarModo(modo);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 4. Troca a cena
            stage.setScene(new Scene(raiz));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}