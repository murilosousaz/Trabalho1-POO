package model;

public class Rocha extends Obstaculo {
    public Rocha(int id, int eixoX, int eixoY) {
        super(id, eixoX, eixoY);
    }

    @Override
    public void bater(Robo robo) {
        System.out.println(">>> OBSTÁCULO: ROCHA <<<");
        System.out.println("O robô " + robo.getCor() + " colidiu com uma rocha no ID: " + getId());
        System.out.println("Movimento bloqueado! O robô deve retornar.");
    }
}
