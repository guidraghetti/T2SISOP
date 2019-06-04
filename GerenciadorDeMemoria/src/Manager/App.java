package Manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//lê o arquivo texto e cria os objetos com as solicitações;
public class App {
	LinkedListOfBlocos bloco = new LinkedListOfBlocos();
	public static void main (String [] args) {
		
	}
	
	public void leArquivo () throws IOException {
		int modo;
		int tamanhoDoBloco;
		
		FileReader lerArquivo = new FileReader("t1.txt");
		BufferedReader s = new BufferedReader(lerArquivo);
	
		String line;
		line = s.readLine();
		modo = Integer.parseInt(line);
		line = s.readLine();
		bloco.inicioBloco = Integer.parseInt(line);
		line = s.readLine();
		bloco.finalBloco = Integer.parseInt(line);
		bloco.tamMemoria = (bloco.finalBloco - bloco.inicioBloco); 
		line = s.readLine();
		
		int bloco = 1;
		
		while (line !=null) {
			String [] linha = line.trim().split(" ");
			if (linha[0] == "S") {
				
			} else {
				
			}
 			
		}
	}
}
