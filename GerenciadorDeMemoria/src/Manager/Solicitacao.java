package Manager;

//recebe os dados das solicitações, como tamanho da alocação ou liberação e qual bloco;
//Cria os nodos que estarão na LinkedList;
public class Solicitacao {
	
	private int bloco;
	private int tamanhoAlocado;
	private int inicioAlocacao;
	private int finalAlocacao;
	private boolean liberado = false;
	
	public Solicitacao() {
		
	}

	public Solicitacao(int bloco, int tamanhoAlocado, int inicioAlocacao, int finalAlocacao, boolean liberado) {
		super();
		this.bloco = bloco;
		this.tamanhoAlocado = tamanhoAlocado;
		this.inicioAlocacao = inicioAlocacao;
		this.finalAlocacao = finalAlocacao;
		this.liberado = liberado;
	}

	public int getBloco() {
		return bloco;
	}

	public void setBloco(int bloco) {
		this.bloco = bloco;
	}

	public int getTamanhoAlocado() {
		return tamanhoAlocado;
	}

	public void setTamanhoAlocado(int tamanhoAlocado) {
		this.tamanhoAlocado = tamanhoAlocado;
	}

	public int getInicioAlocacao() {
		return inicioAlocacao;
	}

	public void setInicioAlocacao(int inicioAlocacao) {
		this.inicioAlocacao = inicioAlocacao;
	}

	public int getFinalAlocacao() {
		return finalAlocacao;
	}

	public void setFinalAlocacao(int finalAlocacao) {
		this.finalAlocacao = finalAlocacao;
	}

	public boolean isLiberado() {
		return liberado;
	}

	public void setLiberado(boolean liberado) {
		this.liberado = liberado;
	}

	@Override
	public String toString() {
		return "Solicitacao [bloco=" + bloco + ", tamanhoAlocado=" + tamanhoAlocado + ", inicioAlocacao="
				+ inicioAlocacao + ", finalAlocacao=" + finalAlocacao + ", liberado=" + liberado + "]";
	}
	
	
	
	
	
	
}
