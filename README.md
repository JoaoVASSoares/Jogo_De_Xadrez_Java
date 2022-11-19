# Jogo de Xadres de Console
<p align="center">
  <img width="700" height="200" src="https://user-images.githubusercontent.com/66191563/88411134-ca94b100-cdad-11ea-9cd9-b6ef821dbf45.png">
</p>

# Índice
  

# Sobre o projeto
Este foi um projeto desenvolvido durante o curso de Java do @acenelio. A proposta principal dele é aplicar nossos conhecimentos na linguagem Java para criar um jogo de Xadrez que rodasse via console. Ele pode parecer um projeto simples, porém ele é muito mais complicado do que você imagina.

# Sobre o tabuleiro
  ### Impressão do tabuleiro no console
  A impressão do tabuleiro foi a parte mais simples desse projeto. Eu utilizei um *for* para criar "-" com um espaço entre eles no tamanho de oito por oito, totalizando sessenta e quatro casas (o tamanho padrão de um tabuleiro de Xadrez).

##### Código do método imprimirTabuleiro:
```cs
public static void printTabuleiro(PecaDeXadrez[][] pecas) {
  for (int i = 0; i<pecas.length;i++) {
	  System.out.print((8-i) + " ");
		  for(int j = 0; j<pecas.length;j++) {
				printPeca(pecas[i][j]);
			}
			System.out.println();
	}
	System.out.println("  a b c d e f g h");
}
```
##### E o resultado final foi este:
<p align="center">
   <img width="384" height="354" src="https://user-images.githubusercontent.com/66191563/88415065-3e39bc80-cdb4-11ea-9c31-ece24c2dda8d.PNG">
</p>
    
  ### A exceção TabuleiroExcecao
  Foi necessária a criação de uma exceção para o tabuleiro, ela ocorre quando uma casa invalida é selecionada.
  ##### Alguns exemplos de exceções:
  ```cs
  throw new TabuleiroExcecao("Não existe peça na posição de origem escolhida!"); 
  ```
  ```cs
  throw new TabuleiroExcecao("A peça na posição de origem escolhida não é sua!");          
  ```
  ```cs
  throw new TabuleiroExcecao("Não há movimentos possiveis para a peça de origem escolhida!");          
  ```
  ```cs
  throw new TabuleiroExcecao("Posição de destino invalida!");
  ```        

# Sobre as peças
 ### Peças presentes no tabuleiro:
 - Rei `(x1 Branco & x1 Preto)`, representado pela letra `R`;
 - Rainha `(x1 Branco & x1 Preto)`, representado pela letra `Q`;
 - Bispo `(x2 Branco & x2 Preto)`, representado pela letra `B`;
 - Cavalo `(x1 Branco & x1 Preto)`, representado pela letra `C`;
 - Torre `(x1 Branco & x1 Preto)`, representado pela letra `T`;
 - Peão `(x8 Branco & x8 Preto)`, representado pela letra `P`.
 
 ##### Imagem do tabuleiro com as peças:
 <p align="center">
   <img width="384" height="354" src="https://user-images.githubusercontent.com/107722106/202856730-637041d6-103c-49ca-9c89-362b9199a9e6.png">
</p>
 
 ### Método para colocar as peças no tabuleiro
 Eu implementei um método para colocar as peças em determinada posição. Cada peça possui a sua letra especifica, o método que mostra essa letra é este:
 ##### Código:
 ```cs
@Override
public String toString() {
	return "R";
}
 ```
 > *Neste caso a letra R é retornada, pois, este pedaço do código é do Rei.*
 
 Então eu necessitava de um modo de colocar esta letra no tabuleiro, então na classe *XadrezPartida* eu criei dois métodos:
 
 ##### Método colocarNovaPeca:
 ```cs
 private void colocarNovaPeca(char coluna, int linha, XadrezPeca peca) {
		tabuleiro.posicaoPeca(peca, new XadrezPosicao(coluna, linha).toPosicao());
		pecaNoTabuleiro.add(peca);
	}
 ```
 
 > Este método recebe uma coluna em char e um número, e o método *toPosicao()* converte esses dados em uma posição valida na matriz.
 
 ##### Método colocarPecas:
 Neste método eu criava uma peça com o *colocarNovaPeca()* deste modo:

 
 ```cs
 colocarNovaPeca('a', 1, new Torre(tab, Cor.Branco));
 ```

# Como foi criada a restrição de movimento para cada peça
Na classe *XadrezPartida* foram criados dois métodos, o método *validacaoPosicaoOrigem* e o método *validacaoPosicaoDestino*.

## Validar posição de origem:
Este método recebe uma posição informada pelo usuário (a coordenada de peça que ele quer mover). Ele foi dividido em tês *if's*.

#### 1. Posição nula
   Para testar se uma posição é nula eu utilizei a posição informada pelo usuário e a testei com o seguinte código:

   ```cs
if(!tabuleiro.temUmaPeca(posicao)) {
			throw new XadrezExcecao("Não existe peça na posição de origem escolhida!");
}  
   ```

#### 2. Se a peça escolhida é do jogador
   Eu tive que testar se a peça escolhida era da cor do jogador atual, para testar foi utilizado o seguinte código:

   ```cs
if(jogadorAtual != ((XadrezPeca)tabuleiro.peca(posicao)).getCor()) {
			throw new XadrezExcecao("A peça na posição de origem escolhida não é sua!");
}
   ```
   
#### 3. Se não existem movimentos possíveis
   Eu testei se a peça selecionada possuía movimentos possíveis, caso fosse escolhida uma peça que não podia se mover ocorria uma exceção.

   ```cs
if(!tabuleiro.peca(posicao).existeAlgumMovimentoPossivel()) {
			throw new XadrezExcecao("Não há movimentos possiveis para a peça de origem escolhida!");
}

   ```
 
## Validando posição de destino
Este método recebe uma posição informada pelo usuário (a coordenada para onde ele quer ir). Para este método foi utilizado apenas um *if*.

```cs
if(!tabuleiro.peca(origem).movimentosPossiveis(destino)) {
			throw new XadrezExcecao("Posição de destino invalida!");
}
```

# Jogadas especiais
 Neste tópico eu mencionarei as jogadas especiais implementadas no jogo.

 #### 1. Roque Pequeno
 O roque pequeno ocorre quando o rei e uma torre não se moveram, e entre eles possuem duas casas vazias.

 ```cs
 // #movimentoespecial roque pequeno
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(origemT);
			tabuleiro.posicaoPeca(torre, destinoT);
			torre.acrentarContadorMovimento();
		}

 ```
 
 #### 2. Roque Grande
 O roque pequeno ocorre quando o rei e uma torre não se moveram, e entre eles têm que possuir quatro casas vazias.

 ```cs
// #movimentoespecial roque grande
		if(p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			XadrezPeca torre = (XadrezPeca)tabuleiro.removerPeca(origemT);
			tabuleiro.posicaoPeca(torre, destinoT);
			torre.acrentarContadorMovimento();
		}
 ```
 
 #### 3. En Passant
  O En Passant ocorre quando um peão adversário avança duas casas no seu primeiro movimento na tentativa de evitar um confronto com um peão avançado (se for um peão branco na linha 5, se for um preto na linha 4) e um peão pode fazer a captura do mesmo modo.

  
##### Imagem ilustrativa
<p align="center">
   <img  src="https://qph.fs.quoracdn.net/main-qimg-945c1c2845899be55fad08abe4c010f9">
</p>

> *Imagem obtida neste [site](https://www.quora.com/How-would-you-explain-en-passant-to-a-beginner)*

##### Codigo
  ```cs
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
```
  
 #### 4. Promoção
A jogada promoção ocorre quando um peão chega no limite adversário do tabuleiro, então o peão pode se torna: Rainha, Bispo, Cavalo e uma Torre. Para esta jogada especial foi necessário criar uma interação com o usuário para ele escolher qual peça ele quer tornar, e depois promover o peão.

 ##### Exemplo de promoção de um peão para um rainha:
```cs
// #movimento especial promotion
		promoted = null;
		if(pesaQueMoveu instanceof Peao) {
			if((pesaQueMoveu.getCor() == Cor.BRANCO && destino.getLinha() == 0) ||(pesaQueMoveu.getCor() == Cor.PRETO && destino.getLinha() == 7)) {
				promoted = (XadrezPeca)tabuleiro.peca(destino);
				promoted = substituirPecaPromovida("Q");
				
				
			}
		}
```
### Contato: joaovitoralmeidas@hotmail.com
