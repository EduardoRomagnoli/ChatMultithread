package Client;

import java.util.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.TrayIcon.MessageType;

/**
 * Classe di gestione messaggi client server e gestione GUI
 * @author Eduardo Romagnoli
 *
 */

public class ClientTHGUI extends ClientTH implements Runnable
{
	public ClientGUI pannello;
	private Socket s;
	private BufferedReader br;
	public JLabel msg;
	public JLabel clients;

	/**
	 * Costruttore della classe GUI del Thread client
	 * @param s
	 * @param pannelloIn
	 * @param msgIn
	 * @param clientsIn
	 * @throws Exception
	 */
	public ClientTHGUI(Socket s, ClientGUI pannelloIn, JLabel msgIn, JLabel clientsIn) throws Exception
	{
		super(s);
		this.s = s; 
		this.pannello = pannelloIn;
		this.clients = clientsIn;
		this.msg = msgIn;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	/**
	 * Metodo run che lancia i Thread del client
	 */
	@Override
	public void run() 
	{
		String testo = "";
		while(true)
		{
			String Inp = ""; 
			try 
			{
				Inp = br.readLine(); 
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			if(Inp == "") 
			{
				break;
			}
			if (Inp.startsWith("SERVER - Online:") || Inp.equals("SERVER - 0 utenti online"))
			{
				ClientGUI.client.setText(Inp);
			}
			else if (Inp.equals("9 - Nome già presente all'interno della chat"))
			{
				JOptionPane.showMessageDialog(null, "ERRORE - Nome utente già esistente", null,  JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				testo = testo + "<br>" + Inp;
				ClientGUI.comandi.setText("<html>" + "> " + testo + "</html>");
			}
		}
		try
		{
			br.close();
		}
		catch (Exception e)
		{
			System.out.println("Errore (logout)");
		}
	}	
}