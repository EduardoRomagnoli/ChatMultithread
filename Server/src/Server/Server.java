package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server 
{
	private static final int porta = 3333; //Dichiarazione della variabile porta (porta del socket)
	private static ArrayList<ServerTH> clients = new ArrayList<>(); //Dichiarazione dell'ArrayList
	private static ExecutorService pool = Executors.newFixedThreadPool(10); //Dichiarazione della Thread Pool, creo dei thread per poi essere lanciati
	private static ServerSocket servsock; //Variabile che conterr� il ServerSocket
	
	public static void main(String[] args) throws IOException 
	{
		servsock = new ServerSocket(porta); //Apro il server Socket ed � in ascolto sulla porta inizializzata alla variabile "porta"
		System.out.println("1 - Server in attesa del client, in ascolto sulla porta: " + porta); //Messaggio

		while(true) //Loop
		{
			Socket client = servsock.accept(); //Accettata la connessione
			System.out.println("2 - Connessione stabilita con" + client); //Lessaggio
			ServerTH ths = new ServerTH(client, clients); //Creato Nuovo oggetto del Thread (Server)
			clients.add(ths); //Aggiunto l'oggetto thread all'ArrayList
			pool.execute(ths); //Eseguito l'oggetto thread 
		}
	}
}