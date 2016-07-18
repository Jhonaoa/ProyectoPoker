
public class ControlPoker {
	
	private Carta[] mazo1;
	private Carta[] mazo2;
	private int bote;
	
	public  ControlPoker()
	{
		mazo1 = new Carta[52];
		
		for(int i =0 ; i<52 ; i++)
		{
			mazo1[i] = new Carta();
		}
		
		mazo2 = new Carta[52];
		
		for(int i =0 ; i<52 ; i++)
		{
			mazo2[i] = new Carta();
		}
		
	}
	
	public void llenarMazo()
	{
		int posicion = 0;
		for(int i= 1; i<=13 ; i++)
		{ 	
						
			for(int j=0; j<=3; j++)
			{
				mazo1[posicion].setNumero(i);				
				mazo1[posicion].setPalo(1+j);
				mazo2[posicion].setNumero(i);				
				mazo2[posicion].setPalo(1+j);
				posicion++;
			}
		}
	}
	
	
	
	
	public void repartir(int num,Carta[] comunes,Carta[]mazo)
	{
		int aleatorio1= 0;
		int contador = 0;
		
		while(contador != num)
		{
			aleatorio1 = (int)(Math.random()*52);
			
			while(mazo[aleatorio1].getNumero() != 0)
			{
				comunes[contador].setNumero(mazo[aleatorio1].getNumero());
				comunes[contador].setPalo(mazo[aleatorio1].getPalo());
				mazo[aleatorio1].setNumero(0);
				contador++;
				
			}
		}
	}
	
	public void repartir(int num,Carta[] comunes,Carta[]mazo,int pos)
	{
		int aleatorio1= 0;
		int contador = 0;
		
		while(contador != num)
		{
			aleatorio1 = (int)(Math.random()*52);
			
			while(mazo[aleatorio1].getNumero() != 0)
			{
				comunes[pos].setNumero(mazo[aleatorio1].getNumero());
				comunes[pos].setPalo(mazo[aleatorio1].getPalo());
				mazo[aleatorio1].setNumero(0);
				contador++;
				
			}
		}
	}
	
	public Carta[] getMazo1() {
		return mazo1;
	}

	public Carta[] getMazo2() {
		return mazo2;
	}


	public int getBote() {
		return bote;
	}

	public void setBote(int bote) {
		this.bote = bote;
	}

	
	
	public boolean hayEscalera(Carta[] mano) 
	{
		int contador = 1; 
		int cartaAlta = 0; 
		
		for (int i = 0 ; i < mano.length-1 ; i ++)
		{
			if(mano[i].getNumero()+1  == mano[i+1].getNumero()  ) 
			{
				contador ++;
				
				if(contador >= 5 )
				{
					cartaAlta = mano[i+1].getNumero();
				}
			}
		}
		
		if (contador >= 5)
		{
			return true; 
		}
		else 
			return false; 
	}

	public boolean hayColor(Carta[] mano)
	{
		int contador = 0; 
		int cartaAlta = 0; 
		
		for (int i = 0 ; i < mano.length-1 ; i ++)
		{	
			contador = 0; 
			for (int j = 0; j < mano.length -1 ; j ++)
			{
				if(mano[i].getPalo() == mano[j].getPalo() )
				{
					contador ++;
					
					if (contador >= 20 )
					{	
						cartaAlta = mano[i+1].getPalo();
						break;
					}
				}
			}
		}
		if(contador  >= 20) 
		{
			return true; 
		}
		else 
			return false; 
	}
	
	public int contarRepetidas (Carta[] mano)
	{
		int contador = 0; 
		
		for (int i = 0 ; i < mano.length ; i++)
		{
			for (int j = i; j < mano.length ; j ++)
			{
				if (i != j)
				{
					if(mano[i].getNumero() == mano[j].getNumero() )
						contador++;
				}
			}
		}
		
		return contador;
	}
	
	public boolean hayPar (Carta[] mano)
	{
		if (contarRepetidas (mano) == 1)
			return true; 
		else 
			return false; 
	}
	
	public boolean hayDoblePareja (Carta[] mano) 
	{
		if (contarRepetidas (mano) == 2)
			return true; 
		else 
			return false;
	}
	
	public boolean hayTrio (Carta[] mano)
	{
		if (contarRepetidas (mano) == 3) 
			return true; 
		else 
			return false; 
	}
	
	public boolean hayFull (Carta[] mano)
	{
		if (contarRepetidas (mano) == 4)
			return true; 
		else
			return true; 
	}
	
	public boolean hayPoker(Carta[] mano)
	{
		if(contarRepetidas (mano) == 6 ) 
			return true; 
		else
			return true; 
		
	}
	
	
	public boolean hayEscaleraColor(Carta[] mano)
	{
		int contador = 1; 
		int cartaAlta = 0;
		for (int i= 0 ; i < mano.length -1 ; i ++)
		{
			if (mano[i].getPalo() == mano[i+1].getPalo() 
				&& mano[i].getNumero()+1 == mano[i+1].getNumero()) 
			{
				contador ++; 
				if(contador >= 5)
					cartaAlta = mano[i+1].getNumero(); 
			}
		}
		
		if (contador >= 5)
			return true; 
		else 
			return false; 
	}
	
	public boolean hayEscaleraReal(Carta[] mano)
	{
		int contador = 1; 
		int cartaAlta = 0;
		for (int i= 0 ; i < mano.length -1 ; i ++)
		{
			if(i == 0  && mano[i].getNumero() == 10) 
			{
				contador++;
			}
			else if (mano[i].getPalo() == mano[i+1].getPalo() 
				&& mano[i].getNumero()+1 == mano[i+1].getNumero()) 
			{
				contador ++; 
				if(contador >= 5)
					cartaAlta = mano[i+1].getNumero(); 
			}
		}
		
		if (contador >= 5)
			return true; 
		else 
			return false; 
	}
	
	
	
	
	
}
