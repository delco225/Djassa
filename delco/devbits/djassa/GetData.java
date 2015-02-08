package delco.devbits.djassa;

import com.devbits.djassa.Objets.Client;
import com.devbits.djassa.Objets.Market;
import com.devbits.djassa.Objets.Offre;
import delco.devbits.network.Websupport;
import java.util.ArrayList;
import java.util.List;

public class GetData
{
  private static final String TAG = " boooooommmmmm errererrr ";
  public List<Market> List_desMarkets = new ArrayList();
  public List<Offre> List_des_Offres = new ArrayList();
  
  public List<Offre> getData(String paramString)
  {
    this.List_des_Offres = new Websupport().Telecharge_Offres(paramString);
    return this.List_des_Offres;
  }
  
  public List<Market> getData_market(String paramString)
  {
    this.List_desMarkets = new Websupport().Telecharge_Market(paramString);
    return this.List_desMarkets;
  }
  
  public List<Offre> getDatafrom_offre()
  {
    for (int i = 0;; i++)
    {
      if (i >= 15) {
        return this.List_des_Offres;
      }
      Client localClient = new Client("koffi1", i);
      Offre localOffre = new Offre("Poulet de chaire 1", Double.valueOf(1000.2D + 5 / (i + 1)), localClient);
      this.List_des_Offres.add(localOffre);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.GetData
 * JD-Core Version:    0.7.0.1
 */