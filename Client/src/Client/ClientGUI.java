package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Classe GUI del client
 * @author Eduardo Romagnoli
 *
 */

public class ClientGUI extends JFrame 
{
	public JPanel Pannello;
	public Socket s;
	public PrintWriter out;
	private static String clients;
	public static JLabel comandi;
	public static JLabel client;

	/**
	 * Metodo main che inizializza il socket e le variabili e chiama i Thread
	 * @param args 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		Socket s = new Socket ("localhost", 3333);
		PrintWriter out = new PrintWriter(s.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream())); 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
		String nameinput = "";
		String scomandi = ""; 
		clients = in.readLine();
		System.out.println(clients);
		do 
		{
			try 
			{
				System.out.println(in.readLine());
				nameinput = JOptionPane.showInputDialog("Inserisci il nome utente");
				out.println(nameinput);
				Thread.sleep(1000);
			} catch (Exception e) 
			{
				JOptionPane.showMessageDialog(null, "ERRORE - Messaggio in uscita non valido", null,  JOptionPane.ERROR_MESSAGE);
			}
			scomandi = in.readLine();
			if (!scomandi.equals("11 - Nome utente impostato"))
			{
				JOptionPane.showMessageDialog(null, scomandi, "Messaggi in Arrivo",  JOptionPane.ERROR_MESSAGE);
			}
		} 
		while (!(scomandi.equals("11 - Nome utente impostato")));
		ClientGUI frame = new ClientGUI(out, s);
		frame.setVisible(true);
		ClientTHGUI THCL = new ClientTHGUI (s, frame, comandi, client);
		(new Thread(THCL)).start();
	}

	/**
	 * Costruttore della classe che inizializza la grafica della tabella
	 * @param out output 
	 * @param s socket client
	 */
	public ClientGUI(PrintWriter out, Socket s) 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 753, 800);
		Pannello = new JPanel();
		Pannello.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(Pannello);
		Pannello.setLayout(new BorderLayout(0, 0));
		Pannello.setBackground(Color.CYAN);
		JTextField insmesstx;
		insmesstx = new JTextField();
		comandi = new JLabel();
		client = new JLabel(clients);
		JScrollPane textPane = new JScrollPane(comandi, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		insmesstx.setToolTipText(" - SERVER INFO -\n 1 - usa '@nomeutente' per inviare un messaggio privato\n 2 - usa <<esci>> per disconnettersi\n");
		Pannello.add(insmesstx, BorderLayout.SOUTH);
		Pannello.add(textPane);
		Pannello.add(client, BorderLayout.NORTH);
		insmesstx.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					try 
					{
						MessaggiOut(insmesstx, out, s);
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * MessaggiOut, metodo chd invia i messaggi e nel caso sia presente il messaggio "esci" allora esce dal programma
	 * @param messaggio messaggio da inviare
	 * @param in output verso il server
	 * @param s socket del client
	 * @throws IOException 
	 */
	public void MessaggiOut (JTextField messaggio, PrintWriter in, Socket s) throws IOException
	{
		String inp = messaggio.getText();
		System.out.println(inp);
		messaggio.setText("");
		if(inp.contains("esci"))
		{
			System.out.println("Chiusura programma...");
			s.close(); 
			System.exit(0);
		}
		else
		{
			in.println(inp);
		}
	}
}