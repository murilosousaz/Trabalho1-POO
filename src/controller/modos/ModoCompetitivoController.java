package controller.modos;

import controller.JogoController;
import model.Robo;
import view.EntradaDados;
import java.util.Random;

public class ModoCompetitivoController extends JogoController {
    @Override
    public void executar() {
        int fx = EntradaDados.lerInteiro("X do alimento: ");
        int fy = EntradaDados.lerInteiro("Y do alimento: ");
        Robo r1 = new Robo("Vermelho");
        Robo r2 = new Robo("Verde");
        Random rand = new Random();

        while (!r1.encontrouAlimento(fx, fy) && !r2.encontrouAlimento(fx, fy)) {
            try { r1.mover(rand.nextInt(4) + 1); } catch (Exception e) {}
            try { r2.mover(rand.nextInt(4) + 1); } catch (Exception e) {}
            
            tabuleiro.atualizar(r1, r2, fx, fy); 
            view.exibirTabuleiro(tabuleiro);
        }
        
        String vencedor = r1.encontrouAlimento(fx, fy) ? "Vermelho" : "Verde";
        System.out.println("O vencedor foi o Robô " + vencedor);
    }
}