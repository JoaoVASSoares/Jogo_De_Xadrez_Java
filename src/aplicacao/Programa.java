package aplicacao;

import Xadrez.PartidaDeXadrez;

public class Programa {
	public static void main(String[]args) {
		
		PartidaDeXadrez partidaDeXadrez = new PartidaDeXadrez();
		UI.printTabuleiro(partidaDeXadrez.getPecas());
	}
}
