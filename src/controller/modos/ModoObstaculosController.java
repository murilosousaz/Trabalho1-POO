package controller.modos;

import controller.JogoController;
import model.*;
import view.EntradaDados;
import java.util.Random;

public class ModoObstaculosController extends JogoController {
    @Override
    public void executar() {
        // Aqui você adicionaria a lógica para posicionar Bomba e Rocha
        // e verificaria a cada passo:
        // if (robo.getX() == bomba.getX() && robo.getY() == bomba.getY()) bomba.bater(robo);
        System.out.println("Modo obstáculos iniciado...");
        // Implementar lógica de colisão similar aos exemplos anteriores
    }
}