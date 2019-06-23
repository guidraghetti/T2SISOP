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
				lsSolicitacao.get(0);
			} else if (linha[0].equals("L")) {
				liberaBloco(linha);

			} else {
				System.out.print("Letra n�o corresponde a nenhum m�todo! ");
			}

			line = s.readLine();
		}
		if (lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao() < finalBloco) {
			Solicitacao completaBloco = new Solicitacao();
			completaBloco.setBloco("livre");
			completaBloco.setLiberado(true);
			completaBloco.setFinalAlocacao(finalBloco);
			completaBloco.setInicioAlocacao(lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao());
			completaBloco.setTamanhoAlocado(finalBloco - completaBloco.getInicioAlocacao());
			lsSolicitacao.add(completaBloco);
		}

		s.close();
		verificaFragmentacao();
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
		// v�riavel para ver o m�todo ja adicionou na lista, evitando de adicionar mais de uma vez.
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
					//substitui o bloco livre pelo objeto da solicita��o e seta o inicio e final com os mesmo n�meros que j� estavam.
					lsSolicitacao.set(i, solic);
					lsSolicitacao.get(i).setInicioAlocacao(aux.getInicioAlocacao());
					lsSolicitacao.get(i).setFinalAlocacao(aux.getFinalAlocacao());
					lsSolicitacao.get(i).setBloco("" + (posicao+1));
					inicioAlocacaoBloco = lsSolicitacao.get(lsSolicitacao.size()-1).getFinalAlocacao();
					jaAdicionou = true;
					break;
				}
				// verifica se h� alguma parti��o liberada, verificando se o tamanho da
				// solicita��o � menor que o tamanho da parti��o;
				else if (aux.isLiberado() && aux.getTamanhoAlocado() > solic.getTamanhoAlocado()) {
					solic.setBloco("" + (posicao + 1));
					solic.setInicioAlocacao(aux.getInicioAlocacao());
					solic.setFinalAlocacao(solic.getInicioAlocacao() + solic.getTamanhoAlocado());
					jaAdicionou = true;
					splitBloco(posicao, solic);

					break;
				}

			}
			//se ainda n�o foi adicionado, adiciona no final da lista;
			if (jaAdicionou == false)
				lsSolicitacao.add(solic);
			
		} 
		// se n�o conseguir adicionar no final da lista, bota em uma lista de espera para quando um bloco liberar verificar se pode encaixar l�;
		else {
			lsSolicEspera.add(solic);
		}

	}

	// libera uma parti��o setando o booleando para poder reescrever e tamb�m
	// verifica se da para juntar duas parti��es.
	public static void liberaBloco(String[] linha) {
		int getBloco = Integer.parseInt(linha[1]) - 1;
		lsSolicitacao.get(getBloco).setLiberado(true);
		lsSolicitacao.get(getBloco).setBloco("livre");

		juntaBloco(getBloco);
		verificaListaEspera(getBloco);
	}

	public static void verificaListaEspera(int getBloco) {
		int g = 0;
		// verifica na lista de Espera se h� alguma solicita��o que se encaixe na
		// posi��o liberada;
		while (g < lsSolicEspera.size()) {
			if (lsSolicEspera.get(g).getTamanhoAlocado() < lsSolicitacao.get(getBloco).getTamanhoAlocado()) {
				lsSolicitacao.set(g, lsSolicEspera.get(g));
				splitBloco(getBloco, lsSolicEspera.get(g));
				break;
			} else if (lsSolicEspera.get(g).getTamanhoAlocado() == lsSolicitacao.get(getBloco).getTamanhoAlocado()) {
				lsSolicitacao.set(g, lsSolicEspera.get(g));
				lsSolicitacao.get(g).setBloco("" + g + 1);
				break;
			}
			g++;
		}

	}

	// junta dois blocos e empurra a linked list para a esquerda, i = i + 1;
	public static void juntaBloco(int bloco) {

		// verifica se bloco acima est� livre para juntar;
		try {
			// junta os dois blocos, setando o inicio do anterior no proximo e a soma dos
			// tamanhos alocados.
			if (lsSolicitacao.get(bloco-1).isLiberado() && (bloco-1) >= 0) {
				lsSolicitacao.get(bloco).setTamanhoAlocado(lsSolicitacao.get(bloco).getTamanhoAlocado()
						+ lsSolicitacao.get(bloco-1).getTamanhoAlocado());
				lsSolicitacao.get(bloco).setInicioAlocacao(lsSolicitacao.get(bloco-1).getInicioAlocacao());
				// remove o bloco inutilizado
				lsSolicitacao.remove(bloco-1);
			} else {
				if (lsSolicitacao.get(bloco+1) != null && lsSolicitacao.get(bloco+1).isLiberado()
						&& (bloco+1 <= lsSolicitacao.size()-1)) {

					lsSolicitacao.get(bloco).setTamanhoAlocado(lsSolicitacao.get(bloco+1).getTamanhoAlocado()
							+ lsSolicitacao.get(bloco).getTamanhoAlocado());
					lsSolicitacao.get(bloco).setFinalAlocacao(lsSolicitacao.get(bloco+1).getFinalAlocacao());
					lsSolicitacao.remove(bloco + 1);
				}
			}
		} 
		// trata o erro para que o programa continue rodando, quando o d� a exce��o � porque o programa n�o necessita deste m�todo.
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
		// empurra a lista para a direita, no qual a posi��o i + 1 = i;
		if ((posicao + 2) < (lsSolicitacao.size())) {
			for (int i = lsSolicitacao.size() - 1; i >= posicao + 2; i--) {

				lsSolicitacao.set(i, lsSolicitacao.get(i - 1));
				lsSolicitacao.get(i).setBloco("" + (i + 1));
			}
			// (posi��o +1) ficar� igual, pois a itera�� s� come�a em (posi��o +2), assim
			// adicionamos esse bloco vazio na posi��o que n�o foi modificada.
			lsSolicitacao.set(posicao + 1, restoDoBloco);
		}
		// seta o inicio da alocacao do proximo bloco para o final do �ltimo bloco da lista;
			inicioAlocacaoBloco = lsSolicitacao.get(lsSolicitacao.size()-1).getFinalAlocacao();
		
	}

	// ap�s o termino da leitura do arquivo verifica fragmenta��o;
	public static void verificaFragmentacao() {
		String resultado = "";
		int memoriaLivre = 0;
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			System.out.println(lsSolicitacao.get(i).toString());
			if (lsSolicitacao.get(i).isLiberado()) {
				memoriaLivre += lsSolicitacao.get(i).getTamanhoAlocado();
			}
			for (int j = 0; j < lsSolicEspera.size(); j++) {
				if (lsSolicEspera.get(j).getTamanhoAlocado() <= memoriaLivre) {
					resultado = memoriaLivre + "livres, " + lsSolicEspera.get(j).getTamanhoAlocado()
							+ " solicitados - Fragmenta��o Externa";
					break;
				}
			}
		}
		System.out.println(resultado);
	}

}