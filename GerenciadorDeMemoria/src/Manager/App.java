package Manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

//lê o arquivo texto e cria os objetos com as solicitações;
public class App {
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
		tamanhoDoBloco = Integer.parseInt(line);
		line = s.readLine();
		tamanhoDoBloco = (tamanhoDoBloco - Integer.parseInt(line)) * (-1); 
		line = s.readLine();
		
		int bloco = 1;
		
		while (line !=null) {
			String [] linha = line.trim().split(" ");
			
		}
	}
}
