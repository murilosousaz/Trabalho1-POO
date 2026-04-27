package controller.modos;

import controller.JogoController;
import model.Robo;
import model.RoboInteligente;
import view.EntradaDados;
import java.util.Random;

public class ModoInteligenteController extends JogoController {
    @Override
    public void executar() {
        int fx = EntradaDados.lerInteiro("X do alimento: ");
        int fy = EntradaDados.lerInteiro("Y do alimento: ");
        Robo normal = new Robo("Normal");
        RoboInteligente smart = new RoboInteligente("Inteligente", null);
        Random rand = new Random();

        while (!normal.encontrouAlimento(fx, fy) || !smart.encontrouAlimento(fx, fy)) {
            if (!normal.encontrouAlimento(fx, fy)) {
                try { normal.mover(rand.nextInt(4) + 1); } catch (Exception e) {}
            }
            if (!smart.encontrouAlimento(fx, fy)) {
                smart.mover("up"); // O mover do inteligente já trata erros internamente
            }
            tabuleiro.atualizar(normal, smart, fx, fy);
            view.exibirTabuleiro(tabuleiro);
        }
    }
}