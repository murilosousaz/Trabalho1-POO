package model;

public abstract class Obstaculo {
    private int id;
    private int eixoX;
    private int eixoY;

    public Obstaculo(int id, int eixoX, int eixoY) {
        this.id = id;
        this.eixoX = eixoX;
        this.eixoY = eixoY;
    }

    public abstract void bater(Robo robo);

    public int getId() {
        return id;
    }

    public int getEixoX() {
        return eixoX;
    }

    public int getEixoY() {
        return eixoY;
    }
}
