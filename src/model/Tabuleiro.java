package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    public static final int TAMANHO = 5; // Ajustado para 5 para cobrir de 0 a 4
    private int alimentoX = -1;
    private int alimentoY = -1;
    private List<Robo> robos;
    private List<Obstaculo> obstaculos;
    private final IntegerProperty turnoAtual;

    // Construtor simplificado (removidos parâmetros redundantes que eram sobrescritos)
    public Tabuleiro() {
        this.turnoAtual = new SimpleIntegerProperty(0);
        this.obstaculos = new ArrayList<>();
        this.robos = new ArrayList<>();
    }

    public void adicionarRobo(Robo robo){
        this.robos.add(robo);
    }

    public void adicionarObstaculo(Obstaculo obstaculo){
        this.obstaculos.add(obstaculo);
    }

    public void removerObstaculo(int id){
        obstaculos.removeIf(o -> o.getId() == id);
    }

    public void definirAlimento(int x, int y){
        if(!posicaoValida(x,y)){
            throw new IllegalArgumentException("Posição fora do tabuleiro: (" + x + "," + y + ")");
        }
        this.alimentoX = x;
        this.alimentoY = y;
    }

    // Método solicitado pelo Controller para atualizar o estado do jogo
    public void atualizar(Robo r1, Robo r2, int fx, int fy) {
        this.robos.clear();
        if (r1 != null) this.robos.add(r1);
        if (r2 != null) this.robos.add(r2);
        definirAlimento(fx, fy);
        avancarTurno();
    }

    // Método essencial para a TabuleiroView desenhar o jogo
    public String[][] getMatriz() {
        String[][] matriz = new String[TAMANHO][TAMANHO];

        // Preenche com vazio
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                matriz[i][j] = ".";
            }
        }

        // Coloca o Alimento
        if (alimentoX != -1 && alimentoY != -1) {
            matriz[alimentoX][alimentoY] = "A";
        }

        // Coloca os Obstáculos
        for (Obstaculo o : obstaculos) {
            matriz[o.getEixoX()][o.getEixoY()] = (o instanceof Bomba) ? "B" : "R";
        }

        // Coloca os Robôs (por último para ficarem "por cima")
        for (Robo r : robos) {
            if (r.getEixoX() != -1 && r.getEixoY() != -1) {
                matriz[r.getEixoX()][r.getEixoY()] = "R";
            }
        }
        return matriz;
    }

    public void avancarTurno(){
        turnoAtual.set(turnoAtual.get() + 1);
    }

    public boolean posicaoValida(int x, int y){
        return x >= 0 && x < TAMANHO && y >= 0 && y < TAMANHO;
    }

    public int getAlimentoX() { return alimentoX; }
    public int getAlimentoY() { return alimentoY; }
    public List<Robo> getRobos() { return robos; }
    public int getTurnoAtual() { return turnoAtual.get(); }
}