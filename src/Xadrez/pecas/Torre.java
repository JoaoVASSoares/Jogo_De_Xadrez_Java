package Xadrez.pecas;

import Xadrez.Cor;
import Xadrez.PecaDeXadrez;
import tabuleiroJogo.Tabuleiro;

public class Torre extends PecaDeXadrez {
	
	public Torre(Tabuleiro tabuleiro , Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "T";
	}
}
