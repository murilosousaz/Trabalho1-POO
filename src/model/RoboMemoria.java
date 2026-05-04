package model;

import exceptions.MovimentoInvalidoException;

public class RoboMemoria extends Robo{
    private final EstrategiaMemoria estrategiaMemoria;

    public RoboMemoria(String cor) {
        super(cor);
        this.estrategiaMemoria = new EstrategiaMemoria();
        setEstrategia(estrategiaMemoria);
    }

    public String moverComMemoria(Alimento alimento, Tabuleiro tabuleiro)
            throws MovimentoInvalidoException {
        return moverComEstrategia(alimento, tabuleiro);
    }

    public void notificarRocha(int x, int y) {
        estrategiaMemoria.notificarRocha(x, y);
    }

    public EstrategiaMemoria getEstrategiaMemoria() {
        return estrategiaMemoria;
    }


}
