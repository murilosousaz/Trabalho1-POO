package controller;

import app.Caminhos;
import controller.modos.ModoCompetitivoController;
import controller.modos.ModoInteligenteController;
import controller.modos.ModoObstaculosController;
import controller.modos.ModoSimplesController;
import exceptions.MovimentoInvalidoException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Obstaculo;
import model.Robo;
import model.Tabuleiro;
import view.EntradaDados;
import view.TabuleiroView;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class JogoController {

    @FXML private GridPane gridTabuleiro;
    @FXML private Label    labelTurno;
    @FXML private Label    labelModo;
    @FXML private Label    labelStatus;
    @FXML private Button   btnCima;
    @FXML private Button   btnBaixo;
    @FXML private Button   btnEsquerda;
    @FXML private Button   btnDireita;

    private Tabuleiro     tabuleiro;
    private TabuleiroView tabuleiroView;
    private Timeline      timelineAtiva;
    private final Random  random = new Random();

    @FXML
    public void initialize() {
        tabuleiro     = new Tabuleiro();
        tabuleiroView = new TabuleiroView(gridTabuleiro);

        tabuleiro.turnoAtualProperty().addListener(
                (obs, antigo, novo) -> labelTurno.setText("Turno: " + novo)
        );

        habilitarBotoesMovimento(false);
        tabuleiroView.renderizar(tabuleiro);
    }

    public void iniciarModo(String modo) {
        tabuleiro.resetar();
        pararTimelineAtiva();

        switch (modo) {
            case "simples"     -> iniciarSimples();
            case "competitivo" -> iniciarAutomatico(new ModoCompetitivoController());
            case "inteligente" -> iniciarAutomatico(new ModoInteligenteController());
            case "obstaculos"  -> iniciarAutomatico(new ModoObstaculosController());
        }
    }

    private void iniciarSimples() {
        ModoSimplesController modo = new ModoSimplesController();
        labelModo.setText(modo.getNome());
        labelStatus.setText(modo.getDescricao());
        if (!modo.configurar(tabuleiro)) return;
        habilitarBotoesMovimento(true);
        tabuleiroView.renderizar(tabuleiro);
    }

    private void iniciarAutomatico(ModoCompetitivoController modo) {
        labelModo.setText(modo.getNome());
        labelStatus.setText(modo.getDescricao());
        habilitarBotoesMovimento(false);
        if (!modo.configurar(tabuleiro)) return;
        tabuleiroView.renderizar(tabuleiro);
        iniciarTimeline(modo.getIntervaloMs(), this::tickAutomatico);
    }

    private void iniciarAutomatico(ModoInteligenteController modo) {
        labelModo.setText(modo.getNome());
        labelStatus.setText(modo.getDescricao());
        habilitarBotoesMovimento(false);
        if (!modo.configurar(tabuleiro)) return;
        tabuleiroView.renderizar(tabuleiro);
        iniciarTimeline(modo.getIntervaloMs(), this::tickAutomatico);
    }

    private void iniciarAutomatico(ModoObstaculosController modo) {
        labelModo.setText(modo.getNome());
        labelStatus.setText(modo.getDescricao());
        habilitarBotoesMovimento(false);
        if (!modo.configurar(tabuleiro)) return;
        tabuleiroView.renderizar(tabuleiro);
        iniciarTimeline(modo.getIntervaloMs(), this::tickAutomatico);
    }

    private void tickAutomatico() {
        executarTurnoAleatorio(tabuleiro.getRobos());
        tabuleiroView.renderizar(tabuleiro);
        tabuleiro.avancarTurno();

        if (tabuleiro.todosRobosDestruidos()) {
            labelStatus.setText("💀 Todos os robôs foram destruídos!");
            pararTimelineAtiva();
        } else if (tabuleiro.algumRoboEncontrouAlimento()) {
            labelStatus.setText("🏆 Alimento encontrado!");
            pararTimelineAtiva();
        }
    }

    @FXML public void onMoverCima()     { moverRoboComVerificacao("up");    }
    @FXML public void onMoverBaixo()    { moverRoboComVerificacao("down");  }
    @FXML public void onMoverDireita()  { moverRoboComVerificacao("right"); }
    @FXML public void onMoverEsquerda() { moverRoboComVerificacao("left");  }

    @FXML
    public void onVoltarMenu(ActionEvent event) {
        pararTimelineAtiva();
        try {
            FXMLLoader loader = new FXMLLoader(
                    Caminhos.ANCORA.getResource(Caminhos.FXML_MENU)
            );
            Parent raiz = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(raiz, 400, 560));
            stage.setMinWidth(400);
            stage.setMinHeight(560);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[] abrirDialogoPosicao(String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Caminhos.ANCORA.getResource(Caminhos.FXML_DIALOGO)
            );
            Parent raiz = loader.load();

            DialogoPosicaoController ctrl = loader.getController();
            ctrl.setTitulo(titulo);

            Stage dialogo = new Stage();
            dialogo.initModality(Modality.APPLICATION_MODAL);
            dialogo.setTitle(titulo);
            dialogo.setScene(new Scene(raiz));
            dialogo.setResizable(false);
            dialogo.showAndWait();

            return ctrl.getResultado();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void moverRoboComVerificacao(String direcao) {
        if (tabuleiro.getRobos().isEmpty()) return;
        Robo robo = tabuleiro.getRobos().get(0);

        int posAnteriorX = robo.getEixoX();
        int posAnteriorY = robo.getEixoY();

        try {
            robo.mover(direcao);
            verificarObstaculoNaPosicao(robo, posAnteriorX, posAnteriorY);
            tabuleiro.avancarTurno();
            tabuleiroView.renderizar(tabuleiro);

            if (tabuleiro.algumRoboEncontrouAlimento()) {
                labelStatus.setText("🏆 Alimento encontrado!");
                habilitarBotoesMovimento(false);
                EntradaDados.mostrarInfo("Parabéns!", "O robô encontrou o alimento!");
            }
        } catch (MovimentoInvalidoException e) {
            labelStatus.setText("⚠ " + e.getMessage());
        }
    }

    private void verificarObstaculoNaPosicao(Robo robo, int posAnteriorX, int posAnteriorY) {
        Obstaculo obs = tabuleiro.getObstaculoNaPosicao(
                robo.getEixoX(), robo.getEixoY()
        );
        if (obs == null) return;

        obs.bater(robo);

        if (robo.getEixoX() == -1) {
            tabuleiro.removerObstaculo(obs.getId());
            labelStatus.setText("💥 Robô destruído pela bomba!");
        } else {
            robo.setEixoX(posAnteriorX);
            robo.setEixoY(posAnteriorY);
            labelStatus.setText("🪨 Movimento bloqueado pela rocha!");
        }
    }

    private void executarTurnoAleatorio(List<Robo> robos) {
        String[] direcoes = {"up", "down", "right", "left"};
        for (Robo robo : robos) {
            if (robo.getEixoX() == -1) continue;
            int posAnteriorX = robo.getEixoX();
            int posAnteriorY = robo.getEixoY();
            try {
                robo.mover(direcoes[random.nextInt(4)]);
                verificarObstaculoNaPosicao(robo, posAnteriorX, posAnteriorY);
            } catch (MovimentoInvalidoException e) {
                // tenta direção diferente no próximo turno
            }
        }
    }

    private void iniciarTimeline(int intervaloMs, Runnable acao) {
        timelineAtiva = new Timeline(
                new KeyFrame(Duration.millis(intervaloMs), e -> acao.run())
        );
        timelineAtiva.setCycleCount(Timeline.INDEFINITE);
        timelineAtiva.play();
    }

    private void pararTimelineAtiva() {
        if (timelineAtiva != null) {
            timelineAtiva.stop();
            timelineAtiva = null;
        }
    }

    private void habilitarBotoesMovimento(boolean habilitar) {
        btnCima.setDisable(!habilitar);
        btnBaixo.setDisable(!habilitar);
        btnEsquerda.setDisable(!habilitar);
        btnDireita.setDisable(!habilitar);
    }
}