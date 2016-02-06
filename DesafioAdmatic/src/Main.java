import java.util.Random;
import java.util.Scanner;


class Consumidor extends Thread {
	private volatile Buffer buffer;
	public Consumidor(Buffer buffer, String Nome) {
		super(Nome);
		this.buffer = buffer;
		
	}
	
	public void run() {
		int dados;
		System.out.println(getName()+" criado com sucesso.");
		while (true) {
			dados = buffer.consome(getName());
		}
	}
}

class Produtor extends Thread {
	private volatile Buffer buffer;
	public Produtor (Buffer buffer, String Nome) {
		super(Nome);
		this.buffer = buffer;
	}
	public void run() {
		Random prod = new Random();
		System.out.println(getName()+" criado com sucesso.");
		while (true) {
			int n = prod.nextInt();
			buffer.produz(n, getName());
		}
	}
}

class Buffer {
	private volatile int dados;
	private volatile boolean vazio;
	
	public Buffer() {
		this.vazio = true;
	}
	
	public synchronized void produz(int novosDados, String Nome) {
		while (!this.vazio) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.dados = novosDados;
		this.vazio = false;
		this.notifyAll();
		System.out.println("O "+Nome+" produziu: " + novosDados);
	}
	public synchronized int consome(String Nome) {
		while (this.vazio) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.vazio = true;
		this.notifyAll();
		System.out.println("O "+Nome+" consumiu: " +dados);
		return dados;
	}
}

public class Main {
	public static void main(String[] args) {
		Scanner entrada = new Scanner(System.in);
		Buffer buffer = new Buffer();
		System.out.println("Insira o número de Threads de Produtor: ");
		int pdt = entrada.nextInt();
		System.out.println("\nVocê selecionou "+pdt+" Threads.");
		System.out.println("\nInsira o número de Threads de Consumidor: ");
		int con = entrada.nextInt();
		System.out.println("\nVocê selecionou " +con+" Threads de Consumidor.");
		int x = 0;
		int y = 0;
		entrada.close();
		while (y < con) {
			Consumidor temp2 = new Consumidor(buffer, "Consumidor #"+(y+1));
			temp2.start();
			y++;
		}
		
		while (x < pdt) {
			Produtor temp = new Produtor(buffer, "Produtor #"+(x+1));
			temp.start();
			x++;
		}
				
	}
}