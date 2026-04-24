package entities;

public class Robo {
    private int eixoX;
    private int eixoY;

    public Robo() {
    }

    public Robo(int eixoY, int eixoX) {
        this.eixoY = 0;
        this.eixoX = 0;
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
}
