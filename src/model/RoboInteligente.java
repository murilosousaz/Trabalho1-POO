package model;

import exceptions.MovimentoInvalidoException;
import java.util.Random;

public class RoboInteligente extends Robo {

    private Random random;

    public RoboInteligente(String cor, Random random) {
        super(cor);
        this.random = (random != null) ? random : new Random();
    }

    @Override
    public void mover(String direcao) {
        boolean movido = false;
        String direcaoTentativa = direcao;

        int tentativas = 0;
        while (!movido && tentativas < 8) {
            try {
                super.mover(direcaoTentativa);
                movido = true;
            } catch (MovimentoInvalidoException e) {
                System.out.println("Robô Inteligente detectou erro: "
                        + e.getMessage() + " — Tentando nova direção");
                direcaoTentativa = gerarDirecaoAleatoria();
                tentativas++;
            }
        }

        if (!movido) {
            System.out.println("Robô Inteligente não encontrou direção válida neste turno.");
        }
    }

    private String gerarDirecaoAleatoria() {
        switch (random.nextInt(4) + 1) {
            case 1: return "up";
            case 2: return "down";
            case 3: return "right";
            case 4: return "left";
            default: return "up";
        }
    }
}