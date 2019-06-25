package Manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//l� o arquivo texto e cria os objetos com as solicita��es;
public class App {
	// inicializa as vari�veis das parti��es
	public static int inicioBloco;
	public static int finalBloco;
	public static int tamMemoria;
	public static int inicioAlocacaoBloco;
	public static int bloco = 1;
	public static ArrayList<Solicitacao> lsSolicitacao = new ArrayList();
	public static ArrayList<Solicitacao> lsSolicEspera = new ArrayList();

	// inicializa o programa
	public static void main(String[] args) throws IOException {

		FileReader lerArquivo = new FileReader("t1.txt");
		BufferedReader s = new BufferedReader(lerArquivo);

		String line;
		// descarta o primeiro numero do arquivo
		s.readLine();
		line = s.readLine();

		// l� as vari�veis para definir o in�cio e o fim da mem�ria para alocar as
		// requisi��es.
		inicioBloco = Integer.parseInt(line);
		line = s.readLine();
		finalBloco = Integer.parseInt(line);
		tamMemoria = (finalBloco - inicioBloco);
		line = s.readLine();
		inicioAlocacaoBloco = inicioBloco;

		// l� a linha com a solicita��o.
		while (line != null) {

			String[] linha = line.trim().split(" ");
			// verifica qual a letra para chamar o m�todo correto.
			if (linha[0].equals("S")) {
				criaSolicitacao(linha);

			} else if (linha[0].equals("L")) {
				liberaBloco(linha);

			} else {
				System.out.print("Letra n�o corresponde a nenhum m�todo! ");
			}

			line = s.readLine();
		}
		// depois de ler todas as solicita��es, verifica a diferen�a entre o �ltimo
		// bloco da lista e n�mero final passado pelo txt, e cria um bloco livre com o
		// tamanho do final total - o final da aloca��o do ultimo elemento da lista;
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
		System.out.println("Estado do bloco ap�s todas as leituras: ");
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			System.out.println(lsSolicitacao.get(i).toString());
		}
		s.close();
	}

	public static void criaSolicitacao(String[] linha) {
		// cria a parti��o inicializando as v�riaveis.
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
		// v�riavel para ver o m�todo ja adicionou na lista, evitando de adicionar mais
		// de uma vez.
		boolean jaAdicionou = false;
		// para adicionar na lista devemos procurar na linked list se h� alguma posi��o
		// dispin�vel que a solicita��o possa ser alocada.
		if (solic.getFinalAlocacao() <= finalBloco && !lsSolicitacao.contains(solic)) {
			for (int i = 0; i < lsSolicitacao.size(); i++) {
				Solicitacao aux = lsSolicitacao.get(i);
				int posicao = lsSolicitacao.indexOf(aux);
				// verifica se h� alguma parti��o liberada, verificando se o tamanho da
				// solicita��o � igual ao tamanho da parti��o.
				if (aux.isLiberado() && aux.getTamanhoAlocado() == solic.getTamanhoAlocado()) {
					// substitui o bloco livre pelo objeto da solicita��o e seta o inicio e final
					// com os mesmo n�meros que j� estavam.
					lsSolicitacao.set(i, solic);
					lsSolicitacao.get(i).setInicioAlocacao(aux.getInicioAlocacao());
					lsSolicitacao.get(i).setFinalAlocacao(aux.getFinalAlocacao());
					inicioAlocacaoBloco = lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao();
					jaAdicionou = true;
					tamMemoria -= lsSolicitacao.get(i).getTamanhoAlocado();
					System.out.println(lsSolicitacao.get(i).toString() + "\n");
					break;
				}
				// verifica se h� alguma parti��o liberada, verificando se o tamanho da
				// solicita��o � menor que o tamanho da parti��o;
				else if (aux.isLiberado() && aux.getTamanhoAlocado() > solic.getTamanhoAlocado()) {
					solic.setInicioAlocacao(aux.getInicioAlocacao());
					solic.setFinalAlocacao(solic.getInicioAlocacao() + solic.getTamanhoAlocado());
					tamMemoria -= solic.getTamanhoAlocado();
					jaAdicionou = true;
					splitBloco(posicao, solic);

					break;
				}

			}
			// se ainda n�o foi adicionado, adiciona no final da lista;
			if (jaAdicionou == false)
				lsSolicitacao.add(solic);
			tamMemoria -= solic.getTamanhoAlocado();
			System.out.println(solic.toString() + "\n");
		}
		// se n�o conseguir adicionar no final da lista, bota em uma lista de espera
		// para quando um bloco liberar verificar se pode encaixar l�;
		else {
			if (solic.getTamanhoAlocado() <= tamMemoria) {
				System.out.println(tamMemoria + " livres - " + solic.getTamanhoAlocado()
						+ " solicitados - Fragmenta��o Externa\n");
			} else {
				System.out.println("N�o h� mem�ria dispon�vel\n");
			}
			lsSolicEspera.add(solic);
		}

	}

	// libera uma parti��o setando o booleando para poder reescrever e tamb�m
	// verifica se da para juntar duas parti��es.
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
		// percorre a as parti��es em busca de uma parti��o livre/
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			aux = lsSolicitacao.get(i);
			for (int j = 0; j < lsSolicEspera.size(); j++) {
				// se encontrar, verifica se algum elemento da lista de espera pode se encaixar
				// nessa parti��o
				// se puder se encaixar e for menor que a parti��o da lista principal, seta o
				// objeto com o inicio e fim de aloca��o corretos, e chama split para divir em
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
					// se for do mesmo tamanho, adiciona na posi��o e seta e inicio e final
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

		// verifica se bloco acima est� livre para juntar;
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
				if (lsSolicitacao.get(bloco-1).isLiberado()) {
					juntaBloco(bloco-1);
				}
			} else {
				if (lsSolicitacao.get(bloco + 1) != null && lsSolicitacao.get(bloco + 1).isLiberado()) {

					lsSolicitacao.get(bloco).setTamanhoAlocado(lsSolicitacao.get(bloco + 1).getTamanhoAlocado()
							+ lsSolicitacao.get(bloco).getTamanhoAlocado());
					lsSolicitacao.get(bloco).setFinalAlocacao(lsSolicitacao.get(bloco + 1).getFinalAlocacao());
					lsSolicitacao.remove(bloco + 1);
					System.out.println(lsSolicitacao.get(bloco).toString() + "\n");
					if (lsSolicitacao.get(bloco+1).isLiberado()) {
						juntaBloco(bloco+1);
					}
				}

			}

		}
		// trata o erro para que o programa continue rodando, quando o d� a exce��o �
		// porque o programa n�o necessita deste m�todo.
		catch (IndexOutOfBoundsException e) {

		}

	}

	// divide um bloco em dois e empurra para a direita, i+1 = i; Por padr�o o bloco
	// livre ficar� depois do bloco ocupado.
	public static void splitBloco(int posicao, Solicitacao solic) {
		// cria um bloco vazio com o resto do tamanho da aloca��o;
		Solicitacao restoDoBloco = new Solicitacao();
		restoDoBloco.setTamanhoAlocado(lsSolicitacao.get(posicao).getTamanhoAlocado() - solic.getTamanhoAlocado());
		restoDoBloco.setInicioAlocacao(solic.getFinalAlocacao());
		restoDoBloco.setFinalAlocacao(solic.getFinalAlocacao() + restoDoBloco.getTamanhoAlocado());
		restoDoBloco.setBloco("livre");
		restoDoBloco.setLiberado(true);
		// coloca a solicita��o na posi��o;
		lsSolicitacao.set(posicao, solic);
		// adiciona a solicita��o no final do bloco
		lsSolicitacao.add(restoDoBloco);
		System.out.println(lsSolicitacao.get(posicao).toString() + "\n");
		System.out.println(restoDoBloco.toString() + "\n");
		// empurra a lista para a direita, no qual a posi��o i + 1 = i;
		if ((posicao + 2) < (lsSolicitacao.size())) {
			for (int i = lsSolicitacao.size() - 1; i >= posicao + 2; i--) {
				lsSolicitacao.set(i, lsSolicitacao.get(i - 1));
			}
			// (posi��o +1) ficar� igual, pois a itera�� s� come�a em (posi��o +2), assim
			// adicionamos esse bloco vazio na posi��o que n�o foi modificada.
			lsSolicitacao.set(posicao + 1, restoDoBloco);
		}
		// seta o inicio da alocacao do proximo bloco para o final do �ltimo bloco da
		// lista;
		inicioAlocacaoBloco = lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao();

	}

}