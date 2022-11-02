package Xadrez.pecas;

import Xadrez.Cor;
import Xadrez.XadrezPeca;
import tabuleiroJogo.Tabuleiro;

public class Torre extends XadrezPeca {
	
	public Torre(Tabuleiro tabuleiro , Cor cor) {
		super(tabuleiro, cor);
	}
	
	@Override
	public String toString() {
		return "T";
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		return mat;
	}
}
