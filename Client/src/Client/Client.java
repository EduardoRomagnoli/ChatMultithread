package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client 
{
	public static void main(String[] argas) throws UnknownHostException, IOException 
	{
		Socket sock = new Socket("localhost", 3333); //Connessione al Socket
		ClientTH thcl = new ClientTH(sock); //Creazione Thread client
		(new Thread(thcl)).start(); //Apertura Thread client
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); //B.R. che legge i valori mandati in chat (client)
		PrintWriter out = new PrintWriter(sock.getOutputStream(), true); //P.W. che manda i valori scritti dal client al socket, "true" per ripulire il b.r. dopo il passaggio dei dati
		
		String NomeUtent = nome(thcl); //Richiamo il metodo nome() passandogli come valore il valore del thread, il return del metodo lo assegno alla variabile NomeUtent
		
		while(true) //Loop
		{
			System.out.println(NomeUtent + "> "); //Messaggi mandati dall'Utente
			String comandi = br.readLine(); //Leggo il messaggio tramite il b.r. e assegno il valore ricavato in comandi
			if(comandi.equals("esci")) //Controllo se il client ha scritto "esci", in questo caso (continua...)
			{
				System.out.println("Chiusura programma..."); //Messaggio al client
				break; //Esco dal loop (*1)
			}
			out.println(comandi); //Mando al server il messaggio "comandi"
		}
		sock.close(); //chiudo il socket (*1)
		System.exit(0); //chiudo l'app client (*1)
	}
	
	public static String nome(ClientTH thread) throws IOException //Metodo di assegnazione nome utente
	{ 
		System.out.println("Inserisci il tuo nome: "); //Messaggio client
		BufferedReader inputnome = new BufferedReader(new InputStreamReader(System.in)); //Inizializzo il b.r. che si occuperà di prendere il nome messo in input dall'utente
		String NomeUtente = inputnome.readLine(); //Assegno il valore ad una stringa
		return NomeUtente; //ritrorno il valore preso
	}
}
