package model;

import exceptions.MovimentoInvalidoException;

public class Robo {
    private int eixoX;
    private int eixoY;
    private String cor;

    public Robo() {
    }

    public Robo(String cor) {
        this.cor = cor;
        this.eixoY = 0;
        this.eixoX = 0;
    }

    public String getCor() {
        return cor;
    }

    public int getEixoX() {
        return eixoX;
    }

    public void setEixoX(int eixoX) {
        this.eixoX = eixoX;
    }

    public int getEixoY() {
        return eixoY;
    }

    public void setEixoY(int eixoY) {
        this.eixoY = eixoY;
    }

    public void mover(String direcao) throws MovimentoInvalidoException {
        int novoX = this.eixoX;
        int novoY = this.eixoY;

        switch (direcao.toLowerCase()) {
            case "up":    novoY++; break;
            case "down":  novoY--; break;
            case "right": novoX++; break;
            case "left":  novoX--; break;
            default:
                throw new MovimentoInvalidoException("Comando " + direcao + " desconhecido");
        }

        validarPosicao(novoX, novoY, direcao);

        this.eixoX = novoX;
        this.eixoY = novoY;
        System.out.println("O robô " + cor + " moveu-se para (" + eixoX + ", " + eixoY + ")");
    }

    public void mover(int direcao) throws MovimentoInvalidoException {
        switch (direcao) {
            case 1: mover("up");    break;
            case 2: mover("down");  break;
            case 3: mover("right"); break;
            case 4: mover("left");  break;
            default:
                throw new MovimentoInvalidoException("Código " + direcao + " é inválido (use 1 a 4)");
        }
    }

    public boolean encontrouAlimento(int alimentoX, int alimentoY) {
        return (this.eixoX == alimentoX && this.eixoY == alimentoY);
    }

    public void validarPosicao(int x, int y, String direcao) throws MovimentoInvalidoException {
        int limite = Tabuleiro.TAMANHO - 1;
        if (x < 0 || y < 0 || x > limite || y > limite) {
            throw new MovimentoInvalidoException(
                    "Movimento inválido para " + direcao +
                            ": posição (" + x + ", " + y + ") fora do tabuleiro"
            );
        }
    }
}