package Manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//lê o arquivo texto e cria os objetos com as solicitações;
public class App {
	// inicializa as variáveis das partições
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
		
		s.close();
		verificaFragmentacao();
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
		// para adicionar na lista devemos procurar na linked list se há alguma posição
		// dispinível que a solicitação possa ser alocada.
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			Solicitacao aux = lsSolicitacao.get(i);
			// verifica se há alguma partição liberada, verificando se o tamanho da
			// solicitação é igual ao tamanho da partição.
			if (aux.isLiberado() && aux.getTamanhoAlocado() == solic.getTamanhoAlocado()) {
				lsSolicitacao.set(i, solic);
				int difTamanhoAlocado = aux.getTamanhoAlocado() - solic.getTamanhoAlocado();
				if (lsSolicitacao.get(i + 1).isLiberado()) {
					lsSolicitacao.get(i + 1)
							.setInicioAlocacao(lsSolicitacao.get(i + 1).getInicioAlocacao() - difTamanhoAlocado);
					lsSolicitacao.get(i + 1)
							.setTamanhoAlocado(lsSolicitacao.get(i + 1).getTamanhoAlocado() + difTamanhoAlocado);
				}
				inicioAlocacaoBloco = solic.getFinalAlocacao();

			}
			// verifica se há alguma partição liberada, verificando se o tamanho da
			// solicitação é menor que o tamanho da partição;
			else if (aux.isLiberado() && aux.getTamanhoAlocado() > solic.getTamanhoAlocado()) {
				int posicao = lsSolicitacao.indexOf(aux);
				splitBloco(posicao, solic);
				inicioAlocacaoBloco = solic.getFinalAlocacao();
			}
		}
		// adiciona no final do bloco verificando se há espaço suficiente.
		if (inicioAlocacaoBloco <= finalBloco) {
			lsSolicitacao.add(solic);
		} else {
			//não tem mais espaço no bloco, restante da menoria é adicionada no bloco final como livre
			Solicitacao solFinal = new Solicitacao();
			solFinal.setTamanhoAlocado(finalBloco - lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao());
			solFinal.setFinalAlocacao(finalBloco);
			solFinal.setLiberado(true);
			solFinal.setInicioAlocacao(lsSolicitacao.get(lsSolicitacao.size() - 1).getFinalAlocacao());
			solFinal.setBloco("Livre");
			lsSolicitacao.add(solFinal);
			lsSolicEspera.add(solic);

		}

	}

	// libera uma partição setando o booleando para poder reescrever e também
	// verifica se da para juntar duas partições.
	public static void liberaBloco(String[] linha) {
		int getBloco = Integer.parseInt(linha[1]) -1;
		lsSolicitacao.get(getBloco).setLiberado(true);
		lsSolicitacao.get(getBloco).setBloco("livre");

		juntaBloco(getBloco);
		verificaListaEspera(getBloco);
	}

	public static void verificaListaEspera(int getBloco) {
		int g = 0;
		// verifica na lista de Espera se há alguma solicitação que se encaixe na posição liberada;
		while (g < lsSolicEspera.size()) {
			if (lsSolicEspera.get(g).getTamanhoAlocado()< lsSolicitacao.get(getBloco).getTamanhoAlocado()) {
				lsSolicitacao.set(g, lsSolicEspera.get(g));
				splitBloco(getBloco, lsSolicEspera.get(g));
				break;
			} else if (lsSolicEspera.get(g).getTamanhoAlocado() == lsSolicitacao.get(getBloco).getTamanhoAlocado()) {
				lsSolicitacao.set(g, lsSolicEspera.get(g));
				break;
			}
			g++;
		}
		
		
	}

	// junta dois blocos e empurra a linked list para a esquerda, i = i + 1;
	public static void juntaBloco(int bloco) {

		// verifica se bloco acima está livre para juntar;
		if (lsSolicitacao.get(bloco - 1).isLiberado() && (bloco -1) >= 0) {
			lsSolicitacao.get(bloco - 1).setTamanhoAlocado(
					lsSolicitacao.get(bloco).getTamanhoAlocado() + lsSolicitacao.get(bloco - 1).getTamanhoAlocado());
			lsSolicitacao.get(bloco - 1).setFinalAlocacao(lsSolicitacao.get(bloco).getFinalAlocacao());
			for (int i = bloco; i < lsSolicitacao.size(); i++) {
				if(i < lsSolicitacao.size() - 1) {
					lsSolicitacao.set(i, lsSolicitacao.get(i + 1));
				}
			}
			// remove o ultimo elemento da lista, pois estará duplicado;
			lsSolicitacao.removeLast();
			// verifica se a bloco abaixo está livre para juntar
			//dúvida oolhar o baixo aqui oou não?????????????????????????
//			if(lsSolicitacao.get(bloco).isLiberado()){
//				lsSolicitacao.get(bloco - 1).setTamanhoAlocado(
//						lsSolicitacao.get(bloco).getTamanhoAlocado() + lsSolicitacao.get(bloco - 1).getTamanhoAlocado());
//				lsSolicitacao.get(bloco - 1).setFinalAlocacao(lsSolicitacao.get(bloco).getFinalAlocacao());
//				for (int i = bloco; i < lsSolicitacao.size(); i++) {
//					if(i < lsSolicitacao.size() - 1) {
//						lsSolicitacao.set(i, lsSolicitacao.get(i + 1));
//					}
//				}
//				// remove o ultimo elemento da lista, pois estará duplicado;
//				lsSolicitacao.removeLast();
//			}
		} 
		//TÁ DANDO ERRO AQUI. ELE CAI DENTRO DO IF, MAS NÃO ERA PRA CAIR;
		else if (lsSolicitacao.get(bloco + 1) != null && lsSolicitacao.get(bloco + 1).isLiberado() && (bloco + 1 <= lsSolicitacao.size())) {

			lsSolicitacao.get(bloco).setTamanhoAlocado(
					lsSolicitacao.get(bloco + 1).getTamanhoAlocado() + lsSolicitacao.get(bloco).getTamanhoAlocado());
			lsSolicitacao.get(bloco).setFinalAlocacao(lsSolicitacao.get(bloco + 1).getFinalAlocacao());
			for (int i = bloco + 1; i < lsSolicitacao.size(); i++) {
				if(i < lsSolicitacao.size() - 1) {
					lsSolicitacao.set(i, lsSolicitacao.get(i + 1));
				}
			}
			// remove o ultimo elemento da lista, pois estará duplicado;
			lsSolicitacao.removeLast();
		}

	}

	// divide um bloco em dois e empurra para a direita, i+1 = i; Por padrão o bloco
	// livre ficará depois do bloco ocupado.
	public static void splitBloco(int posicao, Solicitacao solic) {
		//cria um bloco vazio com o resto do tamanho da alocação;
		Solicitacao restoDoBloco = new Solicitacao(); 
		restoDoBloco.setTamanhoAlocado(lsSolicitacao.get(posicao).getTamanhoAlocado() - solic.getTamanhoAlocado());
		restoDoBloco.setInicioAlocacao(solic.getFinalAlocacao());
		restoDoBloco.setFinalAlocacao(solic.getFinalAlocacao() + restoDoBloco.getTamanhoAlocado());
		restoDoBloco.setLiberado(true);
		//coloca a solicitação na posição;
		lsSolicitacao.set(posicao, solic);
		//adiciona a solicitação no final do bloco
		lsSolicitacao.add(restoDoBloco);
		// empurra a lista para a direita, no qual a posição i + 1 = i;
		for (int i= posicao+2; i< lsSolicitacao.size(); i++) {
			lsSolicitacao.set(i, lsSolicitacao.get(i-1));
		}
		// (posição +1) ficará igual, pois a iteraçã só começa em (posição +2), assim adicionamos esse bloco vazio na posição que não foi modificada.
		lsSolicitacao.set(posicao+1, restoDoBloco);
	}

	// após o termino da leitura do arquivo verifica fragmentação;
	public static void verificaFragmentacao() {
		String resultado = "";
		int memoriaLivre = 0;
			for (int i = 0; i < lsSolicitacao.size(); i++) {
				System.out.println(lsSolicitacao.get(i).toString());
				if (lsSolicitacao.get(i).isLiberado()) {
					memoriaLivre += lsSolicitacao.get(i).getTamanhoAlocado();
				}
				for (int j = 0; j< lsSolicEspera.size(); j++) {
					if (lsSolicEspera.get(j).getTamanhoAlocado() <= memoriaLivre) {
						resultado = memoriaLivre + "livres, " + lsSolicEspera.get(j).getTamanhoAlocado() + " solicitados - Fragmentação Externa";
						break;
					}
				}
			}
			System.out.println(resultado);
	}

}