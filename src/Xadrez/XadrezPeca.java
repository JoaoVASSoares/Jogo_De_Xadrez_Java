package Xadrez;

import tabuleiroJogo.Peca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;

public abstract class XadrezPeca extends Peca {
	
	private Cor cor;
	private int contadorMovimento;

	public XadrezPeca(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}
	
	public int getContadorMovimento() {
		return contadorMovimento;
	}
	public void acrentarContadorMovimento() {
		contadorMovimento++;
	}
	public void diminuirContadorMovimento() {
		contadorMovimento--;
	}
	public XadrezPosicao getXadrezPosicao() {
		return XadrezPosicao.dePosicao(posicao);
	}
	protected boolean existePecaAdversaria(Posicao posicao) {
		XadrezPeca p = (XadrezPeca)getTabuleiro().peca(posicao);
		return p != null && p.getCor() != cor;
 	}
}
