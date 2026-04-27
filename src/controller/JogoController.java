package controller;

import model.Tabuleiro;
import view.TabuleiroView;

public abstract class JogoController {
    protected Tabuleiro tabuleiro;
    protected TabuleiroView view;

    public JogoController() {
        this.tabuleiro = new Tabuleiro(); 
        this.view = new TabuleiroView();
    }

 // Este método será implementado obrigatoriamente nos arquivos da pasta 'modos'
    public abstract void executar();

    // Método comum para atualizar a tela após cada movimento
    protected void atualizarTela() {
        view.exibirTabuleiro(tabuleiro);
        try {
            Thread.sleep(500); // Pausa para o usuário ver o movimento
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}