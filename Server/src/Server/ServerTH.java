package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Classe ServerTH, si occupa di:
 * Gestire i Thread lanciati della classe Server;
 * Gestire la chiusura di una comunicazione da parte di un client, con relativo messaggio di disconnessione che viene inoltrat a tutti gli altri clients;
 * Gestire l'inoltro dei messaggi tra i clients.
 * @author Eduardo Romagnoli
 *
 */
public class ServerTH implements Runnable
{
	private Socket client; //Dichiaro la variabile d'appoggio per il valore del Socket
	private BufferedReader in; //B.R. per i valori in input
	private PrintWriter out; //P.R. per i valori in output
	private ArrayList<ServerTH> clients; //ArrayList con tutti i client
	
	/**
	 * Metodo costruttore, si occupa di inizializzare i valori per la classe ServerTH
	 * @param clientsock
	 * @param clients
	 * @throws IOException
	 */
	public ServerTH(Socket clientsock, ArrayList<ServerTH> clients) throws IOException
	{
		this.client = clientsock; //inizializzo la variabile client con il valore passato dalla classe server
		this.clients = clients; //inizializzo l'ArrayList clients con i valori passati dalla classe server
		in = new BufferedReader(new InputStreamReader(client.getInputStream())); //Inizializzo il B.R. in modo che possa prendere i dati passati da client: variabile in
		out = new PrintWriter(client.getOutputStream(), true); //Inizializzo P.R. e passo i valori a client, "true": in modo che "pulisca" il buffer dopo ogni output
	}
	
	/**
	 * Metodo "run", si occupa di gestire i thread e chiamare i metodi di invio dei messaggi e di chiusura della counicazione da parte di un client
	 */
	@Override
	public void run() //Run
	{
		try
		{
			while(true) //Loop
			{
				String inputDalServer = in.readLine(); //Legge i messaggi in input al server e li salva nella variabile "inputDalServer"
				System.out.println("3 - Preparazione Invio messaggio"); //Messaggio
				try //Provo:
				{
					int SpazioPerInvioMessaggio = inputDalServer.indexOf(""); //Controlla i caratteri in valori
					messaggioOut(inputDalServer.substring(SpazioPerInvioMessaggio)); //manda il messaggio in input e la sottostringa composta dal valore al metodo "messaggioOut"
				}
				catch (Exception e) //Eccezione lanciata nel caso un client si sia disconnesso (Continua...)
				{
					break; //Esce dal loop (Continua...)
				}
			}
			chiudiComunicazione(); //chiama il metodo chiuduComunicazione() (fine)
		}
		catch (IOException e) //Eccezione lanciata nel caso i valori reiterati nel loop presentino degli errori
		{
			System.out.println(e.getMessage()); 
			System.out.println("Errore Thread - errorre riconoscimento messaggio"); //Messaggio eccezione
			System.exit(1); //Chiude il programma
		}
		try 
		{
			in.close(); //Chiude il Buffer
			out.close(); //Chiude il Buffer
		} 
		catch (IOException e) //Eccezione lanciata nel caso si presentino degli errori nella chiusura del buffer
		{
			System.out.println(e.getMessage());
			System.out.println("Errore Thread - errorre nella chiusura del buffer"); //Messaggio eccezione
			System.exit(1); //Chiude il programma
		}
	}
	
	/**
	 * Metodo "messaggioOut" si occupa di inoltrare i messaggi agli utenti
	 * @param messaggio
	 */
	private void messaggioOut(String messaggio) //Metodo che serve a inviare il messaggio a tutti i client
	{
		for(ServerTH aTutti : clients) //For:each -> scorre i valori dell'ArrayList (necessario in questo caso per alleggerire il codice)
		{
			System.out.println("4 - Invio messaggio"); //Messaggio per console server
			aTutti.out.println(messaggio); //Invia i messaggi a tutti i client
		}
	}
	
	/**
	 * Metodo "chiudiComunicazione", si occupa di mandare il messaggio relativo ai client online quando un client si disconnette
	 * si occupa di cancellare il valore del client nell'ArrayList
	 */
	public void chiudiComunicazione() //Metodo che serve per chiudere mandare i relativi messaggi di client scollegati e rimuovere i dati dall'ArrayList
	{
		int i = 0; //Dichiarazione valore int (per iterazione)
		System.out.println("5 - Client disconnesso"); //Messaggio per console server 
		for(ServerTH errore_per_tutti : clients) //For:each -> scorre i valori dell'ArrayList (necessario in questo caso per alleggerire il codice)
		{
			errore_per_tutti.out.println("SERVER - Il secondo client si è disconnesso, i messaggi che invierà non saranno inoltrati"); //Messaggio per i client
			i++; //Incremento valore per prendere nota del valore dell'ArrayList
			clients.remove(i); //Rimuove il valore nella posizione data
			System.out.println("6 - Cancellato valore nell'arraylist"); //Messaggio per console server 
		}
	}
}
