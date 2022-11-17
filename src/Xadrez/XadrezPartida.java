package Xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Xadrez.pecas.Bispo;
import Xadrez.pecas.Cavalo;
import Xadrez.pecas.Peao;
import Xadrez.pecas.Rainha;
import Xadrez.pecas.Rei;
import Xadrez.pecas.Torre;
import tabuleiroJogo.Peca;
import tabuleiroJogo.Posicao;
import tabuleiroJogo.Tabuleiro;

public class XadrezPartida  {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private XadrezPeca enPassantVunerabilidade;
	private XadrezPeca promoted;
	
	private List<Peca> pecaNoTabuleiro = new ArrayList<>();
	private List<Peca> pecaCapturadas = new ArrayList<>();
	
	public XadrezPartida() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.BRANCO;
		setupInicial();
	}
	
	public int getTurno(){
		return turno;
	}
	
	public Cor getJogadorAtual() {
		return jogadorAtual;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public XadrezPeca getEnPassantVunerabilidade(){
		return enPassantVunerabilidade;
	}
	
	public XadrezPeca getPromoted() {
		return promoted;
	}
	
	public XadrezPeca[][] getPecas(){
		XadrezPeca[][] mat = new XadrezPeca[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for(int i = 0; i<tabuleiro.getLinhas();i++) {
			for(int j=0; j<tabuleiro.getColunas(); j++) {
				mat[i][j] = (XadrezPeca) tabuleiro.peca(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] movimentoPossivel(XadrezPosicao posicaoOrigem){
		Posicao posicao = posicaoOrigem.toPosicao();
		validacaoPosicaoOrigem(posicao);
		return tabuleiro.peca(posicao).movimentosPossiveis();
	}
	
	public XadrezPeca performaMovimentoXadrez(XadrezPosicao destinoInicial, XadrezPosicao destinoFinal) {
		Posicao origem = destinoInicial.toPosicao();
		Posicao destino = destinoFinal.toPosicao(); 
		validacaoPosicaoOrigem(origem);
		validacaoPosicaoDestino(origem,destino);
		Peca capturaDePeca = fazerMovimento(origem, destino);
		
		if(testeCheck(jogadorAtual)) {
			desfazerMovimento(origem, destino, capturaDePeca);
			throw new XadrezExcecao("Você não pode se colcar em check");
		}
		
		XadrezPeca pesaQueMoveu = (XadrezPeca)tabuleiro.peca(destino);
		
		// #movimento especial promotion
		promoted = null;
		if(pesaQueMoveu instanceof Peao) {
			if((pesaQueMoveu.getCor() == Cor.BRANCO && destino.getLinha() == 0) ||(pesaQueMoveu.getCor() == Cor.PRETO && destino.getLinha() == 7)) {
				promoted = (XadrezPeca)tabuleiro.peca(destino);
				promoted = substituirPecaPromovida("Q");
				
				
			}
		}
		
		check = (testeCheck(oponente(jogadorAtual))) ? true : false;
		
		if(testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			proximoTurno();
		}
		
		// #movimento especial en passanet
		if(pesaQueMoveu instanceof Peao && (destino.getLinha() == origem.getLinha() - 2 || destino.getLinha() == origem.getLinha() + 2)) {
			enPassantVunerabilidade = pesaQueMoveu;
		}
		else {
			enPassantVunerabilidade = null;
		}
		
		return (XadrezPeca)capturaDePeca;
	}
	
	public XadrezPeca substituirPecaPromovida(String type) {
		if(promoted == null) {
			throw new IllegalStateException("Não tem peça para ser promovida");
		}
		if(!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("Q")) {
			return promoted;
		}
		
		Posicao pos = promoted.getXadrezPosicao().toPosicao();
		Peca p = tabuleiro.removerPeca(pos);
		pecaNoTabuleiro.remove(p);
		
		XadrezPeca novaPeca = novaPeca(type,promoted.getCor());
		tabuleiro.posicaoPeca(novaPeca, pos);
		pecaNoTabuleiro.add(novaPeca);
		
		return novaPeca;
	}
	
	private XadrezPeca novaPeca(String type, Cor cor) {
		if(type.equals("B")) return new Bispo(tabuleiro, cor);
		if(type.equals("C")) return new Cavalo(tabuleiro, cor);
		if(type.equals("Q")) return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
	}
	
	private Peca fazerMovimento(Posicao origem, Posicao destino) {
		XadrezPeca p = (XadrezPeca)tabuleiro.removerPeca(origem);
		p.acrentarContadorMovimento();
		Peca pecaCapturada = tabuleiro.removerPeca(destino);
		tabuleiro.posicaoPeca(p, destino);
		
		if(pecaCapturada != null) {
			pecaNoTabuleiro.remove(pecaCapturada);
			pecaCapturadas.add(pecaCapturada);
		}
		
		// #movimentoespecial roque pequeno
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(origemT);
			tabuleiro.posicaoPeca(torre, destinoT);
			torre.acrentarContadorMovimento();
		}
		
		// #movimentoespecial roque grande
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(origemT);
			tabuleiro.posicaoPeca(torre, destinoT);
			torre.acrentarContadorMovimento();
		}
		
		// #movimentoespecial en passant
		if(p instanceof Peao) {
			if(origem.getColuna() != destino.getColuna() && pecaCapturada == null) {
				Posicao posicaoPeao;
				if(p.getCor() == Cor.BRANCO) {
					posicaoPeao = new Posicao(destino.getLinha() + 1, destino.getColuna());
				}
				else {
					posicaoPeao = new Posicao(destino.getLinha() - 1, destino.getColuna());
				}
				pecaCapturada = tabuleiro.removerPeca(posicaoPeao);
				pecaCapturadas.add(pecaCapturada);
				pecaNoTabuleiro.remove(pecaCapturada);
			}
		}
		
		
		return pecaCapturada;
	}
	
	public void desfazerMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		XadrezPeca p = (XadrezPeca)tabuleiro.removerPeca(destino);
		p.diminuirContadorMovimento();
		tabuleiro.posicaoPeca(p, origem);
		
		if(pecaCapturada != null) {
			tabuleiro.posicaoPeca(pecaCapturada, destino);
			pecaCapturadas.remove(pecaCapturada);
			pecaNoTabuleiro.add(pecaCapturada);
		}

		// #movimentoespecial roque pequeno
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(destinoT);
			tabuleiro.posicaoPeca(torre, origemT);
			torre.diminuirContadorMovimento();
		}
		
		// #movimentoespecial roque grande
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(destinoT);
			tabuleiro.posicaoPeca(torre, origemT);
			torre.diminuirContadorMovimento();
		}
		
		// #movimentoespecial en passant
		if(p instanceof Peao) {
			if(origem.getColuna() != destino.getColuna() && pecaCapturada == enPassantVunerabilidade) {
				XadrezPeca peao = (XadrezPeca)tabuleiro.removerPeca(destino);
				Posicao posicaoPeao;
				if(p.getCor() == Cor.BRANCO) {
					posicaoPeao = new Posicao(3, destino.getColuna());
				}
				else {
					posicaoPeao = new Posicao(4, destino.getColuna());
				}
				tabuleiro.posicaoPeca(peao, posicaoPeao);
			}
		}
	}

	private void validacaoPosicaoOrigem(Posicao posicao) {
		if(!tabuleiro.temUmaPeca(posicao)) {
			throw new XadrezExcecao("Não existe peça na posição de origem escolhida!");
		}
		if(jogadorAtual != ((XadrezPeca)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezExcecao("A peça na posição de origem escolhida não é sua!");
		}
		if(!tabuleiro.peca(posicao).existeAlgumMovimentoPossivel()) {
			throw new XadrezExcecao("Não há movimentos possiveis para a peça de origem escolhida!");
		}
	}
	
	private void validacaoPosicaoDestino(Posicao origem, Posicao destino) {
		if(!tabuleiro.peca(origem).movimentosPossiveis(destino)) {
			throw new XadrezExcecao("Posição de destino invalida!");
		}
	}
	
	private void proximoTurno() {
		turno++;
		//(se o jogador atual for igual a branco entao agora ele vai ser igual a preto caso contrario sera igual a branco 
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}
	
	private Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private XadrezPeca rei(Cor cor) {
		List<Peca> list = pecaNoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == cor).collect(Collectors.toList());
		for(Peca p : list) {
			if(p instanceof Rei) {
				return (XadrezPeca)p;
			}
		}
		throw new IllegalStateException("Não existe o rei na cor " + cor + " no tabuleiro");
	}

	private boolean testeCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getXadrezPosicao().toPosicao();
		List<Peca> oponentePecas = pecaNoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == oponente(cor)).collect(Collectors.toList());
		for(Peca p : oponentePecas) {
			boolean[][] mat = p.movimentosPossiveis();
			if(mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testeCheckMate(Cor cor) {
		if(!testeCheck(cor)) {
			return false;
		}
		List<Peca> list =  pecaNoTabuleiro.stream().filter(x -> ((XadrezPeca)x).getCor() == cor).collect(Collectors.toList());
		for(Peca p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for(int i=0; i<tabuleiro.getLinhas(); i++) {
				for(int j=0; j<tabuleiro.getColunas(); j++) {
					if(mat[i][j]) {
						Posicao origem = ((XadrezPeca)p).getXadrezPosicao().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca capturaPeca = fazerMovimento(origem, destino);
						boolean testeCheck = testeCheck(cor);
						desfazerMovimento(origem, destino, capturaPeca);
						if(!testeCheck) {
							return false;
						}
					}
				}
			}
		}
		return true; 
	}
	
	private void colocarNovaPeca(char coluna, int linha, XadrezPeca peca) {
		tabuleiro.posicaoPeca(peca, new XadrezPosicao(coluna, linha).toPosicao());
		pecaNoTabuleiro.add(peca);
	}
	
	private void setupInicial() {
		colocarNovaPeca('a', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('b', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('c', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 1, new Rainha(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO,this));
		colocarNovaPeca('f', 1, new Bispo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('g', 1, new Cavalo(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('h', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('a', 2, new Peao(tabuleiro, Cor.BRANCO,this));
		colocarNovaPeca('b', 2, new Peao(tabuleiro, Cor.BRANCO,this));
		colocarNovaPeca('c', 2, new Peao(tabuleiro, Cor.BRANCO,this));
		colocarNovaPeca('d', 2, new Peao(tabuleiro, Cor.BRANCO,this));
		colocarNovaPeca('e', 2, new Peao(tabuleiro, Cor.BRANCO,this));
		colocarNovaPeca('f', 2, new Peao(tabuleiro, Cor.BRANCO,this));
		colocarNovaPeca('g', 2, new Peao(tabuleiro, Cor.BRANCO,this));
		colocarNovaPeca('h', 2, new Peao(tabuleiro, Cor.BRANCO,this));
		
		colocarNovaPeca('a', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('b', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('c', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('d', 8, new Rainha(tabuleiro, Cor.PRETO));
		colocarNovaPeca('e', 8, new Rei(tabuleiro, Cor.PRETO,this));
		colocarNovaPeca('f', 8, new Bispo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('g', 8, new Cavalo(tabuleiro, Cor.PRETO));
		colocarNovaPeca('h', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('a', 7, new Peao(tabuleiro, Cor.PRETO,this));
		colocarNovaPeca('b', 7, new Peao(tabuleiro, Cor.PRETO,this));
		colocarNovaPeca('c', 7, new Peao(tabuleiro, Cor.PRETO,this));
		colocarNovaPeca('d', 7, new Peao(tabuleiro, Cor.PRETO,this));
		colocarNovaPeca('e', 7, new Peao(tabuleiro, Cor.PRETO,this));
		colocarNovaPeca('f', 7, new Peao(tabuleiro, Cor.PRETO,this));
		colocarNovaPeca('g', 7, new Peao(tabuleiro, Cor.PRETO,this));
		colocarNovaPeca('h', 7, new Peao(tabuleiro, Cor.PRETO,this));		
	}
}
