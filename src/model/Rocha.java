package model;

public class Rocha extends Obstaculo {
    public Rocha(int id, int eixoX, int eixoY) {
        super(id, eixoX, eixoY);
    }

    @Override
    public void bater(Robo robo){
        System.out.println("EXPLOSÃO!!!");
        System.out.println("O robo" + robo.getCor() + "atingiu a bomba");
        System.out.println("Robo destruido e não pode mais se mover");

        robo.setEixoX(-1);
        robo.setEixoY(-1);
    }
}
