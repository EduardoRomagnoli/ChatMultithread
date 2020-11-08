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

public class ClientTHGUI extends ClientGUI implements Runnable
{
	public ClientGUI pannello;
	private Socket s;
	private BufferedReader br;
	public JLabel msg;
	public JLabel clients;
	
	public ClientTHGUI(Socket s, ClientGUI pannelloIn, JLabel msgIn, JLabel clientsIn) throws Exception
	{
		super(null, s);
		s = s; 
		this.pannello = pannelloIn;
		this.clients = clientsIn;
		this.msg = msgIn;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	
	@Override
	public void run() 
	{
			String testo = "";
			TrayIcon Tray = ClientTH.Lanciatrayicon();
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
				if (Inp.startsWith("Online: ") || Inp.equals("0 online"))
				{
					ClientGUI.client.setText(Inp);
				}
				else if (Inp.equals("ERRORE - Nome utente già esistente"))
				{
					JOptionPane.showMessageDialog(null, "ERRORE - Nome utente già esistente", null,  JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					ClientTH.WindowsNOTF(Inp, Tray);
					testo = testo + Inp;
					ClientGUI.comandi.setText("> " + testo);
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