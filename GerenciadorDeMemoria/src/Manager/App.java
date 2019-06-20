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
	public static LinkedList<Solicitacao> lsSolicitacao = new LinkedList();
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
			System.out.println(linha[0]);
			// verifica qual a letra para chamar o m�todo correto.
			if (linha[0].equals("S")) {
				criaSolicitacao(linha);

			} else if (linha[0].equals("L")) {
				liberaBloco(linha);

			} else {
				System.out.print("Letra n�o corresponde a nenhum m�todo! ");
			}

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
		adicionaNaLista(solic);
	}

	public static void adicionaNaLista(Solicitacao solic) {
		// para adicionar na lista devemos procurar na linked list se h� alguma posi��o
		// dispin�vel que a solicita��o possa ser alocada.
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			Solicitacao aux = lsSolicitacao.get(i);
			// verifica se h� alguma parti��o liberada, verificando se o tamanho da
			// solicita��o � igual ao tamanho da parti��o.
			if (aux.isLiberado() && aux.getTamanhoAlocado() == solic.getTamanhoAlocado()) {
				lsSolicitacao.set(i, solic);
				int difTamanhoAlocado = aux.getTamanhoAlocado() - solic.getTamanhoAlocado();
				if (lsSolicitacao.get(i + 1).isLiberado()) {
					lsSolicitacao.get(i + 1)
							.setInicioAlocacao(lsSolicitacao.get(i + 1).getInicioAlocacao() - difTamanhoAlocado);
					lsSolicitacao.get(i + 1)
							.setTamanhoAlocado(lsSolicitacao.get(i + 1).getTamanhoAlocado() + difTamanhoAlocado);
				}
				// verifica se h� alguma parti��o liberada, verificando se o tamanho da
				// solicita��o � menor que o tamanho da parti��o;
			} else if (aux.isLiberado() && aux.getTamanhoAlocado() > solic.getTamanhoAlocado()) {
				int posicao = lsSolicitacao.indexOf(aux);
				splitBloco(posicao, solic);
			}
		}
		// adiciona no final do bloco verificando se h� espa�o suficiente.
		if (inicioAlocacaoBloco + solic.getTamanhoAlocado() < finalBloco) {
			lsSolicitacao.add(solic);
		} else {
			lsSolicEspera.add(solic);
		}
		inicioAlocacaoBloco = solic.getFinalAlocacao();

	}

	// libera uma parti��o setando o booleando para poder reescrever e tamb�m
	// verifica se da para juntar duas parti��es.
	public static void liberaBloco(String[] linha) {
		int getBloco = Integer.parseInt(linha[1]);
		lsSolicitacao.get(getBloco).setLiberado(true);
		lsSolicitacao.get(getBloco).setBloco("livre");

		juntaBloco(getBloco);

	}

	// junta dois parti��o e empurra a linked list para a esquerda, i = i + 1;
	public static void juntaBloco(int bloco) {
		if (lsSolicitacao.get(bloco - 1).isLiberado()) {
			lsSolicitacao.get(bloco - 1).setTamanhoAlocado(
					lsSolicitacao.get(bloco).getTamanhoAlocado() + lsSolicitacao.get(bloco - 1).getTamanhoAlocado());
			lsSolicitacao.get(bloco - 1).setFinalAlocacao(lsSolicitacao.get(bloco).getFinalAlocacao());
			for (int i = bloco; i < lsSolicitacao.size(); i++) {
				lsSolicitacao.set(i, lsSolicitacao.get(i + 1));
			}
		} else if (lsSolicitacao.get(bloco + 1).isLiberado()) {
			lsSolicitacao.get(bloco).setTamanhoAlocado(
					lsSolicitacao.get(bloco + 1).getTamanhoAlocado() + lsSolicitacao.get(bloco).getTamanhoAlocado());
			lsSolicitacao.get(bloco).setFinalAlocacao(lsSolicitacao.get(bloco + 1).getFinalAlocacao());
			for (int i = bloco + 1; i < lsSolicitacao.size(); i++) {
				lsSolicitacao.set(i, lsSolicitacao.get(i + 1));
			}
		}

	}

	// divide uma parti��o em dois e empurra para a direita, i+1 = i
	public static void splitBloco(int posicao, Solicitacao solic) {

	}

	// ap�s o termino da leitura do arquivo verifica fragmenta��o;
	public static void verificaFragmentacao() {

	}

}