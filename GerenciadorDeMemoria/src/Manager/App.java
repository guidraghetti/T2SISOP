package Manager;

import java.io.BufferedReader;
import java.util.LinkedList;

import com.sun.org.apache.bcel.internal.generic.LSTORE;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//lê o arquivo texto e cria os objetos com as solicitações;
public class App {
	public static void main (String [] args) {
		
	}
	
	public void leArquivo () throws IOException {
		int tamanhoDoBloco;
		
		FileReader lerArquivo = new FileReader("t1.txt");
		BufferedReader s = new BufferedReader(lerArquivo);
	
		String line;
		line = s.readLine();
		line = s.readLine();
		int inicioBloco = Integer.parseInt(line);
		line = s.readLine();
		int finalBloco = Integer.parseInt(line);
		int tamMemoria = (finalBloco - inicioBloco); 
		line = s.readLine();
		int inicioAlocacaoBloco = inicioBloco;
		
		int bloco = 1;
		
		LinkedList<Solicitacao> lsSolicitacao = new LinkedList();
		
		while (line !=null) {
			String [] linha = line.trim().split(" ");
			if (linha[0] == "S") {
				Solicitacao solic = new Solicitacao();
				solic.setBloco(bloco);
				solic.setInicioAlocacao(inicioAlocacaoBloco);
				solic.setTamanhoAlocado(Integer.parseInt(linha[1]));
				solic.setFinalAlocacao(inicioAlocacaoBloco + solic.getTamanhoAlocado());
				bloco ++;
				for (int i= 0; i< lsSolicitacao.size(); i++) {
					Solicitacao aux = lsSolicitacao.get(i);
					
					if(aux.isLiberado() && aux.getTamanhoAlocado() >= solic.getTamanhoAlocado()) {
						lsSolicitacao.set(i, solic);
					int difTamanhoAlocado = aux.getTamanhoAlocado() - solic.getTamanhoAlocado();
					if (lsSolicitacao.get(i+1).isLiberado()) {
						lsSolicitacao.get(i+1).setInicioAlocacao(lsSolicitacao.get(i+1).getInicioAlocacao() - difTamanhoAlocado);
						lsSolicitacao.get(i+1).setTamanhoAlocado(lsSolicitacao.get(i+1).getTamanhoAlocado()+ difTamanhoAlocado);
					}
					}
				}
				inicioAlocacaoBloco = solic.getFinalAlocacao();
				
			} else {
				int getBloco = Integer.parseInt(linha[1]);
				lsSolicitacao.get(getBloco).setLiberado(true);
				
				
			}
 			
		}
	}
}