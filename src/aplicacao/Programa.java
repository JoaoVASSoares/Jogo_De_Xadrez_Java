package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import Xadrez.XadrezExcecao;
import Xadrez.XadrezPartida;
import Xadrez.XadrezPeca;
import Xadrez.XadrezPosicao;

public class Programa {
	public static void main(String[]args) {
		
		Scanner sc = new Scanner(System.in);
		XadrezPartida xadrezPartida = new XadrezPartida();
		List<XadrezPeca> capturadas = new ArrayList<>();
		
		while(!xadrezPartida.getCheckMate()) {
			try {
				UI.limparTela();
				UI.printPartida(xadrezPartida,capturadas);
				System.out.println();
				System.out.print("Origem: ");
				XadrezPosicao origem = UI.lerPosicaoXadrez(sc);
				
				boolean[][] movimentoPossivel = xadrezPartida.movimentoPossivel(origem);
				UI.limparTela();
				UI.printTabuleiro(xadrezPartida.getPecas(),movimentoPossivel);
			
				System.out.println();
				System.out.print("Destino: ");
				XadrezPosicao destino = UI.lerPosicaoXadrez(sc);
				
				XadrezPeca capturaPeca = xadrezPartida.performaMovimentoXadrez(origem, destino);
				
				if(capturaPeca != null) {
					capturadas.add(capturaPeca);
				}
			}
			catch(XadrezExcecao e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.limparTela();
		UI.printPartida(xadrezPartida, capturadas);
	}
}
