package aplicacao;

import java.util.InputMismatchException;
import java.util.Scanner;

import Xadrez.XadrezExcecao;
import Xadrez.XadrezPartida;
import Xadrez.XadrezPeca;
import Xadrez.XadrezPosicao;

public class Programa {
	public static void main(String[]args) {
		
		Scanner sc = new Scanner(System.in);
		XadrezPartida xadrezPartida = new XadrezPartida();
		
		while(true) {
			try {
				UI.limparTela();
				UI.printTabuleiro(xadrezPartida.getPecas());
				System.out.println();
				System.out.print("Origem: ");
				XadrezPosicao origem = UI.lerPosicaoXadrez(sc);
				
				System.out.println();
				System.out.print("Destino: ");
				XadrezPosicao destino = UI.lerPosicaoXadrez(sc);
				
				XadrezPeca capturaPeca = xadrezPartida.performaMovimentoXadrez(origem, destino);
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
	}
}
