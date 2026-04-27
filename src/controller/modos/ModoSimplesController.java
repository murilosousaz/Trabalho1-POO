package controller.modos;

import controller.JogoController;
import model.Robo;
import view.EntradaDados;
import exceptions.MovimentoInvalidoException;

public class ModoSimplesController extends JogoController {
    @Override
    public void executar() {
        int fx = EntradaDados.lerInteiro("X do alimento: ");
        int fy = EntradaDados.lerInteiro("Y do alimento: ");
        Robo robo = new Robo("Azul");

        while (!robo.encontrouAlimento(fx, fy)) {
            tabuleiro.atualizar(robo, robo, fx, fy); // Mostra o estado atual
            view.exibirTabuleiro(tabuleiro);
            
            String dir = EntradaDados.lerString("Mova o robô (up, down, left, right): ");
            try {
                robo.mover(dir);
            } catch (MovimentoInvalidoException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Parabéns! O robô azul achou a comida.");
    }
}