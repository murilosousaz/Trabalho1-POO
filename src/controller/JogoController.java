package controller;

import exceptions.MovimentoInvalidoException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Bomba;
import model.Obstaculo;
import model.Robo;
import model.RoboInteligente;
import model.Tabuleiro;
import view.EntradaDados;
import view.TabuleiroView;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public abstract class JogoController {
    @FXML private GridPane gridTabuleiro;
    @FXML private Label labelTurno;
    @FXML private Label labelModo;
    @FXML private Label labelStatus;
    @FXML private Button btnCima;
    @FXML private Button btnBaixo;
    @FXML private Button btnEsquerda;
    @FXML private Button btnDireita;

    private Tabuleiro tabuleiro;
    private TabuleiroView tabuleiroView;
    private Timeline timelineAtiva;
    private Random random = new Random();

    @FXML
    public void initialize() {
        tabuleiro = new Tabuleiro();
        tabuleiroView = new TabuleiroView(gridTabuleiro);

        tabuleiro.turnoAtualProperty().addListener(
                (obs, antigo, novo) ->
                        labelTurno.setText("Turno: " + novo)
        );

        habilitarBotoesMovimento(false);
        tabuleiroView.renderizar(tabuleiro); // tela inicial vazia
    }

    public void iniciarModo(String modo){
        tabuleiro.resetar();
        pararTimelineAtiva();

        switch (modo) {
            case "simples"     -> configurarModoSimples();
            case "competitivo" -> configurarModoCompetitivo();
            case "inteligente" -> configurarModoInteligente();
            case "obstaculos"  -> configurarModoObstaculos();
        }
    }

    private void configurarModoSimples() {
        labelModo.setText("Modo: Simples");
        labelStatus.setText("Use os botões para mover.");

        int[] pos = EntradaDados.lerCoordenada("Posição do Alimento");
        if (pos == null) return;

        tabuleiro.definirAlimento(pos[0], pos[1]);
        tabuleiro.adicionarRobo(new Robo("blue"));

        habilitarBotoesMovimento(true);
        tabuleiroView.renderizar(tabuleiro);
    }

    private void configurarModoCompetitivo() {
        labelModo.setText("Modo: Competitivo");
        labelStatus.setText("Dois robôs buscam o alimento.");
        habilitarBotoesMovimento(false);

        int[] pos = EntradaDados.lerCoordenada("Posição do Alimento");
        if (pos == null) return;

        tabuleiro.definirAlimento(pos[0], pos[1]);
        tabuleiro.adicionarRobo(new Robo("blue"));
        tabuleiro.adicionarRobo(new Robo("red"));

        tabuleiroView.renderizar(tabuleiro);
        iniciarTimeline(700, () -> {
            executarTurnoAleatorio(tabuleiro.getRobos());
            tabuleiroView.renderizar(tabuleiro);
            tabuleiro.avancarTurno();
            if (tabuleiro.algumRoboEncontrouAlimento()) {
                labelStatus.setText("🏆 Um robô encontrou o alimento!");
                pararTimelineAtiva();
            }
        });
    }

    private void configurarModoInteligente() {
        labelModo.setText("Modo: Inteligente");
        labelStatus.setText("Robô normal vs Inteligente.");
        habilitarBotoesMovimento(false);

        int[] pos = EntradaDados.lerCoordenada("Posição do Alimento");
        if (pos == null) return;

        tabuleiro.definirAlimento(pos[0], pos[1]);
        tabuleiro.adicionarRobo(new Robo("blue"));
        tabuleiro.adicionarRobo(new RoboInteligente("gold", random));

        tabuleiroView.renderizar(tabuleiro);
        iniciarTimeline(800, () -> {
            executarTurnoAleatorio(tabuleiro.getRobos());
            tabuleiroView.renderizar(tabuleiro);
            tabuleiro.avancarTurno();
            if (tabuleiro.algumRoboEncontrouAlimento()) {
                labelStatus.setText("🏆 Alimento encontrado!");
                pararTimelineAtiva();
            }
        });
    }

    private void configurarModoObstaculos() {
        labelModo.setText("Modo: Obstáculos");
        labelStatus.setText("Cuidado com bombas e rochas!");
        habilitarBotoesMovimento(false);

        int[] posBomba = EntradaDados.lerCoordenada("Posição da Bomba");
        if (posBomba != null)
            tabuleiro.adicionarObstaculo(new Bomba(1, posBomba[0], posBomba[1]));

        int[] pos = EntradaDados.lerCoordenada("Posição do Alimento");
        if (pos == null) return;

        tabuleiro.definirAlimento(pos[0], pos[1]);
        tabuleiro.adicionarRobo(new Robo("blue"));

        tabuleiroView.renderizar(tabuleiro);
        iniciarTimeline(700, () -> {
            executarTurnoAleatorio(tabuleiro.getRobos());
            tabuleiroView.renderizar(tabuleiro);
            tabuleiro.avancarTurno();
            if (tabuleiro.todosRobosDestruidos()) {
                labelStatus.setText("💀 Robô destruído!");
                pararTimelineAtiva();
            } else if (tabuleiro.algumRoboEncontrouAlimento()) {
                labelStatus.setText("🏆 Alimento encontrado!");
                pararTimelineAtiva();
            }
        });
    }

    @FXML public void onMoverCima()     { moverRoboComVerificacao("up");    }
    @FXML public void onMoverBaixo()    { moverRoboComVerificacao("down");  }
    @FXML public void onMoverDireita()  { moverRoboComVerificacao("right"); }
    @FXML public void onMoverEsquerda() { moverRoboComVerificacao("left");  }

    @FXML
    public void onVoltarMenu(javafx.event.ActionEvent event) {
        pararTimelineAtiva();
        try {
            Parent raiz = FXMLLoader.load(
                    getClass().getResource("/fxml/menu.fxml")
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(raiz));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moverRoboComVerificacao(String direcao) {
        if (tabuleiro.getRobos().isEmpty()) return;
        Robo robo = tabuleiro.getRobos().get(0);
        try {
            robo.mover(direcao);
            verificarObstaculoNaPosicao(robo);
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

    private void verificarObstaculoNaPosicao(Robo robo) {
        Obstaculo obs = tabuleiro.getObstaculoNaPosicao(
                robo.getEixoX(), robo.getEixoY()
        );
        if (obs == null) return;

        obs.bater(robo);

        if (robo.getEixoX() == -1) {
            tabuleiro.removerObstaculo(obs.getId());
            labelStatus.setText("💥 Robô destruído pela bomba!");
        } else {
            labelStatus.setText("🪨 Movimento bloqueado pela rocha!");
        }
    }

    private void executarTurnoAleatorio(List<Robo> robos) {
        String[] direcoes = {"up", "down", "right", "left"};
        for (Robo robo : robos) {
            if (robo.getEixoX() == -1) continue;
            try {
                robo.mover(direcoes[random.nextInt(4)]);
                verificarObstaculoNaPosicao(robo);
            } catch (MovimentoInvalidoException e) {
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