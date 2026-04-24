package model;

import exceptions.MovimentoInvalidoException;
import java.util.Random;

public class RoboInteligente extends Robo {

    private Random random;

    public RoboInteligente(String cor, Random random) {
        super(cor);
        this.random = new Random();
    }

    @Override
    public void mover(String direcao){
        boolean movido = false;
        String direcaoTentativa = direcao;

        while(!movido){
            try {
                super.mover(direcaoTentativa);
                movido = true;
            }catch (MovimentoInvalidoException e){
                System.out.println("Robo Inteligente detectou erro: " + e.getMessage() + "Tentando nova direção");

                direcaoTentativa = gerarDirecaoAleatoria();
            }
        }
    }

    private String gerarDirecaoAleatoria(){
        int opcao = random.nextInt(4) + 1;
        switch (opcao){
            case 1: return "up";
            case 2: return "down";
            case 3: return "right";
            case 4: return  "left";
            default: return "up";
        }
    }
}
