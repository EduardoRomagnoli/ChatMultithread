package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Classe ClientTH, si occupa di:
 * Gestire i thread dei clients;
 * Gestire i messaggi in output dai clients.
 * @author Eduardo Romagnoli
 *
 */
public class ClientTH implements Runnable
{
	private Socket server; //Creo la variabile socket server
	private BufferedReader in; //Creo il b.r. in

	/**
	 * Costruttore della class ClientTH, si occupa di inizializzare i valori: "s" è il socket che viene passato dalla classe client appena si collega con il server 
	 * @param s
	 * @throws IOException
	 */
	public ClientTH(Socket s) throws IOException //Costruttore: passo il valore del socket dalla classe "Client"
	{
		server = s; //Inizializzo la variabile socket con la variabile d'appoggio s
		in = new BufferedReader(new InputStreamReader(server.getInputStream())); //Inizializzo il b.r. dicendo che deve prendere i messaggi dal socket di riferimento
	}

	/**
	 * Metodo che si occupa di runnare i threads
	 */
	@Override
	public void run() //Run
	{
		try 
		{
			while(true) //Loop
			{
				String outputDalServer = in.readLine(); //Prendo i valori mandati in output dal server e li salvo nella variabile "outputDalServer"
				if(outputDalServer == null) //Controllo se il valore è nullo serve per la chiusura della connessione (controllare classe Client), in questo caso:
				{
					break; //esco dal loop(1*)
				}
				System.out.println("> " + outputDalServer); //Messaggio client che ritorna il valore inviato dal server
			}
		}
		catch (IOException e) //Eccezione lanciata nel caso che ci siano errori nella parte di codice del loop (controllare testo del messaggio per più info)
		{
			e.printStackTrace();
		}
		try 
		{
			in.close(); //chiudo il b.r., la connessione con il server (1*)
		}
		catch (IOException e) //Eccezione lanciata nel caso che ci siano degli errori nella chiusura della comunicazione con il server (controllare testo del messaggio per più info)
		{
			System.out.println(e.getMessage()); 
			System.out.println("Errore durante la chiusura della comunicazione con il server."); //Messaggio per client
		}
	}
}
