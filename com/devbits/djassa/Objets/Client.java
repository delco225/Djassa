package com.devbits.djassa.Objets;

public class Client
  extends Objets
{
  private String Email;
  private String Habitation;
  private int Numero;
  private String[] Préference;
  
  public Client(String paramString, int paramInt)
  {
    super(paramString);
    setNumero(paramInt);
  }
  
  public String getEmail()
  {
    return this.Email;
  }
  
  public String getHabitation()
  {
    return this.Habitation;
  }
  
  public int getNumero()
  {
    return this.Numero;
  }
  
  public String[] getPréference()
  {
    return this.Préference;
  }
  
  public void setEmail(String paramString)
  {
    this.Email = paramString;
  }
  
  public void setHabitation(String paramString)
  {
    this.Habitation = paramString;
  }
  
  public void setNumero(int paramInt)
  {
    this.Numero = paramInt;
  }
  
  public void setPréference(String[] paramArrayOfString)
  {
    this.Préference = paramArrayOfString;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     com.devbits.djassa.Objets.Client
 * JD-Core Version:    0.7.0.1
 */