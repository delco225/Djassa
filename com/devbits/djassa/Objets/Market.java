package com.devbits.djassa.Objets;

import android.util.Log;
import org.json.JSONObject;

public class Market
  extends Objets
{
  private Client[] Abonnees;
  private Offre[] Catalogues;
  private Long Contacts;
  private String Lieu;
  private String Lieu_Vente;
  private int Telephone;
  public String Type;
  private String Url;
  private Client Vendor;
  private String nbOffre;
  private String photo_path;
  
  public Market(String paramString, Client paramClient, int paramInt)
  {
    super(paramString);
    this.Vendor = paramClient;
    this.Telephone = paramInt;
  }
  
  public Market(JSONObject paramJSONObject)
  {
    super("");
    this.photo_path = paramJSONObject.optString("Photo_Markets");
    Log.v("Ok presence", "Photo " + this.photo_path);
    this.Nom = paramJSONObject.optString("Nom_Markets");
    this.Lieu_Vente = paramJSONObject.optString("Lieu_VenteM");
    this.Contacts = Long.valueOf(paramJSONObject.optLong("Contact"));
    this.Type = paramJSONObject.optString("type");
    Log.v("Instanciation ", " Instanciation effectué avec succes ");
  }
  
  public Long getContact()
  {
    return this.Contacts;
  }
  
  public String getLieu()
  {
    return this.Lieu;
  }
  
  public String getLieu_Vente()
  {
    return this.Lieu_Vente;
  }
  
  public String getNbOffre()
  {
    return this.nbOffre;
  }
  
  public String getPhoto_path()
  {
    return this.photo_path;
  }
  
  public int getTelephone()
  {
    return this.Telephone;
  }
  
  public String getType()
  {
    return this.Type;
  }
  
  public String getUrl()
  {
    return this.Url;
  }
  
  public Client getVendor()
  {
    return this.Vendor;
  }
  
  public void setLieu(String paramString)
  {
    this.Lieu = paramString;
  }
  
  public void setLieu_Vente(String paramString)
  {
    this.Lieu_Vente = paramString;
  }
  
  public void setNbOffre(String paramString)
  {
    this.nbOffre = paramString;
  }
  
  public void setPhoto_path(String paramString)
  {
    this.photo_path = paramString;
  }
  
  public void setTelephone(int paramInt)
  {
    this.Telephone = paramInt;
  }
  
  public void setType(String paramString)
  {
    this.Type = paramString;
  }
  
  public void setUrl(String paramString)
  {
    this.Url = paramString;
  }
  
  public void setVendor(Client paramClient)
  {
    this.Vendor = paramClient;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     com.devbits.djassa.Objets.Market
 * JD-Core Version:    0.7.0.1
 */