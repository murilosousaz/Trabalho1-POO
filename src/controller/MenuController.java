package controller;

import app.Caminhos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML public void onModoSimples(ActionEvent e)      { navegar(e, "simples");     }
    @FXML public void onModoCompetitivo(ActionEvent e)  { navegar(e, "competitivo"); }
    @FXML public void onModoInteligente(ActionEvent e)  { navegar(e, "inteligente"); }
    @FXML public void onModoObstaculos(ActionEvent e)   { navegar(e, "obstaculos");  }
    @FXML public void onModoEstrategico(ActionEvent e)  { navegar(e, "estrategico"); }

    private void navegar(ActionEvent event, String modo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Caminhos.ANCORA.getResource(Caminhos.FXML_TABULEIRO)
            );
            Parent raiz = loader.load();

            JogoController jogoController = loader.getController();
            jogoController.iniciarModo(modo);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(raiz, 760, 580));
            stage.setMinWidth(760);
            stage.setMinHeight(580);
            stage.show();

        } catch (IOException e) { e.printStackTrace(); }
    }
}