package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EstrategiaMemoria {
    private final Random random = new Random();
    private final List<String> visitadas  = new ArrayList<>(); // "x,y"
    private final List<String> bloqueadas = new ArrayList<>(); // "x,y"

    @Override
    public String escolherMovimento(Robo robo, Alimento alimento, Tabuleiro tabuleiro) {
        int rx = robo.getEixoX();
        int ry = robo.getEixoY();

        // Registra posição atual como visitada
        String posAtual = rx + "," + ry;
        if (!visitadas.contains(posAtual)) visitadas.add(posAtual);

        String[] dirs = {"up", "down", "right", "left"};
        int[]    dxs  = { 0,    0,      1,       -1};
        int[]    dys  = { 1,   -1,      0,        0};

        List<String> naoVisitadas  = new ArrayList<>();
        List<String> naoBloqueadas = new ArrayList<>();
        List<String> qualquerValida = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int nx = rx + dxs[i];
            int ny = ry + dys[i];
            if (!tabuleiro.posicaoValida(nx, ny)) continue;

            String proxPos = nx + "," + ny;
            qualquerValida.add(dirs[i]);

            if (!bloqueadas.contains(proxPos)) {
                naoBloqueadas.add(dirs[i]);
                if (!visitadas.contains(proxPos)) {
                    naoVisitadas.add(dirs[i]);
                }
            }
        }

        // Escolhe pela melhor categoria disponível
        if (!naoVisitadas.isEmpty())
            return naoVisitadas.get(random.nextInt(naoVisitadas.size()));
        if (!naoBloqueadas.isEmpty())
            return naoBloqueadas.get(random.nextInt(naoBloqueadas.size()));
        if (!qualquerValida.isEmpty())
            return qualquerValida.get(random.nextInt(qualquerValida.size()));
        return "up";
    }

    // Chamado pelo JogoController ao detectar colisão com Rocha
    public void notificarRocha(int x, int y) {
        String pos = x + "," + y;
        if (!bloqueadas.contains(pos)) {
            bloqueadas.add(pos);
            System.out.println("[Memória] Posição (" + x + "," + y + ") marcada como bloqueada.");
        }
    }

    public List<String> getVisitadas()  { return visitadas; }
    public List<String> getBloqueadas() { return bloqueadas; }
}
