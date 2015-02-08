package com.devbits.djassa.Objets;

import android.media.Image;

public abstract class Objets
{
  protected String Localisation;
  protected String Nom;
  protected Image photo;
  
  public Objets(String paramString)
  {
    setNom(paramString);
  }
  
  public String getLocalisation()
  {
    return this.Localisation;
  }
  
  public String getNom()
  {
    return this.Nom;
  }
  
  public Image getPhoto()
  {
    return this.photo;
  }
  
  public void setLocalisation(String paramString)
  {
    this.Localisation = paramString;
  }
  
  public void setNom(String paramString)
  {
    this.Nom = paramString;
  }
  
  public void setPhoto(Image paramImage)
  {
    this.photo = paramImage;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     com.devbits.djassa.Objets.Objets
 * JD-Core Version:    0.7.0.1
 */