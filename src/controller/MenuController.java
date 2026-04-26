package controller;

import java.util.Scanner;
import view.EntradaDados; 
import controller.modos.ModoSimplesController;
import controller.modos.ModoCompetitivoController;
import controller.modos.ModoInteligenteController;
import controller.modos.ModoObstaculosController;

public class MenuController {
    // Poode-se remover o Scanner daqui se usar apenas o EntradaDados
    
    public void iniciarMenu() {
        boolean sair = false;

        while (!sair) {
            System.out.println("=== JOGO DO ROBÔ (LOGO) ===");
            System.out.println("1. Modo Simples");
            System.out.println("2. Modo Competitivo");
            System.out.println("3. Modo Inteligente");
            System.out.println("4. Modo Obstáculos");
            System.out.println("0. Sair");

            // Certifique-se que lerInteiro existe em EntradaDados!
            int opcao = EntradaDados.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> new ModoSimplesController().executar();
                case 2 -> new ModoCompetitivoController().executar();
                case 3 -> new ModoInteligenteController().executar();
                case 4 -> new ModoObstaculosController().executar();
                case 0 -> sair = true;
                default -> System.out.println("Opção inválida!");
            }
        }
    }
}