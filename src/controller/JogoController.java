package controller;

import app.Caminhos;
import controller.modos.*;
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
import model.*;
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

    // Contexto do modo Estratégico — null nos outros modos
    private Alimento        alimentoObjeto;
    private RoboEstrategico roboEstrategico;
    private RoboMemoria     roboMemoria;

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

    //  iniciarModo() — ponto de entrada único para todos os modos
    public void iniciarModo(String modo) {
        pararTimelineAtiva();
        tabuleiro.resetar();
        alimentoObjeto  = null;
        roboEstrategico = null;
        roboMemoria     = null;

        switch (modo) {
            case "simples"      -> iniciarSimples();
            case "competitivo"  -> iniciarAutomatico(new ModoCompetitivoController());
            case "inteligente"  -> iniciarAutomatico(new ModoInteligenteController());
            case "obstaculos"   -> iniciarAutomatico(new ModoObstaculosController());
            case "estrategico"  -> iniciarEstrategico();
        }
    }

    // ── Modo Simples (manual) ────────────────────────────────
    private void iniciarSimples() {
        ModoSimplesController modo = new ModoSimplesController();
        labelModo.setText(modo.getNome());
        labelStatus.setText(modo.getDescricao());
        if (!modo.configurar(tabuleiro)) return;
        habilitarBotoesMovimento(true);
        tabuleiroView.renderizar(tabuleiro);
    }

    // ── Modos automáticos com Timeline ──────────────────────
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

    // ── Modo Estratégico ────────────────────────────────────
    private void iniciarEstrategico() {
        ModoEstrategicoController modo = new ModoEstrategicoController();
        labelModo.setText(modo.getNome());
        labelStatus.setText(modo.getDescricao());
        habilitarBotoesMovimento(false);
        if (!modo.configurar(tabuleiro)) return;

        // Guarda referências tipadas para uso no tick
        alimentoObjeto  = modo.getAlimento();
        roboEstrategico = modo.getRoboEstrategico();
        roboMemoria     = modo.getRoboMemoria();

        tabuleiroView.renderizar(tabuleiro);
        iniciarTimeline(modo.getIntervaloMs(), this::tickEstrategico);
    }

    private void tickAutomatico() {
        executarTurnoAleatorio(tabuleiro.getRobos());
        tabuleiroView.renderizar(tabuleiro);
        tabuleiro.avancarTurno();

        if (tabuleiro.todosRobosDestruidos()) {
            encerrar("💀 Todos os robôs foram destruídos!");
        } else if (tabuleiro.algumRoboEncontrouAlimento()) {
            encerrar("🏆 Alimento encontrado!\n" + tabuleiro.getPlacar());
        }
    }

    private void tickEstrategico() {
        // Move RoboEstrategico (se vivo e ainda não chegou)
        if (roboEstrategico.getEixoX() != -1
                && !roboEstrategico.encontrouAlimento(
                alimentoObjeto.getEixoX(), alimentoObjeto.getEixoY())) {

            int px = roboEstrategico.getEixoX();
            int py = roboEstrategico.getEixoY();
            try {
                roboEstrategico.moverEmDirecaoAo(alimentoObjeto, tabuleiro);
                verificarColisoes(roboEstrategico, px, py);
            } catch (MovimentoInvalidoException e) {
                labelStatus.setText("⚠ Estratégico: " + e.getMessage());
            }
        }

        // Move RoboMemoria (se vivo e ainda não chegou)
        if (roboMemoria.getEixoX() != -1
                && !roboMemoria.encontrouAlimento(
                alimentoObjeto.getEixoX(), alimentoObjeto.getEixoY())) {

            int px = roboMemoria.getEixoX();
            int py = roboMemoria.getEixoY();
            try {
                roboMemoria.moverComMemoria(alimentoObjeto, tabuleiro);
                verificarColisoes(roboMemoria, px, py);
            } catch (MovimentoInvalidoException e) {
                labelStatus.setText("⚠ Memória: " + e.getMessage());
            }
        }

        tabuleiroView.renderizar(tabuleiro);
        tabuleiro.avancarTurno();

        // Verifica fim: ambos chegaram OU ambos destruídos OU um chegou e outro morreu
        boolean eChegou  = roboEstrategico.getEixoX() != -1
                && roboEstrategico.encontrouAlimento(alimentoObjeto.getEixoX(), alimentoObjeto.getEixoY());
        boolean eMorreu  = roboEstrategico.getEixoX() == -1;
        boolean mChegou  = roboMemoria.getEixoX() != -1
                && roboMemoria.encontrouAlimento(alimentoObjeto.getEixoX(), alimentoObjeto.getEixoY());
        boolean mMorreu  = roboMemoria.getEixoX() == -1;

        if ((eChegou || eMorreu) && (mChegou || mMorreu)) {
            String resultado = construirPlacarEstrategico(eChegou, eMorreu, mChegou, mMorreu);
            encerrar(resultado);
        }
    }

    private void verificarColisoes(Robo robo, int posAnteriorX, int posAnteriorY) {
        // 1. Obstáculo
        Obstaculo obs = tabuleiro.getObstaculoNaPosicao(robo.getEixoX(), robo.getEixoY());
        if (obs != null) {
            obs.bater(robo);
            if (robo.getEixoX() == -1) {
                tabuleiro.removerObstaculo(obs.getId());
                labelStatus.setText("💥 Robô " + robo.getCor() + " destruído pela bomba!");
            } else {
                // Rocha: recua e notifica memória se aplicável
                if (robo instanceof RoboMemoria)
                    ((RoboMemoria) robo).notificarRocha(robo.getEixoX(), robo.getEixoY());
                robo.setEixoX(posAnteriorX);
                robo.setEixoY(posAnteriorY);
                labelStatus.setText("🪨 Robô " + robo.getCor() + " bloqueado pela rocha!");
            }
            return;
        }

        // 2. Colisão entre robôs
        if (tabuleiro.posicaoOcupadaPorRobo(robo.getEixoX(), robo.getEixoY(), robo)) {
            robo.setEixoX(posAnteriorX);
            robo.setEixoY(posAnteriorY);
            labelStatus.setText("🤖 Robô " + robo.getCor() + " colidiu com outro robô!");
        }
    }

    private void executarTurnoAleatorio(List<Robo> robos) {
        String[] direcoes = {"up", "down", "right", "left"};
        for (Robo robo : robos) {
            if (robo.getEixoX() == -1) continue;
            int px = robo.getEixoX(), py = robo.getEixoY();
            try {
                robo.mover(direcoes[random.nextInt(4)]);
                verificarColisoes(robo, px, py);
            } catch (MovimentoInvalidoException e) { /* tenta no próximo turno */ }
        }
    }

    @FXML public void onMoverCima()     { moverSimples("up");    }
    @FXML public void onMoverBaixo()    { moverSimples("down");  }
    @FXML public void onMoverDireita()  { moverSimples("right"); }
    @FXML public void onMoverEsquerda() { moverSimples("left");  }

    private void moverSimples(String direcao) {
        if (tabuleiro.getRobos().isEmpty()) return;
        Robo robo = tabuleiro.getRobos().get(0);
        int px = robo.getEixoX(), py = robo.getEixoY();
        try {
            robo.mover(direcao);
            verificarColisoes(robo, px, py);
            tabuleiro.avancarTurno();
            tabuleiroView.renderizar(tabuleiro);
            if (tabuleiro.algumRoboEncontrouAlimento()) {
                habilitarBotoesMovimento(false);
                encerrar("🏆 Parabéns!\n" + tabuleiro.getPlacar());
            }
        } catch (MovimentoInvalidoException e) {
            labelStatus.setText("⚠ " + e.getMessage());
        }
    }

    @FXML
    public void onVoltarMenu(ActionEvent event) {
        pararTimelineAtiva();
        try {
            FXMLLoader loader = new FXMLLoader(Caminhos.ANCORA.getResource(Caminhos.FXML_MENU));
            Parent raiz = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(raiz, 400, 600));
            stage.setMinWidth(400);
            stage.setMinHeight(600);
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public int[] abrirDialogoPosicao(String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Caminhos.ANCORA.getResource(Caminhos.FXML_DIALOGO));
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
        } catch (IOException e) { e.printStackTrace(); return null; }
    }

    private void encerrar(String mensagem) {
        labelStatus.setText(mensagem);
        pararTimelineAtiva();
        habilitarBotoesMovimento(false);
        EntradaDados.mostrarInfo("Fim de jogo", mensagem);
    }

    private String construirPlacarEstrategico(
            boolean eChegou, boolean eMorreu,
            boolean mChegou, boolean mMorreu) {

        StringBuilder sb = new StringBuilder();
        sb.append("Robô Estratégico (azul): ")
                .append(eChegou ? "✅ Chegou" : eMorreu ? "💥 Destruído" : "❌ Não chegou")
                .append("\n");
        sb.append("Robô Memória (dourado): ")
                .append(mChegou ? "✅ Chegou" : mMorreu ? "💥 Destruído" : "❌ Não chegou")
                .append("\n\n");
        sb.append(tabuleiro.getPlacar());
        return sb.toString();
    }

    private void iniciarTimeline(int intervaloMs, Runnable acao) {
        timelineAtiva = new Timeline(
                new KeyFrame(Duration.millis(intervaloMs), e -> acao.run())
        );
        timelineAtiva.setCycleCount(Timeline.INDEFINITE);
        timelineAtiva.play();
    }

    private void pararTimelineAtiva() {
        if (timelineAtiva != null) { timelineAtiva.stop(); timelineAtiva = null; }
    }

    private void habilitarBotoesMovimento(boolean habilitar) {
        btnCima.setDisable(!habilitar);
        btnBaixo.setDisable(!habilitar);
        btnEsquerda.setDisable(!habilitar);
        btnDireita.setDisable(!habilitar);
    }
}