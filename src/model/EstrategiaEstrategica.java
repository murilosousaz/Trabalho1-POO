package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EstrategiaEstrategica {
    private final Random random = new Random();

    @Override
    public String escolherMovimento(Robo robo, Alimento alimento, Tabuleiro tabuleiro) {
        int rx = robo.getEixoX();
        int ry = robo.getEixoY();
        int ax = alimento.getEixoX();
        int ay = alimento.getEixoY();

        // Candidatas: direções que reduzem a distância
        List<String> melhores = new ArrayList<>();
        int distAtual = Math.abs(ax - rx) + Math.abs(ay - ry);

        String[] direcoes = {"up", "down", "right", "left"};
        int[]    dxs      = { 0,    0,      1,       -1};
        int[]    dys      = { 1,   -1,      0,        0};

        for (int i = 0; i < 4; i++) {
            int nx = rx + dxs[i];
            int ny = ry + dys[i];

            // Só considera posições dentro do tabuleiro
            if (!tabuleiro.posicaoValida(nx, ny)) continue;

            int novaDist = Math.abs(ax - nx) + Math.abs(ay - ny);
            if (novaDist < distAtual) {
                melhores.add(direcoes[i]);
            }
        }

        // Se nenhuma direção melhora (não deveria acontecer
        // num tabuleiro livre), sorteia entre as válidas
        if (melhores.isEmpty()) {
            List<String> validas = new ArrayList<>(Arrays.asList(direcoes));
            validas.removeIf(d -> {
                int nx = rx + (d.equals("right") ? 1 : d.equals("left") ? -1 : 0);
                int ny = ry + (d.equals("up")    ? 1 : d.equals("down") ? -1 : 0);
                return !tabuleiro.posicaoValida(nx, ny);
            });
            return validas.isEmpty() ? "up" : validas.get(random.nextInt(validas.size()));
        }

        // Se houver empate, sorteia entre as melhores
        return melhores.get(random.nextInt(melhores.size()));
    }

}
