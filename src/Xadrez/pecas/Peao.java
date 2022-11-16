package Xadrez.pecas;

import Xadrez.Cor;
import Xadrez.XadrezPartida;
import Xadrez.XadrezPeca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;

public class Peao extends XadrezPeca{
	
	private XadrezPartida xadrezPartida;

	public Peao(Tabuleiro tabuleiro, Cor cor, XadrezPartida xadrezPartida) {
		super(tabuleiro, cor);
		this.xadrezPartida = xadrezPartida;
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];
		
		Posicao p = new Posicao(0,0);
		
		if(getCor() == Cor.BRANCO) {
			p.setValores(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().temUmaPeca(p2) && getContadorMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if(getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if(getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			// #movimento especial en passant Branco
			if(posicao.getLinha() == 3) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1 );
				if(getTabuleiro().posicaoExiste(esquerda) && existePecaAdversaria(esquerda) && getTabuleiro().peca(esquerda) == xadrezPartida.getEnPassantVunerabilidade()) {
					mat[esquerda.getLinha() - 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1 );
				if(getTabuleiro().posicaoExiste(direita) && existePecaAdversaria(direita) && getTabuleiro().peca(direita) == xadrezPartida.getEnPassantVunerabilidade()) {
					mat[direita.getLinha() - 1][direita.getColuna()] = true;
				}
			}
		}
		else {
			p.setValores(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if(getTabuleiro().posicaoExiste(p) && !getTabuleiro().temUmaPeca(p) && getTabuleiro().posicaoExiste(p2) && !getTabuleiro().temUmaPeca(p2) && getContadorMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if(getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValores(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if(getTabuleiro().posicaoExiste(p) && existePecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			
			// #movimento especial en passant Preta
			if(posicao.getLinha() == 4) {
				Posicao esquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1 );
				if(getTabuleiro().posicaoExiste(esquerda) && existePecaAdversaria(esquerda) && getTabuleiro().peca(esquerda) == xadrezPartida.getEnPassantVunerabilidade()) {
					mat[esquerda.getLinha() + 1][esquerda.getColuna()] = true;
				}
				Posicao direita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1 );
				if(getTabuleiro().posicaoExiste(direita) && existePecaAdversaria(direita) && getTabuleiro().peca(direita) == xadrezPartida.getEnPassantVunerabilidade()) {
					mat[direita.getLinha() + 1][direita.getColuna()] = true;
				}
			}
		}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}
	
}
