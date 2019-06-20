package Manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//lê o arquivo texto e cria os objetos com as solicitações;
public class App {
	public static int inicioBloco;
	public static int finalBloco;
	public static int tamMemoria;
	public static int inicioAlocacaoBloco;
	public static int bloco = 1;
	public static LinkedList<Solicitacao> lsSolicitacao = new LinkedList();
	
	public static void main(String[] args) throws IOException {

		FileReader lerArquivo = new FileReader("t1.txt");
		BufferedReader s = new BufferedReader(lerArquivo);

		String line;
		s.readLine();
		line = s.readLine();
		
		
		inicioBloco = Integer.parseInt(line);
		line = s.readLine();
		finalBloco = Integer.parseInt(line);
		tamMemoria = (finalBloco - inicioBloco);
		line = s.readLine();
		inicioAlocacaoBloco = inicioBloco;


		while (line != null) {
			
			String[] linha = line.trim().split(" ");
			System.out.println(linha[0]);
			if (linha[0].equals("S")) {
				criaSolicitacao(linha);
				

			} else {
				removeBloco(linha);

			}

		} 
		s.close();
	}

	public static void criaSolicitacao(String[] linha) {
		Solicitacao solic = new Solicitacao();
		solic.setBloco(bloco);
		solic.setInicioAlocacao(inicioAlocacaoBloco);
		solic.setTamanhoAlocado(Integer.parseInt(linha[1]));
		solic.setFinalAlocacao(inicioAlocacaoBloco + solic.getTamanhoAlocado());
		bloco++;
		adicionaNaLista(solic);
	}

	public static void adicionaNaLista(Solicitacao solic) {
		for (int i = 0; i < lsSolicitacao.size(); i++) {
			System.out.println(lsSolicitacao.get(i));
			Solicitacao aux = lsSolicitacao.get(i);

			if (aux.isLiberado() && aux.getTamanhoAlocado() >= solic.getTamanhoAlocado()) {
				lsSolicitacao.set(i, solic);
				int difTamanhoAlocado = aux.getTamanhoAlocado() - solic.getTamanhoAlocado();
				if (lsSolicitacao.get(i + 1).isLiberado()) {
					lsSolicitacao.get(i + 1).setInicioAlocacao(
							lsSolicitacao.get(i + 1).getInicioAlocacao() - difTamanhoAlocado);
					lsSolicitacao.get(i + 1).setTamanhoAlocado(
							lsSolicitacao.get(i + 1).getTamanhoAlocado() + difTamanhoAlocado);
				}
			}
		}
		inicioAlocacaoBloco = solic.getFinalAlocacao();
		
	}

	public static void removeBloco(String [] linha) {
		int getBloco = Integer.parseInt(linha[1]);
		lsSolicitacao.get(getBloco).setLiberado(true);
	}

	public void juntaBloco() {

	}

	public void splitBloco() {

	}

}