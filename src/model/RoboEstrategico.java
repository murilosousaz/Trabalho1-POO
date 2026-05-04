package model;

import exceptions.MovimentoInvalidoException;

public class RoboEstrategico extends Robo{
    public RoboEstrategico(String cor) {
        super(cor, new EstrategiaEstrategica());
    }
    public String moverEmDirecaoAo(Alimento alimento, Tabuleiro tabuleiro)
            throws MovimentoInvalidoException {
        return moverComEstrategia(alimento, tabuleiro);
    }

}
