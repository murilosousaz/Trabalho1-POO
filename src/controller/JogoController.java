package controller;

import exceptions.MovimentoInvalidoException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import model.Bomba;
import model.Obstaculo;
import model.Robo;
import model.RoboInteligente;
import model.Tabuleiro;
import view.EntradaDados;
import view.TabuleiroView;

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
    private Random random = new Random();

    @FXML
    public void initialize() {
        tabuleiro     = new Tabuleiro();
        tabuleiroView = new TabuleiroView(gridTabuleiro);

        tabuleiro.turnoAtualProperty().addListener(
                (obs, antigo, novo) ->
                        labelTurno.setText("Turno: " + novo)
        );

        tabuleiroView.renderizar(tabuleiro); // tela inicial vazia
    }

    @FXML
    public void onModoSimples() {
        tabuleiro.resetar();
        labelModo.setText("Modo: Simples");
        labelStatus.setText("Use os botões para mover.");

        int[] posAlimento = EntradaDados.lerCoordenada("Posição do Alimento");
        if (posAlimento == null) return;

        tabuleiro.definirAlimento(posAlimento[0], posAlimento[1]);
        tabuleiro.adicionarRobo(new Robo("blue"));

        habilitarBotoesMovimento(true);
        tabuleiroView.renderizar(tabuleiro);
    }

    @FXML
    public void onModoCompetitivo() {
        tabuleiro.resetar();
        labelModo.setText("Modo: Competitivo");
        labelStatus.setText("Dois robôs buscam o alimento.");
        habilitarBotoesMovimento(false);

        int[] posAlimento = EntradaDados.lerCoordenada("Posição do Alimento");
        if (posAlimento == null) return;

        tabuleiro.definirAlimento(posAlimento[0], posAlimento[1]);
        tabuleiro.adicionarRobo(new Robo("blue"));
        tabuleiro.adicionarRobo(new Robo("red"));

        tabuleiroView.renderizar(tabuleiro);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(700), e -> {
                    executarTurnoAleatorio(tabuleiro.getRobos());
                    tabuleiroView.renderizar(tabuleiro);
                    tabuleiro.avancarTurno();

                    if (tabuleiro.algumRoboEncontrouAlimento()) {
                        labelStatus.setText("🏆 Um robô encontrou o alimento!");
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // repete infinitamente
        timeline.play();                              // inicia
    }

    @FXML
    public void onModoInteligente() {
        tabuleiro.resetar();
        labelModo.setText("Modo: Inteligente");
        labelStatus.setText("Robô normal vs Inteligente.");
        habilitarBotoesMovimento(false);

        int[] posAlimento = EntradaDados.lerCoordenada("Posição do Alimento");
        if (posAlimento == null) return;

        tabuleiro.definirAlimento(posAlimento[0], posAlimento[1]);
        tabuleiro.adicionarRobo(new Robo("blue"));
        tabuleiro.adicionarRobo(new RoboInteligente("gold", random));

        tabuleiroView.renderizar(tabuleiro);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(800), e -> {
                    executarTurnoAleatorio(tabuleiro.getRobos());
                    tabuleiroView.renderizar(tabuleiro);
                    tabuleiro.avancarTurno();

                    if (tabuleiro.algumRoboEncontrouAlimento()) {
                        labelStatus.setText("🏆 Alimento encontrado!");
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void onModoObstaculos() {
        tabuleiro.resetar();
        labelModo.setText("Modo: Obstáculos");
        labelStatus.setText("Cuidado com bombas e rochas!");
        habilitarBotoesMovimento(false);

        int[] posBomba = EntradaDados.lerCoordenada("Posição da Bomba");
        if (posBomba != null)
            tabuleiro.adicionarObstaculo(new Bomba(1, posBomba[0], posBomba[1]));

        int[] posAlimento = EntradaDados.lerCoordenada("Posição do Alimento");
        if (posAlimento == null) return;

        tabuleiro.definirAlimento(posAlimento[0], posAlimento[1]);
        tabuleiro.adicionarRobo(new Robo("blue"));

        tabuleiroView.renderizar(tabuleiro);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(700), e -> {
                    executarTurnoComObstaculos();
                    tabuleiroView.renderizar(tabuleiro);
                    tabuleiro.avancarTurno();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML public void onMoverCima()     { moverRoboComVerificacao("up");    }
    @FXML public void onMoverBaixo()    { moverRoboComVerificacao("down");  }
    @FXML public void onMoverDireita()  { moverRoboComVerificacao("right"); }
    @FXML public void onMoverEsquerda() { moverRoboComVerificacao("left");  }

    @FXML
    public void onReiniciar() {
        tabuleiro.resetar();
        labelModo.setText("Modo: —");
        labelStatus.setText("Selecione um modo.");
        habilitarBotoesMovimento(false);
        tabuleiroView.renderizar(tabuleiro);
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
        if (obs != null) {
            obs.bater(robo);
            if (robo.getEixoX() == -1) {
                tabuleiro.removerObstaculo(obs.getId());
                labelStatus.setText("💥 Robô destruído pela bomba!");
            } else {
                robo.setEixoX(robo.getEixoX());
                labelStatus.setText("🪨 Movimento bloqueado pela rocha!");
            }
        }
    }

    private void executarTurnoAleatorio(List<Robo> robos) {
        String[] direcoes = {"up", "down", "right", "left"};
        for (Robo robo : robos) {
            if (robo.getEixoX() == -1) continue; // pula destruídos
            try {
                robo.mover(direcoes[random.nextInt(4)]);
                verificarObstaculoNaPosicao(robo);
            } catch (MovimentoInvalidoException e) {
                // ignora e tenta no próximo turno
            }
        }
    }

    private void executarTurnoComObstaculos() {
        executarTurnoAleatorio(tabuleiro.getRobos());
        if (tabuleiro.todosRobosDestruidos()) {
            labelStatus.setText("💀 Todos os robôs foram destruídos.");
        } else if (tabuleiro.algumRoboEncontrouAlimento()) {
            labelStatus.setText("🏆 Alimento encontrado!");
        }
    }

    private void habilitarBotoesMovimento(boolean habilitar) {
        btnCima.setDisable(!habilitar);
        btnBaixo.setDisable(!habilitar);
        btnEsquerda.setDisable(!habilitar);
        btnDireita.setDisable(!habilitar);
    }




}