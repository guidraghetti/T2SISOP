package Manager;

import java.util.LinkedList;

//recebe o tamanho da mem�ria e cria organiza os blocos de acordo com as solita��es;
//linkedList
public class LinkedListOfBlocos {
	public int tamMemoria;
	public int inicioBloco;
	public int finalBloco;

	public LinkedListOfBlocos() {

	}

	public boolean verifcaEspaco(int tamanhoAlocado) {

		if (tamanhoAlocado <= tamMemoria) {
			tamMemoria -= tamanhoAlocado;
			return true;

		} else {
			return false;
		}
	}
	
	public boolean verificaFragmentacao() {
		return true;
	}
	
	public void adicionaNaMemoria() {
		
	}

}