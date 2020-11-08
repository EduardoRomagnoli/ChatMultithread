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
	private String NomeUtente;

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
		out.println(nameList());
		setNome();
		try
		{
			while(true) //Loop
			{
				String input = "";
				input = in.readLine(); int index = input.indexOf(""); // lettura input
				if (input.startsWith("@"))
				{
					String recipient = input.substring(1); int index2 = recipient.indexOf("");
					input = "";
					input = in.readLine(); index = input.indexOf(""); // lettura input
					messaggioOut(input.substring(index), recipient.substring(index2));
				}
				else 
				{
					messaggioOut(input.substring(index)); // passa il messaggio al metodo output
				}

			}
		}
		catch (IOException e) //Eccezione lanciata per verificare i client disconnessi senza comando esci
		{
			System.out.println(e.getMessage()); 
			System.out.println("Errore Thread - un client si è disconnesso"); //Messaggio eccezione
			chiudiComunicazione(); //chiama il metodo chiudiComunicazione() per rimuovere il valore dall'array e per chiudere la comunicazione
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
	 * Metodo che si occupa di inoltrare i messaggi one-to-one	
	 * @param message
	 * @param recipient
	 */
	private void messaggioOut(String message, String recipient)
	{
		String messagetime = "" + java.time.LocalTime.now();
		messagetime = messagetime.substring(0, 5);
		for(ServerTH Lista : clients)
		{
			if (recipient.equals(Lista.getNome()))
			{
				Lista.out.println(NomeUtente + "> "+ message + "\t"); // invio del messaggio
			}
		}
		System.out.println("7 - Messaggio inviato da " + NomeUtente);
	}

	/**
	 * Ritorna il valore del nome dell'utente
	 * @return
	 */
	public String getNome()
	{
		return NomeUtente;
	}

	/**
	 * Assegna nome utente
	 */
	private void setNome()
	{
		String clstring = null;
		boolean controllo = false;
		while (!(controllo))
		{
			controllo = true;
			out.println("SERVER - Inserire nome utente:");
			try 
			{
				clstring = in.readLine();
				clstring = clstring.replace("\n", "").replace("\r", "");
				if (clstring.equals(""))
				{
					out.println("8 - Utente non valido, per favore inserisci un nome diverso");
					controllo = false;
					continue;
				}
				for (ServerTH clientsNonArray : clients)
				{
					if (clstring.equals(clientsNonArray.getNome()))
					{
						out.println("9 - Nome già presente all'interno della chat");
						controllo = false;
						break;
					}
				}
			}
			catch (IOException e)
			{
				out.println("10 - Errore input");
			}
		}
		this.NomeUtente = clstring;
		AggiornaSessione();
		out.println("11 - Nome utente impostato");
		for(ServerTH clientsNonArray : clients)
		{
			clientsNonArray.out.println("SERVER -" + NomeUtente + " è online"); 
		}
	}

	/**
	 * Metodo di gestione della lista della persone connesse al server
	 * @return
	 */
	public String nameList()
	{
		String listaNomiUtenti = "";
		for(ServerTH ClientNotArray : clients)
		{
			listaNomiUtenti = listaNomiUtenti + ClientNotArray.getNome() + ", ";
		}
		String tmpstring = listaNomiUtenti.replace(", ", "").replace("null", "");
		if (tmpstring.length()<1)
		{
			listaNomiUtenti = "SERVER - Sembra che oltre a te non ci sia più nessuno connesso in chat";
		}
		else 
		{
			listaNomiUtenti = listaNomiUtenti.replace("null, ", "");
			listaNomiUtenti = listaNomiUtenti.substring(0, listaNomiUtenti.length() - 2);
			listaNomiUtenti = "SERVER - Online:\n" + listaNomiUtenti;
		}
		return listaNomiUtenti;
	}

	/**
	 * Metodo "chiudiComunicazione", si occupa di mandare il messaggio relativo ai client online quando un client si disconnette
	 * si occupa di cancellare il valore del client nell'ArrayList
	 */
	public void chiudiComunicazione()
	{
		System.out.println("12" + NomeUtente + " si è disconnesso dalla chat");
		clients.remove(this);
		for(ServerTH NonArray : clients)
		{
			NonArray.out.println(NomeUtente + " si è disconnesso dalla chat"); 
		}
		AggiornaSessione();
		out.println(nameList());
	}

	/**
	 * Metodo che si occupa di far inviare dai client gli aggiornamenti della sessione attiva
	 */
	public void AggiornaSessione()
	{
		for(ServerTH NonArray : clients) 
		{
			System.out.println("13 - Aggiornamento sessione di chat corrente");
			NonArray.Refresh();
		}
	}

	/**
	 * Aggiorna la sessione attiva
	 */
	public void Refresh()
	{
		clients = Server.clients1;
	}

}

