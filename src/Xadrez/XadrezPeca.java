package Xadrez;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Tabuleiro;

public abstract class XadrezPeca extends Peca {
	
	private Cor cor;

	public XadrezPeca(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
}
