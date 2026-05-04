package model;

import exceptions.MovimentoInvalidoException;

public class Robo {
    private int eixoX;
    private int eixoY;
    private String cor;
    private int movimentosValidos   = 0;
    private int movimentosInvalidos = 0;

    private EstrategiaMovimento estrategia;

    public Robo() {}

    public Robo(String cor) {
        this.cor   = cor;
        this.eixoX = 0;
        this.eixoY = 0;
    }

    public Robo(String cor, EstrategiaMovimento estrategia) {
        this(cor);
        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaMovimento estrategia) {
        this.estrategia = estrategia;
    }

    public EstrategiaMovimento getEstrategia() { return estrategia; }

    public String moverComEstrategia(Alimento alimento, Tabuleiro tabuleiro)
            throws MovimentoInvalidoException {
        if (estrategia == null)
            throw new IllegalStateException("Robô " + cor + " não tem estratégia definida.");
        String direcao = estrategia.escolherMovimento(this, alimento, tabuleiro);
        mover(direcao);
        return direcao;
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
                movimentosInvalidos++;
                throw new MovimentoInvalidoException("Comando " + direcao + " desconhecido");
        }

        try {
            validarPosicao(novoX, novoY, direcao);
        } catch (MovimentoInvalidoException e) {
            movimentosInvalidos++;
            throw e;
        }

        this.eixoX = novoX;
        this.eixoY = novoY;
        movimentosValidos++;
        System.out.println("O robô " + cor + " moveu-se para (" + eixoX + ", " + eixoY + ")");
    }

    public void mover(int direcao) throws MovimentoInvalidoException {
        switch (direcao) {
            case 1: mover("up");    break;
            case 2: mover("down");  break;
            case 3: mover("right"); break;
            case 4: mover("left");  break;
            default:
                movimentosInvalidos++;
                throw new MovimentoInvalidoException("Código " + direcao + " é inválido");
        }
    }

    public void validarPosicao(int x, int y, String direcao) throws MovimentoInvalidoException {
        int limite = Tabuleiro.TAMANHO - 1;
        if (x < 0 || y < 0 || x > limite || y > limite)
            throw new MovimentoInvalidoException(
                    "Movimento inválido para " + direcao +
                            ": posição (" + x + ", " + y + ") fora do tabuleiro"
            );
    }

    public boolean encontrouAlimento(int alimentoX, int alimentoY) {
        return this.eixoX == alimentoX && this.eixoY == alimentoY;
    }

    public String getResumoMovimentos() {
        return "Robô " + cor
                + " → válidos: "   + movimentosValidos
                + " | inválidos: " + movimentosInvalidos;
    }

    public String getCor()                { return cor; }
    public int getEixoX()                 { return eixoX; }
    public int getEixoY()                 { return eixoY; }
    public void setEixoX(int x)           { this.eixoX = x; }
    public void setEixoY(int y)           { this.eixoY = y; }
    public int getMovimentosValidos()     { return movimentosValidos; }
    public int getMovimentosInvalidos()   { return movimentosInvalidos; }
}