package Manager;

public class Nodo {
	Nodo head;

	Solicitacao data;
	Nodo prev;
	Nodo next;
	int count;

	Nodo(Solicitacao d) {
		data = d;
	}
}
