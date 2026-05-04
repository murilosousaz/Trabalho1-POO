package controller.modos;

import model.Alimento;
import model.Bomba;
import model.RoboEstrategico;
import model.RoboMemoria;
import model.Rocha;
import model.Tabuleiro;
import view.EntradaDados;

public class ModoEstrategicoController {

    // Guardamos referências tipadas para acessar
    // métodos específicos (ex: notificarRocha) no Controller
    private RoboEstrategico roboEstrategico;
    private RoboMemoria     roboMemoria;
    private Alimento        alimento;

    public boolean configurar(Tabuleiro tabuleiro) {

        if (EntradaDados.confirmar("Obstáculos", "Deseja adicionar uma Bomba?")) {
            int[] pos = EntradaDados.lerCoordenada("Posição da Bomba 💣");
            if (pos != null)
                tabuleiro.adicionarObstaculo(new Bomba(1, pos[0], pos[1]));
        }

        if (EntradaDados.confirmar("Obstáculos", "Deseja adicionar uma Rocha?")) {
            int[] pos = EntradaDados.lerCoordenada("Posição da Rocha 🪨");
            if (pos != null)
                tabuleiro.adicionarObstaculo(new Rocha(2, pos[0], pos[1]));
        }

        int[] posAlimento = EntradaDados.lerCoordenada("Posição do Alimento 🍎");
        if (posAlimento == null) return false;

        alimento = new Alimento(posAlimento[0], posAlimento[1]);
        tabuleiro.definirAlimento(posAlimento[0], posAlimento[1]);

        roboEstrategico = new RoboEstrategico("blue");
        roboMemoria     = new RoboMemoria("gold");

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