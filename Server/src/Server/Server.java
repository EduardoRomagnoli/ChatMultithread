package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Classe che si occupa di:
 * Aprire il socket;
 * Aspettare che i client si colleghino al socket;
 * Creare la thread pool e avviarla;
 * Avviare i Thread per poi lanciarli con il codice contenuto nella classe "ServerTH".
 * @author Eduardo Romagnoli
 *
 */
public class Server 
{
	private static final int porta = 3333; //Dichiarazione della variabile porta (porta del socket)
	static ArrayList<ServerTH> clients1 = new ArrayList<>(); //Dichiarazione dell'ArrayList
	private static ExecutorService pool = Executors.newFixedThreadPool(10); //Dichiarazione della Thread Pool, creo dei thread per poi essere lanciati
	private static ServerSocket servsock; //Variabile che conterrà il ServerSocket
	
	/**
	 * Metodo main (vedere descrizione classe)
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException 
	{
		servsock = new ServerSocket(porta); //Apro il server Socket ed è in ascolto sulla porta inizializzata alla variabile "porta"
		System.out.println("1 - Server in attesa del client, in ascolto sulla porta: " + porta); //Messaggio

		while(true) //Loop
		{
			Socket client = servsock.accept(); //Accettata la connessione
			System.out.println("2 - Connessione stabilita con" + client); //Lessaggio
			ServerTH ths = new ServerTH(client, clients1); //Creato Nuovo oggetto del Thread (Server)
			clients1.add(ths); //Aggiunto l'oggetto thread all'ArrayList
			pool.execute(ths); //Eseguito l'oggetto thread 
		}
	}
}
