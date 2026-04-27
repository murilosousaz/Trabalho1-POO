package view;
import java.util.Scanner;

public class EntradaDados {

	public static int lerInteiro(String mensagem) {
	    System.out.print(mensagem);
	    Scanner sc = new Scanner(System.in);
	    return sc.nextInt();
	}

	public static String lerString(String mensagem) {
	    Scanner leitor = new Scanner(System.in);
	    
	    // Exibimos a mensagem que você passou por parâmetro (ex: "Digite a direção: ")
	    System.out.print(mensagem);
	    
	    // Lemos a linha inteira que o usuário digitar
	    String entrada = leitor.nextLine();
	    
	    // Retornamos o texto para quem chamou o método (o Controller)
	    return entrada;
	}
}