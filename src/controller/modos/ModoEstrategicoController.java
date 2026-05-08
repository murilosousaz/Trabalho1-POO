package controller.modos;

import model.Alimento;
import model.Bomba;
import model.RoboEstrategico;
import model.RoboMemoria;
import model.Rocha;
import model.Tabuleiro;
import view.EntradaDados;

public class ModoEstrategicoController {

    private RoboEstrategico roboEstrategico;
    private RoboMemoria     roboMemoria;
    private Alimento        alimento;

    public boolean configurar(Tabuleiro tabuleiro) {
        int[] pos = EntradaDados.lerCoordenada("Posição do Alimento");
        if (pos == null) return false;

        tabuleiro.definirAlimento(pos[0], pos[1]);

        roboEstrategico = new RoboEstrategico("blue");
        roboMemoria     = new RoboMemoria("red");

        tabuleiro.adicionarRobo(roboEstrategico);
        tabuleiro.adicionarRobo(roboMemoria);
        return true;

    }

    public RoboEstrategico getRoboEstrategico() { return roboEstrategico; }
    public RoboMemoria     getRoboMemoria()     { return roboMemoria; }
    public Alimento        getAlimento()        { return alimento; }

    public String getNome()       { return "Modo: Estratégico"; }
    public String getDescricao()  { return "Estratégico vs Memória — ambos buscam o alimento!"; }
    public boolean isModoManual() { return false; }
    public int getIntervaloMs()   { return 900; }
}