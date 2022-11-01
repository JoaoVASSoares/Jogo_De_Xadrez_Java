package aplicacao;

import java.util.Scanner;

import Xadrez.XadrezPartida;
import Xadrez.XadrezPeca;
import Xadrez.XadrezPosicao;

public class Programa {
	public static void main(String[]args) {
		
		Scanner sc = new Scanner(System.in);
		XadrezPartida xadrezPartida = new XadrezPartida();
		
		while(true) {
		UI.printTabuleiro(xadrezPartida.getPecas());
		System.out.println();
		System.out.print("Origem: ");
		XadrezPosicao origem = UI.lerPosicaoXadrez(sc);
		
		System.out.println();
		System.out.print("Destino: ");
		XadrezPosicao destino = UI.lerPosicaoXadrez(sc);
		
		XadrezPeca capturaPeca = xadrezPartida.performaMovimentoXadrez(origem, destino);
		}
	}
}
