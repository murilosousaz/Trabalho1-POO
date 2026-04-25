package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    public static final int TAMANHO = 4;
    private int alimentoX;
    private int alimentoY;
    private List<Robo> robos;
    private List<Obstaculo> obstaculos;
    private final IntegerProperty turnoAtual;

    public Tabuleiro(IntegerProperty turnoAtual, List<Obstaculo> obstaculos, List<Robo> robos, int alimentoY, int alimentoX) {
        this.turnoAtual = new SimpleIntegerProperty(0);
        this.obstaculos = new ArrayList<>();
        this.robos = new ArrayList<>();
        this.alimentoY = -1;
        this.alimentoX = -1;
    }

    public void adicionarRobo(Robo robo){
        this.robos.add(robo);
    }

    public void adicionarObstaculo(Obstaculo obstaculo){
        this.obstaculos.add(obstaculo);
    }

    public void removerObstcaulo(int id){
        obstaculos.removeIf(o -> o.getId() == id);
    }

    public void definirAlimento(int x, int y){
        if(!posicaoValida(x,y)){
            throw new IllegalArgumentException(
                    "Posição do alimento fora do tabuleiro: (" + x + "," + y + ")"
            );
        }
        this.alimentoX = x;
        this.alimentoY = y;
    }

    public void avancarTurno(){
        turnoAtual.set(turnoAtual.get() + 1);
    }

    public void resetar(){
        this.robos.clear();
        this.obstaculos.clear();
        this.turnoAtual.set(0);
        this.alimentoX = -1;
        this.alimentoY = -1;
    }

    public boolean posicaoValida(int x, int y){
        return x >= 0 && x <= TAMANHO - 1
                && y >= 0 && y <= TAMANHO - 1;
    }

    public Obstaculo getObstaculoNaPosicao(int x, int y){
        return obstaculos.stream().filter(o -> o.getEixoX() == x && o.getEixoY() == y).findFirst().orElse(null);
    }

    public boolean todosRobosDestruidos(){
        return robos.stream().allMatch(r -> r.getEixoX() == -1 && r.getEixoY() == -1);
    }

    public boolean algumRoboEncontrouAlimento(){
        return robos.stream().filter(r -> r.getEixoX() != -1).anyMatch(r -> r.encontrouAlimento(alimentoX, alimentoY));
    }

    public int getAlimentoX() {
        return alimentoX;
    }

    public int getAlimentoY() {
        return alimentoY;
    }

    public List<Robo> getRobos() {
        return robos;
    }

    public List<Obstaculo> getObstaculos() {
        return obstaculos;
    }

    public int getTurnoAtual() {
        return turnoAtual.get();
    }

    public IntegerProperty turnoAtualProperty() {
        return turnoAtual;
    }
}
