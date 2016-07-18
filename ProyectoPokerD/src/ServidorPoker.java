//@nombre: Jhon Alejandro Orobio 
//@cÃ³digo: 1533627
//@fecha: 17/junio/2016


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.SwingUtilities;



public class ServidorPoker {
	
	private Jugador[] jugadores;
	
	private Jugador[] jugadoresTexas;
	private Jugador[] jugadoresCubierto;
	private ControlJuego[] controles;
	private ServerSocket servidor; // socket servidor
	private int contador1;
	private int contador2;// contador del nÃ¯Â¿Â½mero de conexiones
	private int contador;
	private ExecutorService ejecutarJuego;

	private ControlPoker controlJuego;
	private Carta[] mazo1;
	private Carta[] mazo2;
	private Carta[] comunes;
	private Lock bloqueoJuego; // para bloquear el juego y estar sincronizado
	private Condition otroJugadorConectadoTexas;
	private Condition otroJugadorConectadoCubierto;// para esperar al otro jugador
	private Condition turnoOtroJugador; // para esperar el turno del otro jugador
	private int jugadorActualTexas;
	private int jugadorActualCubierto;
	private int dealerTexas;
	private int dealerCubierto;
	private String auxTexas;
	private String auxCubierto;
	private int apuestaTexas;
	private int apuestaCubierto;
	private int apuestaTotalTexas;
	private int apuestaTotalCubierto;
	private int cuentaTurno;
	private int boteTexas;
	private int boteCubierto;
	private int numeroRondaTexas;
	private int numeroRondaCubierto;
	public ServidorPoker() 
	{
		numeroRondaTexas = 0;
		numeroRondaCubierto = 0;
		boteTexas = 0;
		boteCubierto = 0;
		dealerTexas = (int)(Math.random()*3);
		dealerCubierto = (int)(Math.random()*3);
		apuestaTexas = 0;
		apuestaCubierto = 0;
		apuestaTotalTexas = 0;
		apuestaTotalCubierto = 0;
		auxTexas = "";
		auxCubierto = "";
		contador = 0;
		contador1 = 0;
		contador2 = 0;
		cuentaTurno = 0;
		mazo1 = new Carta[52];
		mazo2 = new Carta[52];
		comunes = new Carta[5];
		
		for(int i  = 0; i<52;i++)
		{
			mazo1[i] = new Carta();
			mazo2[i] = new Carta();
		}
		
		for(int i  = 0; i<5;i++)
		{
			comunes[i] = new Carta();
			
		}
		
		controlJuego = new ControlPoker();
		jugadorActualTexas = 0;
		jugadorActualCubierto = 0;
		bloqueoJuego = new ReentrantLock(); 
		otroJugadorConectadoTexas = bloqueoJuego.newCondition(); 
		otroJugadorConectadoCubierto = bloqueoJuego.newCondition(); 
		turnoOtroJugador = bloqueoJuego.newCondition();
		jugadores = new Jugador[8];
		controles = new ControlJuego[2];
		jugadoresTexas = new Jugador[4];
		jugadoresCubierto = new Jugador[4];
		controlJuego.llenarMazo();
		mazo1 = controlJuego.getMazo1();
		mazo2 = controlJuego.getMazo2();
		controlJuego.repartir(5,comunes,mazo1);
		
		
	}
		
	
	
	public void execute()
	{
		
		
		
		ejecutarJuego = Executors.newFixedThreadPool( 8 );
		
		try {
			servidor = new ServerSocket(12345,8);
			System.out.println("conexion");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//pintar();
		
		for ( int i = 0; i < jugadores.length; i++ ) {
			try {
				
				if(contador ==4)
				{
					contador = 0;
				}
				jugadores[i] = new Jugador( servidor.accept() );
				jugadores[i].setNumeroJugador(contador++);
				ejecutarJuego.execute(jugadores[i]);//establece la conexiÃ¯Â¿Â½n via Socket
//				System.out.println(jugadores[i].getNumeroJugador());
			
		} // fin de try
			catch ( IOException excepcionES ) 
			{
				excepcionES.printStackTrace();
				System.exit( 1 );
				
			} // fin de catch
		} // fin de for
//		 // fin de try
		
		
	}
	
	
	
	public boolean turnos(int jugador, Jugador jugadores[], int jugadorActual,String aux,int apuesta) 
	{
		boolean resultado = false;
		// mientras no sea el jugador actual, debe esperar su turno
		while ( jugador != jugadorActual )
		{
			
			
			bloqueoJuego.lock(); // bloquea el juego para esperar a que el otro jugador	haga su movmiento
			try{
				turnoOtroJugador.await(); // espera el turno de jugador
			} 
			catch ( InterruptedException excepcion ){
				excepcion.printStackTrace();
			} 
			finally
			{
				bloqueoJuego.unlock(); // desbloquea el juego despuÃ¯Â¿Â½s de esperar
			} 
		
		}
		
		
		if((jugadores[jugadorActual].consultarSaldo(apuesta)))
				{
		
					if( (jugadores[jugadorActual].getJugada() == 3))
					{
						jugadores[jugadorActual].setJugada(0);
						
						for (int i = 0;i < 3;i++)
			        	{
					 		cuentaTurno = ( cuentaTurno + 1 ) % 4;
			        		jugadores[cuentaTurno].actualizarInterfaces(aux);
			        		
			        		System.out.println("cambio"+" "+cuentaTurno);
			        	}
						System.out.println("jugador actualholi: "+jugadorActualTexas); 
					 	
						jugadorActual = ( jugadorActual + 1 ) % 4;
						
						if(jugadores[jugadorActual].getTipoJuego() == 1)
						{
							
					 	 jugadorActualTexas = ( jugadorActualTexas + 1 ) % 4;
					 	 String aux2 = Integer.toString(apuesta); 
					 	 jugadores[jugadorActualTexas].otroJugadorMovio(jugadores, jugadorActualTexas,aux2);
						}  
						
						else if(jugadores[jugadorActual].getTipoJuego() == 2)
						{
							
							
					 	 jugadorActualCubierto = ( jugadorActualCubierto + 1 ) % 4;
					 	 String aux2 = Integer.toString(apuesta); 
					 	 jugadores[jugadorActualCubierto].otroJugadorMovio(jugadores, jugadorActualCubierto,aux2);
						}  
						
					 	System.out.println("jugador actuachao: "+jugadorActualTexas); 
		//	           
			            bloqueoJuego.lock(); // bloquea el juego para indicar al otro jugador que realice su movimiento
			            
			            try {
			                turnoOtroJugador.signal(); // indica al otro jugador que debe continuar
			            } 
			            finally {
			                bloqueoJuego.unlock(); // desbloquea el juego despues de avisar
			            } 
			        
						resultado = true;
					}
			
				}
			 else 
			    {
			        resultado = false; // notifica al jugador que el movimiento fue invï¿½lido
			  // notifica al jugador que el movimiento fue invÃ¯Â¿Â½lido
			    }
			
			return resultado;
		
		
    
	
		}
	
	
	public int actualizarRonda(int ronda)
	{
		int contador = 0;
		
		
		for(int i = 1; i<4;i++)
		{
			if(jugadores[0].getApuestaAcumulada()==jugadores[i].getApuestaAcumulada())
			{
				contador++;
			}
		}
		
		if(contador ==3)
		{
			ronda++;
		}
		
		return ronda;
	}
   
	
	
	
	public boolean terminarJuego()
	{
//		if (jugadores[0].getMarcador() == 5 || jugadores[1].getMarcador() == 5)
//		{
//			return true;
//		}
//		else 
//			return false;
		return false;
	}
	

	
	
	public class Jugador implements Runnable{

		private Socket conexion; // conexiÃ¯Â¿Â½n con el cliente
		private Scanner entrada; // entrada del cliente
		private Formatter salida; // salida al cliente
		private int numeroJugador; // identifica al Jugador
		private boolean suspendido = true; // indica si el subproceso estÃ¯Â¿Â½ suspendido
		private int tipoJuego;
		private Carta[] cartas;
		private int puntajeJugada;
		private int jugada;
		private int contador;
		private int auxInt;
		private int dinero;
		private int apuestaAcumulada;
		private String posiciones;
		
	

		public Jugador (Socket socket)
		{
			posiciones = "";
			apuestaAcumulada = 0;
			contador = 0;
			tipoJuego = 0;
			jugada = 0;
			conexion = socket;
			auxInt = 0;
			dinero = 300;
			 puntajeJugada =0;
			numeroJugador = 0;
			cartas = new Carta[7];
			for(int i = 0; i< 7; i++)
			{
				cartas[i] = new Carta();
			}
			
			try {
				entrada = new Scanner(conexion.getInputStream());
				salida = new Formatter(conexion.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void acomodarJugador()
		{
			if(tipoJuego == 1)
			{
				jugadoresTexas[contador1] = this;
				contador1++;
				
			}
			
			else if(tipoJuego == 2)
			{
				jugadoresCubierto[contador2] = this;
				contador2++;
			}
		}
		
		public void cartasAIntercambiar(int pos)
		{
			if(tipoJuego ==1)
			{
				controlJuego.repartir(1, cartas, mazo1, pos);
			}
			else if(tipoJuego ==2)
			{
				controlJuego.repartir(1, cartas, mazo2, pos);
			}
		}
		
		
		public void actualizarInterfaces(String aux)
		{
			
			salida.format( "actualizar interfaz\n" );
			salida.flush();
			salida.format("%s\n",aux);
			salida.flush();
		}
		
		
		
		public boolean consultarSaldo(int sumaAapostar)
		{
			if(dinero< sumaAapostar)
			{
				return false;
			}
			else 
				return true;
		}
		
		public void escogerDealer()
		{
			int aleatorio= 0;
			
			aleatorio = (int)(Math.random()*3);
			
			if(tipoJuego == 1)
			{
				dealerTexas = aleatorio;
				System.out.println("dealer: "+dealerTexas);
			}
			
			else if(tipoJuego == 2)
			{
				dealerCubierto = aleatorio;
			}
		
		}
		
		
		
		
		public void otroJugadorMovio(Jugador jugadores[],int jugadorActual,String aux)
		{
			if(terminarJuego() == true 
					&& jugadores[jugadorActual].getPuntajeJugada()!=13)
			{
				salida.format( "El otro jugador ha ganado\n" );
				salida.flush();
				salida.format("%s\n",aux);
				salida.flush();
			}
			else
			{
//				System.out.println(this.getJugada());
				System.out.println("jugador actual: "+jugadorActual); 
				salida.format("El otro jugador movio, es tu turno\n");
				salida.flush();
				int auxInt = Integer.parseInt(aux);
				salida.format("%d\n",auxInt);
				salida.flush();
				
			}
	
		}
		
		public void establecerSuspendido(boolean b) {
			// TODO Auto-generated method stub
			suspendido = b;
		}

		@Override
		public void run() 
		{
			

			try{
			
				
				if(contador1 >= 3)
				{
					
					jugadorActualTexas = dealerTexas;
					controles [0]=new ControlJuego(jugadorActualTexas); 
					controles[0].setJugadorLocal(jugadoresTexas );
					controles[0].setOtroJugadorConectado(otroJugadorConectadoTexas);
					
					ejecutarJuego.execute( controles[0]);
					
					 // continÃ¯Â¿Â½a el jugador X
					// despierta el subproceso del jugador X// ejecuta el objeto Runnable jugador
				}
			
				if(contador2 >= 3)
				{
					
					jugadorActualTexas = dealerCubierto;
					controles [1]=new ControlJuego(jugadorActualCubierto); 
					controles[1].setJugadorLocal(jugadoresCubierto );
					controles[1].setOtroJugadorConectado(otroJugadorConectadoCubierto);
					
					ejecutarJuego.execute( controles[1]);
					
					// despierta el subproceso del jugador X
				}
				
				tipoJuego = entrada.nextInt();
				
				acomodarJugador();
				
				salida.format("%s\n",Integer.toString(numeroJugador));
				System.out.println("numero jugador: "+numeroJugador);
				salida.flush();
				
				if(tipoJuego ==1)
				{
					
					controlJuego.repartir(2,cartas,mazo1);
					for(int i = 2; i<7;i++)
					{
						cartas[i] = comunes[i-2];
						//System.out.print( cartas[i].getNumero()+ "  "+cartas[i].getPalo());
					}
					
				}
				
				if(tipoJuego ==2)
				{
					
					controlJuego.repartir(5,cartas,mazo2);
					
				}
				
				
				
				if(tipoJuego ==1)
				{
					
					for(int i = 0; i<7;i++)
					{
						if((Integer.toString(cartas[i].getNumero())).length()==2)
						{
							auxTexas+= Integer.toString(cartas[i].getNumero())+Integer.toString(cartas[i].getPalo());
						}
						else
						{
							auxTexas+= Integer.toString(0)+Integer.toString(cartas[i].getNumero())+Integer.toString(cartas[i].getPalo());
						}
						
//						System.out.println(auxTexas);
					}
//					System.out.println(auxTexas);
					salida.format("%s\n",auxTexas);
					auxTexas= "";
					salida.flush();
				}
				
				if(tipoJuego ==2)
				{
				
					for(int i = 0; i<5;i++)
					{
						if((Integer.toString(cartas[i].getNumero())).length()==2)
						{
							auxCubierto+= Integer.toString(cartas[i].getNumero())+Integer.toString(cartas[i].getPalo());
						}
						else
							auxCubierto+= Integer.toString(0)+Integer.toString(cartas[i].getNumero())+Integer.toString(cartas[i].getPalo());
							
					}
					
					
					salida.format("%s\n",auxCubierto);
					auxCubierto= "";
					salida.flush();
				}
				
			
					
					if(numeroJugador == dealerTexas && tipoJuego == 1)
					{
					
						try{
							salida.format("9\n");//eres el jugador 1
							salida.flush();
							bloqueoJuego.lock(); // bloquea el juego para esperar al segundo jugador
							
							while( suspendido ){
								
								otroJugadorConectadoTexas.await(); // espera al jugador O
							} // fin de while
						} // fin de try
						catch ( InterruptedException excepcion ){
							excepcion.printStackTrace();
						} // fin de catch
						finally {
							bloqueoJuego.unlock(); // desbloquea el juego 
						} // fin de finally
					
					
					salida.format( "El otro jugador se conecto. Ahora es su turno.\n" );
					salida.flush(); // vacÃ¯Â¿Â½a la salida
					}
					
					else if(numeroJugador == dealerCubierto && tipoJuego == 2)
					{
					
					try{
							salida.format("9\n");
							salida.flush();
							bloqueoJuego.lock(); // bloquea el juego para esperar al segundo jugador
							
							while( suspendido ){
								
								otroJugadorConectadoCubierto.await(); // espera al jugador O
							} // fin de while
						} // fin de try
						catch ( InterruptedException excepcion ){
							excepcion.printStackTrace();
						} // fin de catch
						finally {
							bloqueoJuego.unlock(); // desbloquea el juego 
						} // fin de finally
					
					salida.format( "El otro jugador se conecto. Ahora es su turno.\n" );
					salida.flush(); // vacÃ¯Â¿Â½a la salida
				
					}
					
					else if(numeroJugador == ((dealerTexas+1) % 4) && tipoJuego == 1)
					{
//						salida.format("eres el jugador:\n" );
//						//envÃ­a el numero de jugador para identificarlo 
//						salida.flush(); // vacÃ¯Â¿Â½a la salida
						salida.format("eres el dealer\n");
						salida.flush();
					}
					
					else if(numeroJugador == ((dealerTexas+2) % 4) && tipoJuego == 1)
					{
						dinero = dinero-20;
	
						salida.format("te toca la ciega pequena\n");
						salida.flush();
						
					}
					
					else if(numeroJugador == ((dealerTexas+3) % 4) && tipoJuego == 1)
					{
						dinero = dinero-40;
						
						salida.format("te toca la ciega grande\n");
						salida.flush();
						
						
					}
				
					else
					{
						salida.format("eres el jugador:\n" );
						//envÃ­a el numero de jugador para identificarlo 
						salida.flush(); // vacÃ¯Â¿Â½a la salida
					}
					
					// fin de if
					 
				
			// fin de else
			while(!terminarJuego())
			{
				try
				{
					
						if(entrada.hasNext())
							{
							
							auxInt = entrada.nextInt();
						
							if(auxInt == 3)
							{
							
								if(tipoJuego == 1)
									{
										
										apuestaTexas = entrada.nextInt();
										jugadores[jugadorActualTexas].setJugada(auxInt);
										dinero = dinero-apuestaTexas;
										apuestaAcumulada += apuestaTexas;
										boteTexas += apuestaAcumulada;
										salida.format("numero de ronda\n");
										salida.flush();
										salida.format("%d\n",actualizarRonda(numeroRondaTexas));
										salida.flush();
	//									System.out.println("soy la jugada: "+jugadores[jugadorActualTexas].getJugada()+" "+jugadorActualTexas );
									}
									
									if(tipoJuego == 2)
									{
										apuestaCubierto = entrada.nextInt();
										jugadores[jugadorActualCubierto].setJugada(auxInt);
										dinero = dinero-apuestaCubierto;
										apuestaAcumulada += apuestaCubierto;
										boteCubierto += apuestaAcumulada;
										salida.format("numero de ronda\n");
										salida.flush();
										salida.format("%d\n",actualizarRonda(numeroRondaCubierto));
										salida.flush();
										
									
									}
								}
							else  if(auxInt == 4)
							{
								if(tipoJuego == 1)
								{
									
									apuestaTotalTexas = entrada.nextInt();
									jugadores[jugadorActualTexas].setJugada(3);
									dinero = dinero -(apuestaTexas-apuestaAcumulada);
//									System.out.println("soy la jugada: "+jugadores[jugadorActualTexas].getJugada()+" "+jugadorActualTexas );
								}
								
								if(tipoJuego == 2)
								{
									apuestaTotalCubierto = entrada.nextInt();
									jugadores[jugadorActualCubierto].setJugada(3);
									dinero = dinero -(apuestaCubierto-apuestaAcumulada);
								
								}
							}
							
							else  if(auxInt == 5)
							{
								posiciones = entrada.nextLine();
								int  aux = 0;
								for(int i = 0; i <posiciones.length();i++)
								{
									aux = Integer.parseInt(String.valueOf(posiciones.charAt(i)));
									cartasAIntercambiar(aux);
								}
								
								for(int i = 0; i<5;i++)
								{
									if((Integer.toString(cartas[i].getNumero())).length()==2)
									{
										auxCubierto+= Integer.toString(cartas[i].getNumero())+Integer.toString(cartas[i].getPalo());
									}
									else
										auxCubierto+= Integer.toString(0)+Integer.toString(cartas[i].getNumero())+Integer.toString(cartas[i].getPalo());
										
								}
								
								salida.format("%s\n","nuevas cartas");
								
								salida.flush();
								salida.format("%s\n",auxCubierto);
								auxCubierto= "";
								salida.flush();
//								if(tipoJuego == 1)
//								{
//									
//									apuestaTotalTexas = entrada.nextInt();
//									jugadores[jugadorActualTexas].setJugada(3);
//									dinero = dinero -(apuestaTexas-apuestaAcumulada);
////									System.out.println("soy la jugada: "+jugadores[jugadorActualTexas].getJugada()+" "+jugadorActualTexas );
//								}
								
								
								
								
							}
							
							}
					}catch(IllegalStateException e){
						
						
					}
			
				
					
					if(tipoJuego == 1)
					{
						auxTexas = Integer.toString(boteTexas);
						
						if(turnos(numeroJugador,jugadoresTexas,jugadorActualTexas,auxTexas,apuestaTexas))
						{
							contador++;
//							System.out.println("juga: "+jugadorActualTexas);
							
							salida.format("Jugador ha movido\n");
							salida.flush();
//							apuestaTexas = 0;
							
							
							
						}
					}
					else if(tipoJuego == 2)
					{
						
						auxCubierto = Integer.toString(boteCubierto);
						
						if(turnos(numeroJugador,jugadoresCubierto,jugadorActualCubierto,auxCubierto,apuestaCubierto))
						{
							salida.format("Jugador ha movido\n");
							salida.flush();
//							apuesta = 0;
							
						}
					} 
					else
					{
						salida.format("movimiento invalido\n");
						salida.flush();
					}
					
				}
			}
			finally {
				try {
					
					if (terminarJuego() == true 
							&& jugadores[jugadorActualTexas].getPuntajeJugada() ==5)
					{
						
						salida.format( "Has ganado!!" );
				 	 	jugadorActualTexas = ( jugadorActualTexas + 1 ) % 4;
				 	 	System.out.println(auxTexas);
			            jugadores[ jugadorActualTexas ].otroJugadorMovio(jugadoresTexas,jugadorActualTexas,auxTexas);
						salida.flush();
					}
					conexion.close(); // cierra la conexiÃ¯Â¿Â½n con el cliente
				} // fin de try
				catch ( IOException excepcionES ){
					excepcionES.printStackTrace();
					System.exit( 1 );
				} // fin de catch
				} 
			// TODO Auto-generated method stub
			//System.out.println("hablalo");
			
		}	
		
		 public void setPuntajeMano()
		{
			//metodo de ordenar manos
			ordenar(cartas);
			if (controlJuego.hayEscaleraReal (cartas) ) 
				puntajeJugada = 10000 ; 
			
			else if (controlJuego.hayEscaleraColor(cartas) )
				puntajeJugada = 9000 ;
			
			else if (controlJuego.hayPoker(cartas))
				puntajeJugada = 8000 ;
			
			else if (controlJuego.hayFull (cartas))
				puntajeJugada = 7000;
			else if (controlJuego.hayColor(cartas))
				puntajeJugada = 6000;
			else if(controlJuego.hayEscalera (cartas))
				puntajeJugada = 5000; 
			else if (controlJuego.hayTrio (cartas))
			puntajeJugada = 4000; 
			else if (controlJuego.hayDoblePareja (cartas))
				puntajeJugada = 3000; 
			else if (controlJuego.hayPar(cartas))
				puntajeJugada = 2000; 
			
	 
			puntajeJugada += cartas[cartas.length].getNumero() ; 
				
		}
 
 		public int getNumeroJugador() {
 			return numeroJugador;
 		}
		public int getApuestaAcumulada() {
			return apuestaAcumulada;
		}

		public void setApuestaAcumulada(int apuestaAcumulada) {
			this.apuestaAcumulada = apuestaAcumulada;
		}
	
		
		public void setNumeroJugador(int numeroJugador) {
			this.numeroJugador = numeroJugador;
		}

		public boolean isSuspendido() {
			return suspendido;
		}

		public void setSuspendido(boolean suspendido) {
			this.suspendido = suspendido;
		}
		
		public int getPuntajeJugada() {
			return puntajeJugada;
		}
	
		public void setPuntajeJugada(int puntajeJugadar) {
			this.puntajeJugada = puntajeJugada;
		}
		
		public int getTipoJuego() {
			return tipoJuego;
		}

		public void setTipoJuego(int tipoJuego) {
			this.tipoJuego = tipoJuego;
		}
		
		public int getJugada() {
			return jugada;
		}

		public void setJugada(int jugada) {
			this.jugada = jugada;
		}


	}
		

	
	public class ControlJuego implements Runnable
	{
		
		private Jugador[] jugadorLocal;
		private Condition otroJugadorConectado;
		private int dealer;
		public Condition getOtroJugadorConectado() {
			return otroJugadorConectado;
		}

		public void setOtroJugadorConectado(Condition otroJugadorConectado) {
			this.otroJugadorConectado = otroJugadorConectado;
		}

		public ControlJuego(int dealer)
		{
			this.dealer = dealer;
			jugadorLocal = new Jugador[4];
			otroJugadorConectado = bloqueoJuego.newCondition();
		}

		@Override
		public void run() 
		{
	
			
			bloqueoJuego.lock(); // bloquea el juego para avisar al subproceso del jugador X que inicie
			
			try {
				
					jugadorLocal[dealer].establecerSuspendido( false ); // continÃ¯Â¿Â½a el jugador X
					otroJugadorConectado.signal(); // despierta el subproceso del jugador X
				
				
			} // fin de try
			finally{
				bloqueoJuego.unlock(); // desbloquea el juego despuÃ¯Â¿Â½s de avisar al jugador X
			}
			
		
			
		
		}
		
		
		
	
		public Condition getOtroJugadorConectado1() {
			return otroJugadorConectado;
		}

		public void setOtroJugadorConectado1(Condition otroJugadorConectado) {
			this.otroJugadorConectado = otroJugadorConectado;
		}
		
		public Jugador[] getJugadorLocal() {
			return jugadorLocal;
		}
		
		public void setJugadorLocal(Jugador[] jugadorLocal) {
			this.jugadorLocal = jugadorLocal;
		}

	}
	

}
