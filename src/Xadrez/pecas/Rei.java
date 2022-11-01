package Xadrez.pecas;

import Xadrez.Cor;
import Xadrez.XadrezPeca;
import tabuleiroJogo.Tabuleiro;

public class Rei extends XadrezPeca {

	public Rei(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "R";
	}

}
