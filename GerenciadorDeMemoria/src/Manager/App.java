package Manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*Guilherme Draghetti e Marcelo Azevedo
	Data: 19 de junho de 2019;
		Implementar um programa que controla a alocação de memória utilizando "gerência de memória por partições variáveis". 
1. O gerente deve receber um bloco que representa a memória disponível do endereço mi até o endereço mf.  
2. A partir deste bloco, o gerente recebe solicitações de alocação de memória e solicitações de liberação de memória. 
3. A solicitação de memória deve retornar um identificador para a área de memória que foi alocada, enquanto o comando de liberação de memória envia o identificador recebido durante a alocação.  
4. O programa deve informar o momento que houver fragmentação externa no sistema. Neste momento deve mostrar como a memória está organizada, ou seja, quais os blocos ocupados e quais os blocos livres. 
5. O programa deve receber as solicitações e liberações via o arquivo de entrada de dados 
6. Não é preciso controlar tempo, as alocações e liberações são realizadas na ordem que chegarem e puderem ser atendidas. Se uma alocação não puder ser atendida, deve ser verificado se ela pode ser atendida no momento que uma liberação acontecer. 
7. Deve haver alguma forma de acompanhar (visualizar) o que está acontecendo no programa a cada solicitação ou liberação. 


*/

//lê o arquivo texto e cria os objetos com as solicitações;
public class App {
	// inicializa as variáveis das partições
	public static int inicioBloco;
	public static int finalBloco;
	public static int tamMemoria;
	public static int inicioAlocacaoBloco;
	public static int bloco = 1;
	public static ArrayList<Solicitacao> lsSolicitacao = new ArrayList();
	public static ArrayList<Solicitacao> lsSolicEspera = new ArrayList();

	// inicializa o programa
	public static void main(String[] args) throws IOException {

		FileReader lerArquivo = new FileReader("t3.txt");
		BufferedReader s = new BufferedReader(lerArquivo);

		String line;
		// descarta o primeiro numero do arquivo
		s.readLine();
		line = s.readLine();

		// lê as variáveis para definir o início e o fim da memória para alocar as
		// requisições.
		inicioBloco = Integer.parseInt(line);
		line = s.readLine();
		finalBloco = Integer.parseInt(line);
		tamMemoria = (finalBloco - inicioBloco);
		line = s.readLine();
		inicioAlocacaoBloco = inicioBloco;

		// lê a linha com a solicitação.
		while (line != null) {

			String[] linha = line.trim().split(" ");
			// verifica qual a letra para chamar o método correto.
			if (linha[0].equals("S")) {
				criaSolicitacao(linha);

			} else if (linha[0].equals("L")) {
				liberaBloco(linha);

			} else {
				System.out.print("Letra não corresponde a nenhum método! ");
			}

			line = s.readLine();
		}
		// depois de ler todas as solicitações, verifica a diferença entre o último
		// bloco da lista e número final passado pelo txt, e cria um bloco livre com o
		// tamanho do final total - o final da alocação do ultimo elemento da lista;
		if (lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao() < finalBloco) {
			Solicitacao completaBloco = new Solicitacao();
			completaBloco.setBloco("livre");
			completaBloco.setLiberado(true);
			completaBloco.setFinalAlocacao(finalBloco);
			completaBloco.setInicioAlocacao(lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao());
			completaBloco.setTamanhoAlocado(finalBloco - completaBloco.getInicioAlocacao());
			lsSolicitacao.add(completaBloco);
			System.out.println(completaBloco.toString() + "\n");
		}
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("Estado do bloco após todas as leituras: ");
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			System.out.println(lsSolicitacao.get(i).toString());
		}
		s.close();
	}

	public static void criaSolicitacao(String[] linha) {
		// cria a partição inicializando as váriaveis.
		Solicitacao solic = new Solicitacao();
		solic.setBloco("" + bloco);
		solic.setInicioAlocacao(inicioAlocacaoBloco);
		solic.setTamanhoAlocado(Integer.parseInt(linha[1]));
		solic.setFinalAlocacao(inicioAlocacaoBloco + solic.getTamanhoAlocado());
		bloco++;
		inicioAlocacaoBloco = solic.getFinalAlocacao();
		adicionaNaLista(solic);

	}

	public static void adicionaNaLista(Solicitacao solic) {
		// váriavel para ver o método ja adicionou na lista, evitando de adicionar mais
		// de uma vez.
		boolean jaAdicionou = false;
		// para adicionar na lista devemos procurar na linked list se há alguma posição
		// dispinível que a solicitação possa ser alocada.
		if (solic.getFinalAlocacao() <= finalBloco && !lsSolicitacao.contains(solic)) {
			for (int i = 0; i < lsSolicitacao.size(); i++) {
				Solicitacao aux = lsSolicitacao.get(i);
				int posicao = lsSolicitacao.indexOf(aux);
				// verifica se há alguma partição liberada, verificando se o tamanho da
				// solicitação é igual ao tamanho da partição.
				if (aux.isLiberado() && aux.getTamanhoAlocado() == solic.getTamanhoAlocado()) {
					// substitui o bloco livre pelo objeto da solicitação e seta o inicio e final
					// com os mesmo números que já estavam.
					lsSolicitacao.set(i, solic);
					lsSolicitacao.get(i).setInicioAlocacao(aux.getInicioAlocacao());
					lsSolicitacao.get(i).setFinalAlocacao(aux.getFinalAlocacao());
					inicioAlocacaoBloco = lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao();
					jaAdicionou = true;
					// diminui a memória disponível
					tamMemoria -= lsSolicitacao.get(i).getTamanhoAlocado();
					System.out.println(lsSolicitacao.get(i).toString() + "\n");
					break;
				}
				// verifica se há alguma partição liberada, verificando se o tamanho da
				// solicitação é menor que o tamanho da partição;
				else if (aux.isLiberado() && aux.getTamanhoAlocado() > solic.getTamanhoAlocado()) {
					solic.setInicioAlocacao(aux.getInicioAlocacao());
					solic.setFinalAlocacao(solic.getInicioAlocacao() + solic.getTamanhoAlocado());
					// diminui a memoria disponivel
					tamMemoria -= solic.getTamanhoAlocado();
					jaAdicionou = true;
					splitBloco(posicao, solic);

					break;
				}

			}
			// se ainda não foi adicionado, adiciona no final da lista;
			if (jaAdicionou == false)
				lsSolicitacao.add(solic);
			tamMemoria -= solic.getTamanhoAlocado();
			System.out.println(solic.toString() + "\n");
		}
		// se não conseguir adicionar no final da lista, bota em uma lista de espera
		// para quando um bloco liberar verificar se pode encaixar lá;
		else {
			if (solic.getTamanhoAlocado() <= tamMemoria) {
				System.out.println(tamMemoria + " livres - " + solic.getTamanhoAlocado()
						+ " solicitados - Fragmentação Externa\n");
			} else {
				System.out.println("Não há memória disponível\n");
			}
			lsSolicEspera.add(solic);
		}

	}

	// libera uma partição setando o booleando para poder reescrever e também
	// verifica se da para juntar duas partições.
	public static void liberaBloco(String[] linha) {
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			if (lsSolicitacao.get(i).getBloco().contentEquals(linha[1])) {
				lsSolicitacao.get(i).setBloco("livre");
				lsSolicitacao.get(i).setLiberado(true);
				tamMemoria += lsSolicitacao.get(i).getTamanhoAlocado();
				System.out.println(lsSolicitacao.get(i).toString() + "\n");
				juntaBloco(i);
				break;
			}
		}
		verificaListaEspera();
	}

	public static void verificaListaEspera() {
		Solicitacao aux = new Solicitacao();
		// percorre a as partições em busca de uma partição livre/
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			aux = lsSolicitacao.get(i);
			for (int j = 0; j < lsSolicEspera.size(); j++) {
				// se encontrar, verifica se algum elemento da lista de espera pode se encaixar
				// nessa partição
				// se puder se encaixar e for menor que a partição da lista principal, seta o
				// objeto com o inicio e fim de alocação corretos, e chama split para divir em
				// dois blocos
				if (lsSolicitacao.get(i).isLiberado()
						&& (lsSolicitacao.get(i).getTamanhoAlocado() > lsSolicEspera.get(j).getTamanhoAlocado())) {
					lsSolicEspera.get(j).setInicioAlocacao(aux.getInicioAlocacao());
					lsSolicEspera.get(j).setFinalAlocacao(
							lsSolicEspera.get(j).getInicioAlocacao() + lsSolicEspera.get(j).getTamanhoAlocado());
					tamMemoria -= lsSolicEspera.get(j).getTamanhoAlocado();
					splitBloco(i, lsSolicEspera.get(j));
					lsSolicEspera.remove(j);

					break;
					// se for do mesmo tamanho, adiciona na posição e seta e inicio e final
					// corretamente.
				} else if (lsSolicitacao.get(i).isLiberado()
						&& (lsSolicitacao.get(i).getTamanhoAlocado() == lsSolicEspera.get(j).getTamanhoAlocado())) {
					lsSolicitacao.set(i, lsSolicEspera.get(j));
					lsSolicitacao.get(i).setInicioAlocacao(aux.getInicioAlocacao());
					lsSolicitacao.get(i).setFinalAlocacao(aux.getFinalAlocacao());
					tamMemoria -= lsSolicEspera.get(j).getTamanhoAlocado();
					System.out.println(lsSolicitacao.get(i).toString() + "\n");
					lsSolicEspera.remove(j);

					break;

				}
			}
		}
	}

	// junta dois blocos e empurra a linked list para a esquerda, i = i + 1;
	public static void juntaBloco(int bloco) {

		// verifica se bloco acima está livre para juntar;
		try {
			// junta os dois blocos, setando o inicio do anterior no proximo e a soma dos
			// tamanhos alocados.
			if (lsSolicitacao.get(bloco - 1).isLiberado() && (bloco - 1) >= 0) {
				lsSolicitacao.get(bloco).setTamanhoAlocado(lsSolicitacao.get(bloco).getTamanhoAlocado()
						+ lsSolicitacao.get(bloco - 1).getTamanhoAlocado());
				lsSolicitacao.get(bloco).setInicioAlocacao(lsSolicitacao.get(bloco - 1).getInicioAlocacao());
				// remove o bloco inutilizado
				lsSolicitacao.remove(bloco - 1);
				System.out.println(lsSolicitacao.get(bloco).toString() + "\n");
				// verfica se o bloco anterior está livre.
				if (lsSolicitacao.get(bloco - 1).isLiberado()) {
					juntaBloco(bloco - 1);
				}
			} else {
				if (lsSolicitacao.get(bloco + 1) != null && lsSolicitacao.get(bloco + 1).isLiberado()) {

					lsSolicitacao.get(bloco).setTamanhoAlocado(lsSolicitacao.get(bloco + 1).getTamanhoAlocado()
							+ lsSolicitacao.get(bloco).getTamanhoAlocado());
					lsSolicitacao.get(bloco).setFinalAlocacao(lsSolicitacao.get(bloco + 1).getFinalAlocacao());
					lsSolicitacao.remove(bloco + 1);
					System.out.println(lsSolicitacao.get(bloco).toString() + "\n");
					// verifica se o proximo bloco pode juntar
					if (lsSolicitacao.get(bloco + 1).isLiberado()) {
						juntaBloco(bloco + 1);
					}
				}

			}

		}
		// trata o erro para que o programa continue rodando, quando o dá a exceção é
		// porque o programa não necessita deste método.
		catch (IndexOutOfBoundsException e) {

		}

	}

	// divide um bloco em dois e empurra para a direita, i+1 = i; Por padrão o bloco
	// livre ficará depois do bloco ocupado.
	public static void splitBloco(int posicao, Solicitacao solic) {
		// cria um bloco vazio com o resto do tamanho da alocação;
		Solicitacao restoDoBloco = new Solicitacao();
		restoDoBloco.setTamanhoAlocado(lsSolicitacao.get(posicao).getTamanhoAlocado() - solic.getTamanhoAlocado());
		restoDoBloco.setInicioAlocacao(solic.getFinalAlocacao());
		restoDoBloco.setFinalAlocacao(solic.getFinalAlocacao() + restoDoBloco.getTamanhoAlocado());
		restoDoBloco.setBloco("livre");
		restoDoBloco.setLiberado(true);
		// coloca a solicitação na posição;
		lsSolicitacao.set(posicao, solic);
		// adiciona a solicitação no final do bloco
		lsSolicitacao.add(restoDoBloco);
		System.out.println(lsSolicitacao.get(posicao).toString() + "\n");
		System.out.println(restoDoBloco.toString() + "\n");
		// empurra a lista para a direita, no qual a posição i + 1 = i;
		if ((posicao + 2) < (lsSolicitacao.size())) {
			for (int i = lsSolicitacao.size() - 1; i >= posicao + 2; i--) {
				lsSolicitacao.set(i, lsSolicitacao.get(i - 1));
			}
			// (posição +1) ficará igual, pois a iteraçã só começa em (posição +2), assim
			// adicionamos esse bloco vazio na posição que não foi modificada.
			lsSolicitacao.set(posicao + 1, restoDoBloco);
		}
		// seta o inicio da alocacao do proximo bloco para o final do último bloco da
		// lista;
		inicioAlocacaoBloco = lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao();

	}

}