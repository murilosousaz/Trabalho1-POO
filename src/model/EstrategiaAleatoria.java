package model;

import java.util.Random;

public class EstrategiaAleatoria {
    private final Random random = new Random();
    private String ultimaDirecaoInvalida = null;

    @Override
    public String escolherMovimento(Robo robo, Alimento alimento, Tabuleiro tabuleiro) {
        String[] todas = {"up", "down", "right", "left"};
        String escolha;
        int tentativas = 0;

        do {
            escolha = todas[random.nextInt(4)];
            tentativas++;
        } while (escolha.equals(ultimaDirecaoInvalida) && tentativas < 8);

        return escolha;
    }

    public void registrarInvalida(String direcao) {
        this.ultimaDirecaoInvalida = direcao;
    }

}
