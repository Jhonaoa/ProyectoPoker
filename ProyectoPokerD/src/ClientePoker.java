import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;



public class ClientePoker extends JFrame  implements  Runnable{

	 JTextArea campoDeTexto; 
	private JTextField tField;
//	private JPanel panelExterior;
//	private JPanel panelInterior; 
//	private JPanel panelCartasFlop;
//	
//	private JPanel panelJugador; 
//	
//	private JButton botonRetirar;
//	private JButton botonPasar; 
//	private JButton botonSubirApuesta;
//	private JButton botonBajarApuesta; 
//	private JButton botonApostar;
//	
//	
//	private JLabel holdem;
//	private JLabel cover; 
	
	private Socket socket; 
	private Scanner entrada;
	private Formatter salida; 
	
	private boolean miTurno ;
	private int  mesaDeJuego; // lA MESA 1 ES HOLDEM --- LA MESA 2 ES POKER CUBIERTO 
	private int posicionEnMesa;  //LAS POSICIONES EN LA MESA VAN DE 0 A 3, AYUDA A ADMINISTRAR TURNOS
	private int valorApuesta;
	private int dineroRestante; 
	private int dineroApostado; 
	private int cartasDescartadas;
	private boolean activoEnRonda; 
	private boolean faseDescarte = true; 
	private String cartasRecibidas;
	private Carta[] cartasFlop;
	private Carta[] cartasMano;
	private String miIdentidad;
	private GridBagConstraints gridBag;
	private String nombreHost ; 

	
//	private final int cliente0 = 0;
//	private final int cliente1 = 1;
//	private final int cliente2 = 2;
//	private final int cliente3 = 3; 
//	private final int cliente4 = 4; 
//	private final int cliente5 = 5; 
//	private final int cliente6 = 6; 
//	private final int cliente7 = 7; 
	
	
	
		
	public ClientePoker(String host, int tipoDeJuego) 
	{
//		super("PROYECTO PI ");
//		nombreHost = host; 
//	
		miTurno = false;
		mesaDeJuego = tipoDeJuego; 
		cartasRecibidas= "";
		miIdentidad ="";
			cartasFlop = new Carta[5];
			cartasMano = new Carta[7];
		for(int i = 0; i<7; i++)
		{
			cartasMano[i] = new Carta(0,0);
		}
		
//		tField = new JTextField(); 
//		tField.setEditable(false);
//		tField.setText("JUEGO DE POKER");
//		add(tField , BorderLayout.NORTH);
		
		Interfaz go = new Interfaz() ; 
		
//		
//		campoDeTexto = new JTextArea(4,30); 
//		campoDeTexto.setEditable(false);
//		add(new JScrollPane (campoDeTexto), BorderLayout.SOUTH );
//		
//		panelExterior = new JPanel () ;
//		panelExterior.setLayout(new BorderLayout () );
//		panelExterior.setBackground(Color.BLACK); 
//		
//		panelJugador = new JPanel () ; 
//		panelJugador.setSize(50,50);
//		panelJugador.setBackground(Color.WHITE);
//		
//		
//		panelCartasFlop = new JPanel (new GridBagLayout() ); 
//		panelCartasFlop.setSize(400,400); 
		
//		GridBagConstraints gridBag = new GridBagConstraints();
//		gridBag.insets = new Insets(10, 10, 10 , 10) ;
//		
//		
//		//panelCartasFlop.setBackground(Color.WHITE);
//		
//		
//		JLabel prueba = new JLabel(); 
//		prueba.setText("Prueba");
//		prueba.setSize(50, 30);
//		
		
		
//		
//		
//		botonRetirar = new JButton ("Retirarse");
//		botonApostar = new JButton ("Apostar: "+ valorApuesta);
//		botonPasar = new JButton ("Pasar");
//	
//		
//		panelInterior = new JPanel(); 
//		panelInterior.setLayout(new BorderLayout() );
//		panelInterior.setBackground(Color.BLUE);
//		panelExterior.add(panelInterior, BorderLayout.CENTER);
//		
//		
//		
//		
//		panelInterior.add(panelJugador, BorderLayout.LINE_START);
//		panelInterior.add(panelCartasFlop, BorderLayout.CENTER);
//		
//		holdem = new JLabel(); 
//		holdem.setText("JUGAR MODO TEXAS HOLD'EM");
//		panelInterior.add(holdem, BorderLayout.WEST);
//		poner tamaÃ±oss
//		cover = new JLabel(); 
//		cover.setText("JUGAR MODO POKER CUBIERTO");
//		panelInterior.add(cover, BorderLayout.EAST);
//		
//		
//		holdem.addMouseListener (this); 
//		cover.addMouseListener(this);

		
		
		
//		add(panelExterior);
		
//		setSize(800,800);
//		setVisible(true);
		
		
	iniciarCliente();
		
		
	}

	
	
	
	public void iniciarCliente() 
	{
		try
		{
			socket = new Socket(InetAddress.getByName(nombreHost), 12345) ;
			
			entrada = new Scanner(socket.getInputStream() );
		
			salida = new Formatter (socket.getOutputStream() );
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
			ExecutorService ejecutar = Executors.newFixedThreadPool(1);
			ejecutar.execute(this); 
	}
	
	

	public void setValorApuesta(int n)
	{
			valorApuesta += n; 
		
	}
	
	public void setDineroRestante (int n ) 
	{
		
			dineroRestante = dineroRestante + n; 
	}
	public void mostrarMesa ()
	{
		
	}
	
	public void leerFlop ()
	{
		int palo = 100;
		int numero = 100;
				
		for (int i = 0 ; i < 2 ; i++)
		{
			if(entrada.hasNextInt())
			{
				palo = entrada.nextInt(); 
				numero = entrada.nextInt();
				
				cartasFlop[i]= new Carta(palo, numero);
				
				
			}
		}
	}
		
	
	
	public void enviarCartasDescarte(int [] posiciones)
	{
		for (int i = 0; i < cartasMano.length ; i++ )
			
		{
			if (cartasMano[i].getDescartar())
			{
//				salida.format("%d\n", cartasMano[i].getPalo() );
//				salida.format("%d\n", cartasMano[i].getNumero());
//				salida.flush();
			}
			
		}
		
		//bandera para informa que ya no hay mas cartas para descarte
		//salida.format("no mas descarte \n");
		//salida.flush();
		
	}
	
@Override
	public void run() 
	{
		salida.format("%d\n", mesaDeJuego);
//		System.out.println(mesaDeJuego);
		salida.flush();
		miIdentidad = entrada.nextLine();
		//System.out.println(miIdentidad);
		cartasRecibidas = entrada.nextLine();
		//System.out.println(cartasRecibidas);
		if(mesaDeJuego == 1)
		{
			recibirCartas(7);
		}
		else if (mesaDeJuego == 2)
		{
			recibirCartas(5);
		}
//		miTurno = (miIdentidad.equals("0"));
		
//		int mensaje = -1; 
//		if(entrada.hasNextInt())
//			mensaje = entrada.nextInt();
//		
//		if (mensaje == 0) // el primer mensaje que recibe es el de aceptaciÃ³n o rechazo en una mesa 
//		{
//			mostrarMensaje("LA MESA QUE DESEAS SE ENCUENTRA LLENA! \n INTENTA DE NUEVO MÃ�S TARDE O PRUEBA OTRA OPCIÃ“N \n");
//			
//		}
//		else  // si es aceptado, tambiÃ©n recibo un tipo de mesa y una posicion en la mesa para el cliente.
//		{
//			mesaDeJuego = entrada.nextInt(); 
//			posicionEnMesa = entrada.nextInt(); 
//			dineroRestante = entrada.nextInt();  
//			
//		}
	
		while (true)
		{
			if(entrada.hasNextLine() )
				procesarMensaje(entrada.nextLine() );
		}
//
	}

	public String sacarParejas(String s, int pos)
	{
		String aux="";
		
		
		aux= Character.toString(s.charAt(pos)) + Character.toString(s.charAt(pos+1));
		
		return aux;
	}

	public void recibirCartas(int num)
	{
		int contador = 0;
		int n =0;
		int p = 0;
	
		for(int i= 0; i<num;i++)
		{
			
				n=Integer.parseInt(String.valueOf(sacarParejas(cartasRecibidas, contador)));
				cartasMano[i].setNumero(n);
				contador+=2;
				p = Integer.parseInt(String.valueOf(cartasRecibidas.charAt(contador)));
				cartasMano[i].setPalo(p);
				contador++;
				
			
		}
		
		
	}

	public void procesarMensaje (String codigo)
	{
	
		
		
		if(codigo.equals("0")) //codigo para recibir el valor de apuesta a igualar, o apuesta mÃ­nima.
		{
			int auxInt= 0; 
//			miTurno = true;
			auxInt= entrada.nextInt(); 
			setValorApuesta(auxInt);
		}
		
		else if(codigo.equals("1") ) //codigo para recibir un STRING para mostrar en el area de texto 
		{
			String auxString = "";
			auxString = entrada.nextLine();
			mostrarMensaje(auxString);  
		}
		else if(codigo.equals("2")) //Codigo para recibe mensajes de String del cliente con la mano ganadora 
		{
			String auxString = entrada.nextLine(); //string con la mano ganadora
			entrada.nextInt () ;
			int auxInt = entrada.nextInt(); 
			mostrarMensaje("Gana el jugador " + auxInt + " con :" + auxString+ "\n" ) ; 
			activoEnRonda = true; 
		}
		else if (codigo.equals("3")) //codigo para recibir monto cuando se gana la partida
		{
			int auxInt = entrada.nextInt(); 
			
			setDineroRestante(auxInt);
		}
		
		else if (codigo.equals("4")) // cÃ³digo para recibir confirmaciÃ³n de que la apuesta enviada al server es vÃ¡lida.
		{
			miTurno = false; 
			mostrarMensaje("Apuesta VÃ¡lida\n");
		}
		else if (codigo.equals("5")) //cÃ³digo para recibir info de que la apuesta enviada al server NO es vÃ¡lida.
		{
			miTurno = true; 
			mostrarMensaje("Apuesta No VÃ¡lida, Intenta de Nuevo  \n");
		}
		else if (codigo.equals("6")) //para recibir el dinero restante y lo apostado hasta el momento en la mano actual,.
		{
			int aux = 0; 
			
			dineroRestante = entrada.nextInt(); 
			System.out.println("dineroR:" +  dineroRestante);
			dineroApostado = entrada.nextInt(); 
			System.out.println("dineroA:" + dineroApostado);
		}
		
		else if(codigo.equals("El otro jugador movio, es tu turno") )
		{
			
			
		
				mostrarMensaje(codigo);
				miTurno = true;
				
				
		}
		else if (codigo.equals("movimiento invalido"))
		{
			
			mostrarMensaje(codigo);
//			miTurno=true;
		}
		else if (codigo.equals("actualizar interfaz"))
		{
			
			mostrarMensaje(codigo);
			
			
		}
		
		
		else if(codigo.equals("El otro jugador se conecto. Ahora es su turno."))
		{
			mostrarMensaje(codigo);
			miTurno = true;
		}
		
		else if(codigo.equals("te toca la ciega pequen¿a"))
		{
			mostrarMensaje(codigo);
		}
		
		else if(codigo.equals("te toca la ciega grande"))
		{
			mostrarMensaje(codigo);
		}
		
		else if(codigo.equals("eres el dealer"))
		{
			mostrarMensaje(codigo);
		}

		else if(codigo.equals("eres el jugador:"))
		{
			mostrarMensaje(codigo+" "+ (Integer.parseInt(miIdentidad)+1));
		}
		
		
		else if (codigo.equals("7")) //oara iniciar la fase de Descarte
		{
			miTurno = true; 
			faseDescarte = true;
			mostrarMensaje ("Elije las cartas que deseas cambiar \n"); 
			
			
		}
		else if (codigo.equals("8")) //para finalizar la fase de descarte
		{
			faseDescarte = false; 
			
			for (int i = 0 ; i < 5 ; i ++)
			{
				int auxP = entrada.nextInt();
				int auxN = entrada.nextInt(); 
				
				cartasMano[i] = new Carta(auxP, auxN ); 
				
			}
			miTurno = false;
		}
		
		else if (codigo.equals("9")) //para finalizar la fase de descarte
		{
			
			mostrarMensaje ("te toca jugar"); 
		}
			
		else 
			mostrarMensaje(codigo);
		
	}
	
	private void mostrarMensaje( final String mensajeAMostrar ){
		SwingUtilities.invokeLater( new Runnable() {
										 public void run() {
											campoDeTexto.append( mensajeAMostrar+"\n" ); // actualiza la salida
										 } // fin del mï¿½todo run
									 } // fin de la clase interna
								  ); // fin de la llamada a SwingUtilities.invokeLater
	} // fin del mï¿½todo mostrarMensaje


	
	
	
	private class Carta extends JLabel 
	{
		private int palo;
		private int numero;
		private boolean descartar =false; 
		
		public Carta (int p, int n)
		{
			palo = p;
			numero = n; 
			
		}
		
		public void setDescartar(boolean b)
		{
			descartar = b; 
		}
		
		public boolean getDescartar()
		{
			return descartar; 
		}
		
		public void setNumero(int x)
		{
			numero = x; 
		}
		
		public void setPalo(int x)
		{
			palo = x;
		}
		
		public int getPalo()
		{
			return palo;
		}
		
		public int getNumero()
		{
			return numero;
		}
		
		
		
	
	} // FIN CLASE INTERNA CARTA
	
	
	
	public int getPosicionEnMesa ()
	{
		return posicionEnMesa;
	}
	
	public int[] cartasDescarteSeleccionadas() 
	{
		int contador = 0; 
		int[] cartasSeleccionadas;
		
		for (int i = 0 ; i < cartasMano.length ; i++ )
		{
			if (cartasMano[i].getDescartar () )
			{
				
			}
		}
		
		cartasSeleccionadas = new int[contador];
		
		for (int i = 0 ; i < cartasMano.length ; i++)
		{
			if (cartasMano[i].getDescartar()  )
			{
				cartasSeleccionadas[i] = i;
			}
		}	
		
		return cartasSeleccionadas ; 
		
	}
	
	
	public class Interfaz extends JFrame implements MouseListener{
		
	
	
	    private JButton[] cartas;
	    private JButton botonApostar;
	    private JButton botonSubirApuesta;
	    private JButton botonBajarApuesta; 
	    private JButton botonIgualar;
	    private JButton botonPasar;
	    private JButton botonRetirarse;
	    private JButton botonDescartar; 
	    private JLabel cartas1;
	    private JLabel cartas2;
	    private JLabel cartas3;
	    private JLabel[] cartasCentro;
	   // private JTextArea campoDeTexto; 
	    private JPanel panelTexto;
	    
		private ImageIcon imagen1 = new ImageIcon( "Imagenes/fondo.png");		//imagen fondo
		private ImageIcon imagenEscala1 = new ImageIcon(imagen1.getImage().getScaledInstance(1200, 800, java.awt.Image.SCALE_DEFAULT));
		private ImageIcon imagen2 = new ImageIcon("Imagenes/boton.png");		//imagen fondo
		private ImageIcon imagenEscala2 = new ImageIcon(imagen2.getImage().getScaledInstance(150, 90, java.awt.Image.SCALE_DEFAULT));
		private ImageIcon imagen3 = new ImageIcon("Imagenes/reves.jpg");		//imagen fondo
		private ImageIcon imagenEscala3 = new ImageIcon(imagen3.getImage().getScaledInstance(120, 180, java.awt.Image.SCALE_DEFAULT));
		private ImageIcon imagenEscala33 = new ImageIcon(imagen3.getImage().getScaledInstance(80, 110, java.awt.Image.SCALE_DEFAULT));
		//	private ImageIcon imagenEscalaX = new ImageIcon(imagen.getImage().getScaledInstance(120, 180, java.awt.Image.SCALE_DEFAULT));
	
	
	    public Interfaz(){
	    	
	    	
	    	
	        
	        panelTexto = new JPanel(); 
	        panelTexto.setVisible(true);
	        //panelTexto.setBounds(100,10,270, 170);
	        panelTexto.setBackground(Color.WHITE);
	        panelTexto.setForeground(Color.ORANGE);
	      
	        
	        
	        
	        campoDeTexto = new JTextArea(20,40); 
			campoDeTexto.setEditable(false);
		
//			campoDeTexto.setBounds(0, 0, 420, 170);
			campoDeTexto.setBackground(Color.LIGHT_GRAY);// new java.awt.Color(220,220,220
			campoDeTexto.setVisible(true);
			campoDeTexto.setEditable(false);
			campoDeTexto.append("HOLA");
			//this.add(campoDeTexto);
		//	panelTexto.setLayout(null);
			panelTexto.add(campoDeTexto);
		JScrollPane scroll = new JScrollPane(panelTexto);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(100,10,270, 170);
			
			this.add(scroll);
//			panelTexto.add(scroll);
	    	
			//Cartas Centro
			if(mesaDeJuego == 1)
			{
		    	cartasCentro = new JLabel[5];	
		        for(int i =0;i<= 4;i++)
		        { 
		        	cartasCentro[i] = new JLabel();
		        	cartasCentro[i].setBounds(400+(i*90), 480, 80, 110); // ajusta el tamano y la posicion respecto a la ventana.
		            cartasCentro[i].setIcon(imagenEscala33);
		            this.add(cartasCentro[i]);
	    
		        }
		        
		        cartas = new JButton[2];	
		        for(int i =0;i<= 1;i++)
		        { 
		        	
		        	cartas[i] = new JButton();
		        	cartas[i].setBounds(400+(i*140), 100, 120, 180); // ajusta el tamano y la posicion respecto a la ventana.
		            cartas[i].setIcon(imagenEscala3);
		            cartas[i].addMouseListener(this);
		            this.add(cartas[i]);
				}
			}
			//Cartas de el jugador
			else if (mesaDeJuego == 2)
	    	{
		    	cartas = new JButton[5];	
		        for(int i =0;i<= 4;i++)
		        { 
		        	
		        	cartas[i] = new JButton();
		        	cartas[i].setBounds(400+(i*140), 100, 120, 180); // ajusta el tamano y la posicion respecto a la ventana.
		            cartas[i].setIcon(imagenEscala3);
		            cartas[i].addMouseListener(this);
		            this.add(cartas[i]);
		            
		            botonDescartar = new JButton("DESCARTAR");
					botonDescartar.setBounds(580, 300, 120, 40);
					botonDescartar.setBackground(new java.awt.Color(80,110, 225));
					botonDescartar.setForeground(Color.WHITE);
					this.add(botonDescartar); 
					botonDescartar.addMouseListener(this);
					
		        }
	    	}
		        
	        
	        //BOTONES DE ACCION
	        
	        //Apostar
	        botonApostar = new JButton(" APOSTAR ");
			botonApostar.setBounds(170,200,130,40);	
			botonApostar.setBackground(new java.awt.Color(80,99,225));;
			botonApostar.setForeground(Color.WHITE);
			this.add(botonApostar);
			botonApostar.addMouseListener(this);
			
			//Subir Apuesta
			botonSubirApuesta = new JButton ("+");
			
			botonSubirApuesta.setBounds(320,200, 50,40);
			botonSubirApuesta.setBackground(new java.awt.Color(80,99,225));
			botonSubirApuesta.setForeground(Color.WHITE);
			this.add(botonSubirApuesta);
			botonSubirApuesta.addMouseListener(this);
			
			//BajarApuesta
			botonBajarApuesta = new JButton ("-");
			botonBajarApuesta.setBounds(100, 200, 50,40);
			botonBajarApuesta.setBackground(new java.awt.Color(80,99,225));
			botonBajarApuesta.setForeground(Color.WHITE);
			this.add(botonBajarApuesta);
			botonBajarApuesta.addMouseListener(this);
			
			
			//Igualar
					
	        botonIgualar = new JButton(" IGUALAR ");
	        botonIgualar.setBounds(170,250,130,40);
	        botonIgualar.setBackground(new java.awt.Color(44,64,193));
	        botonIgualar.setForeground(Color.WHITE);
			this.add(botonIgualar);
			botonIgualar.addMouseListener(this);
			
			//Pasar
	        botonPasar = new JButton("  PASAR  ");
	        botonPasar.setBounds(170,300,130,40);
	        botonPasar.setBackground(new java.awt.Color(33,52,175));
	        botonPasar.setForeground(Color.WHITE);
			this.add(botonPasar);
			botonPasar.addMouseListener(this);
			
			//Retirarse
	        botonRetirarse = new JButton("RETIRARSE");
	        botonRetirarse.setBounds(170,350,130,40);
	        botonRetirarse.setBackground(new java.awt.Color(10,26,134));;
	        botonRetirarse.setForeground(Color.WHITE);
			this.add(botonRetirarse);
			botonRetirarse.addMouseListener(this);
			
			//Descartar
			
			
	    
	
			
	    	//FIN
	    
	        //Fondo 
			((JPanel)getContentPane()).setOpaque(false);
			JLabel fondo= new JLabel(); 
			fondo.setIcon(imagenEscala1); 
			getLayeredPane().add(fondo,JLayeredPane.FRAME_CONTENT_LAYER);
			fondo.setBounds(0,0,imagenEscala1.getIconWidth(),imagenEscala1.getIconHeight());
			
	        // configuramos la ventanda
	        this.setLayout(null);
	        this.setVisible(true);
	        this.setSize(1200, 800);
	        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        
	    }
	    
	   
	    
	    
	   public void cambiarIconoCartaFlop()
	   {
		   
		   leerFlop(); 
		   for (int i = 0 ; i < 2 ; i ++)
		   {		
			   String palo = String.valueOf(cartasFlop[i].getPalo() );
			   String numero = String.valueOf(cartasFlop[i].getNumero() );
			   
			   ImageIcon aux = new ImageIcon ("imgPoker/" + palo + "/" + numero+ ".jpg");
			   ImageIcon auxEscala = new ImageIcon (aux.getImage().getScaledInstance(120, 180, java.awt.Image.SCALE_DEFAULT));
			   cartasCentro[i].setIcon(auxEscala);
		   }
		   
		   
	   }
	
	
	
	    @Override
	    public void mouseClicked(MouseEvent e) {
	       
	    	if(miTurno)
	    	{
	    	if( e.getSource() == botonSubirApuesta )
			{
	    		mostrarMensaje ("CLICK \n"); 
				setValorApuesta(10);
			}
			else if (e.getSource() == botonBajarApuesta)
			{
				setValorApuesta(-10);
				
			}

			else if(e.getSource() == botonApostar)
			{
				
//				salida.format("%d\n", valorApuesta); //lee del socket el valor de apuesta mÃ­nima 
//				salida.flush();
				
				mostrarMensaje("Has Apostado " + valorApuesta +"\n");
				mostrarMensaje("Turno Finalizado\n");
				setDineroRestante(valorApuesta);  // resta el valor apostado al dinero restante
				
				salida.format("%d\n",3); //CODIGO PARA RETIRARSEr
				salida.flush() ; 
				salida.format("%d\n", valorApuesta); //lee del socket el valor de apuesta mÃ­nima 
				salida.flush();
				
				System.out.println("Apuesta: "  + miIdentidad);
				miTurno = false; 
			}
	    	
			else if (e.getSource() == botonIgualar)
			{
				mostrarMensaje("Igualas la apuesta \n"); 
				salida.format("%d\n", 4);
				salida.flush(); 
				salida.format("%d\n", dineroApostado);
				salida.flush(); 
				miTurno = false; 
			}
	    	
			else if(e.getSource() == botonRetirarse)
			{
//				salida.format("%d\n", 3); //CODIGO PARA RETIRARSEr
//				//salida.format("retirar); 
//				salida.flush() ; 
				
				activoEnRonda = false;
				mostrarMensaje("Te retiras de la mano en curso.");
				
				System.out.println("Jugador "  + posicionEnMesa +"se retira" );
				
			}
			else if(e.getSource () == botonPasar)
			{
//				salida.format("%d\n", 2); // CODIGO PARA INDICAR QUE EL JUGADOR PASA 
//				//salida.format("pasar") ; 
//				salida.flush();
				mostrarMensaje ("Has pasado tu turno \n");
				miTurno = false; 
				System.out.println("Jugador "  + posicionEnMesa +" pasa");
			}
			else if(e.getSource() ==botonDescartar)
			{
				enviarCartasDescarte (cartasDescarteSeleccionadas());
				this.remove(botonDescartar);
			}
				
	    	if (faseDescarte)
	    	{
	    		
	    		
	    		for (int i  = 0 ; i < cartas.length ; i ++)
	    		{
	    			if (e.getSource() == cartas[i] )//&& cartasMano[i].getDescartar() == false)
	    			{
	    			
	    				//cartasMano[i].setDescartar(true);
	    				
	    				cartas[i].setBounds(400+(i*140), 80, 120, 180);
	    				cartas[i].setForeground(new java.awt.Color(255,230,0));
//	    				System.out.println("entra al if");
	    				
	    			}
	    			else if(e.getSource() == cartas[i] ) // && cartasMano[i].getDescartar() )
	    			{
	    				//cartasMano[i].setDescartar(false);
	    				cartas[i].setBounds(400+ (i*140), 100, 120, 180);
	    				
	    			}
	    		}
	    			
	    			
	    			
	    	}
	    	}
	        
	    }
	
	    @Override
	    public void mouseEntered(MouseEvent e) {
	        
	    	if(e.getSource() == botonApostar )
	    	{
	    		botonApostar.setText(String.valueOf(valorApuesta));
	    	}
	    	else if (e.getSource() == botonIgualar)
	    	{
	    		botonIgualar.setText(String.valueOf(valorApuesta));
	    	}
	    }
	
	    @Override
	    public void mouseExited(MouseEvent e) {
	    
	    	if(e.getSource() == botonApostar )
	    	{
	    		botonApostar.setText(" APOSTAR ");
	    		
	    	}
	    	else if(e.getSource() == botonIgualar)
	    	{
	    		botonIgualar.setText(" IGUALAR ");
	    	}
	        
	    }
	
	    @Override
	    public void mousePressed(MouseEvent arg0) {
	        // TODO Auto-generated method stub
	        
	    }
	
	    @Override
	    public void mouseReleased(MouseEvent arg0) {
	        // TODO Auto-generated method stub
	        
	    }
	    
	
	  
            
        
    }//FUN CLASE INTERNA INTERFAZ
	




}
