package Xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		
		check = (testeCheck(oponente(jogadorAtual))) ? true : false;
		
		if(testeCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			proximoTurno();
		}
		
		return (XadrezPeca)capturaDePeca;
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
		colocarNovaPeca('h', 7, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('d', 1, new Torre(tabuleiro, Cor.BRANCO));
		colocarNovaPeca('e', 1, new Rei(tabuleiro, Cor.BRANCO));
		
		
		colocarNovaPeca('b', 8, new Torre(tabuleiro, Cor.PRETO));
		colocarNovaPeca('a', 8, new Rei(tabuleiro, Cor.PRETO));
	}
}
