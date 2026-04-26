package view;

import model.Tabuleiro;

public class TabuleiroView {

	public void exibirTabuleiro(Tabuleiro tabuleiro) {
	    // 1. Pegamos a representação do tabuleiro (geralmente uma matriz de Strings ou chars)
	    // O seu colega que fez o Tabuleiro.java deve ter um método que retorna a matriz
	    String[][] matriz = tabuleiro.getMatriz(); 

	    System.out.println("\n--- STATUS DO TABULEIRO ---");

	    // 2. Percorremos o eixo Y (de cima para baixo para o 0,0 ser embaixo)
	    for (int i = 4; i >= 0; i--) {
	        System.out.print(i + " | "); // Mostra o índice do eixo Y
	        
	        // 3. Percorremos o eixo X
	        for (int j = 0; j <= 4; j++) {
	            // Imprime o conteúdo daquela célula (Robo, Alimento ou Vazio)
	            System.out.print(matriz[j][i] + "  ");
	        }
	        System.out.println(); // Pula linha
	    }

	    // 4. Desenha a base do gráfico (eixo X)
	    System.out.println("    -----------------");
	    System.out.println("      0  1  2  3  4 (X)");
	}
}