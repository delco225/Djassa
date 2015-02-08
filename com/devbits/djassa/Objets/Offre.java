package com.devbits.djassa.Objets;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONObject;

public class Offre
  extends Objets
  implements Parcelable
{
  private boolean Acheter = false;
  private long Contacts;
  public int[] Evalutation = { 1, 2, 3, 4, 5 };
  private long IdClient;
  private String Lieu_Vente;
  private Double Prix;
  private Client Trader;
  private long idOffre;
  private String photo_path;
  
  public Offre(Parcel paramParcel)
  {
    super(" ");
    this.IdClient = paramParcel.readLong();
    this.photo_path = paramParcel.readString();
    this.idOffre = paramParcel.readLong();
    this.Prix = Double.valueOf(paramParcel.readDouble());
    this.Nom = paramParcel.readString();
    this.Lieu_Vente = paramParcel.readString();
  }
  
  public Offre(String paramString, Double paramDouble, Client paramClient)
  {
    super(paramString);
    setTrader(paramClient);
    setPrix(paramDouble);
  }
  
  public Offre(JSONObject paramJSONObject)
  {
    super("");
    this.IdClient = paramJSONObject.optLong("IdClient");
    this.photo_path = paramJSONObject.optString("Photo_offre");
    this.idOffre = paramJSONObject.optLong("idOffre");
    this.Prix = Double.valueOf(paramJSONObject.optDouble("prix"));
    this.Nom = paramJSONObject.optString("Nom");
    this.Acheter = paramJSONObject.optBoolean("Acheter");
    this.Lieu_Vente = paramJSONObject.optString("Lieu_Vente");
    this.Contacts = paramJSONObject.optLong("Contact");
    Log.v("Instanciation ", " Instanciation effectué avec succes ");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getContacts()
  {
    return this.Contacts;
  }
  
  public String getLieu_Vente()
  {
    return this.Lieu_Vente;
  }
  
  public String getPhoto_path()
  {
    return this.photo_path;
  }
  
  public Double getPrix()
  {
    return this.Prix;
  }
  
  public Client getTrader()
  {
    return this.Trader;
  }
  
  public void setContacts(long paramLong)
  {
    this.Contacts = paramLong;
  }
  
  public void setLieu_Vente(String paramString)
  {
    this.Lieu_Vente = paramString;
  }
  
  public void setPhoto_path(String paramString)
  {
    this.photo_path = paramString;
  }
  
  public void setPrix(Double paramDouble)
  {
    this.Prix = paramDouble;
  }
  
  public void setTrader(Client paramClient)
  {
    this.Trader = paramClient;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(this.IdClient);
    paramParcel.writeLong(this.idOffre);
    paramParcel.writeString(this.Nom);
    paramParcel.writeString(this.Lieu_Vente);
    paramParcel.writeDouble(this.Prix.doubleValue());
    paramParcel.writeString(this.photo_path);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     com.devbits.djassa.Objets.Offre
 * JD-Core Version:    0.7.0.1
 */