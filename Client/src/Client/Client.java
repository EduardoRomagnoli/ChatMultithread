package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * La classe Client si occupa di gestire:
 * La connessione al server;
 * L'apertura dei thread del client, classe "ClientTH", questi servono per gestire pi� di un client per volta;
 * La gestione del comando "esci" che serve per far disconnettere il client dal server (chiude il server socket).
 * @author Eduardo Romagnoli
 * 
 */

public class Client 
{
	/**
	 * Metodo main della classe "Client" si occupa di inizializzare i vari client e di lanciare i thread
	 * @param args 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		Socket sock = new Socket("localhost", 3333); //Connessione al Socket
		ClientTH thcl = new ClientTH(sock); //Creazione Thread client
		(new Thread(thcl)).start(); //Apertura Thread client
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //B.R. che legge i valori mandati in chat (client)
		PrintWriter out = new PrintWriter(sock.getOutputStream(), true); //P.W. che manda i valori scritti dal client al socket, "true" per ripulire il b.r. dopo il passaggio dei dati
		System.out.println(" - SERVER INFO - \n 1 - usa '@nomeutente' per inviare un messaggio privato\n 2 - usa <<esci>> per disconnettersi\n");
		while(true) //Loop
		{
			System.out.println("> "); //Messaggi mandati dall'Utente
			String comandi = br.readLine(); //Leggo il messaggio tramite il b.r. e assegno il valore ricavato in comandi
			if(comandi.equals("esci")) //Controllo se il client ha scritto "esci", in questo caso (continua...)
			{
				System.out.println("Chiusura programma..."); //Messaggio al client
				break; //Esco dal loop (*1)
			}
			if (comandi.startsWith("@"))
			{
				System.out.println("SERVER - Scrivi il messaggio privato da inviare a: " + comandi.substring(1));
			}
			out.println(comandi); //Mando al server il messaggio "comandi"
		}
		sock.close(); //chiudo il socket (*1)
		System.exit(0); //chiudo l'app client (*1)
	}

	/**
	 * Metodo che ha la funzione di assegnare il nome al client appena collegato
	 * @throws IOException
	 */
	public static String getNome() throws Exception
	{
		return null;
	}
}
