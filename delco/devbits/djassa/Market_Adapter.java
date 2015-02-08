package delco.devbits.djassa;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.devbits.djassa.Objets.Market;
import delco.devbits.network.Call_Management;
import delco.devbits.network.Sms_Mangment;
import delco.devbits.network.Websupport;
import java.util.ArrayList;
import java.util.List;

public class Market_Adapter
  extends BaseAdapter
{
  public static String Load_Path = "http://projects.emerginov.ci/Djassa/photo/Market/";
  public List<Bitmap> ListBt_image = new ArrayList();
  public List<Market> Listing_Markets = null;
  Context context;
  public LayoutInflater inflater = null;
  
  public Market_Adapter(Context paramContext)
  {
    this.context = paramContext;
    this.inflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    this.Listing_Markets = new GetData().getData_market("http://projects.emerginov.ci/Djassa/Market.php");
    Log.v("TEST DE PReSENCE", ((Market)this.Listing_Markets.get(0)).getNom());
    for (int i = 0;; i++)
    {
      if (i >= this.Listing_Markets.size()) {
        return;
      }
      Bitmap localBitmap = new Websupport().Telecharge_image(Load_Path + ((Market)this.Listing_Markets.get(i)).getPhoto_path());
      if (localBitmap == null) {
        Log.v("Erreru ", " Bt vide ");
      }
      this.ListBt_image.add(localBitmap);
    }
  }
  
  public Context getContext()
  {
    return this.context;
  }
  
  public int getCount()
  {
    return this.Listing_Markets.size();
  }
  
  public Object getItem(int paramInt)
  {
    return null;
  }
  
  public long getItemId(int paramInt)
  {
    return 0L;
  }
  
  public List<Market> getListing_Markets()
  {
    return this.Listing_Markets;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = LayoutInflater.from(this.context).inflate(2130903073, paramViewGroup, false);
    }
    TextView localTextView1 = (TextView)paramView.findViewById(2131034200);
    TextView localTextView2 = (TextView)paramView.findViewById(2131034199);
    TextView localTextView3 = (TextView)paramView.findViewById(2131034197);
    ImageView localImageView = (ImageView)paramView.findViewById(2131034198);
    localTextView2.setText(((Market)this.Listing_Markets.get(paramInt)).getNom());
    localTextView3.setText(((Market)this.Listing_Markets.get(paramInt)).getType());
    localTextView1.setText(((Market)this.Listing_Markets.get(paramInt)).getLieu_Vente());
    localImageView.setImageBitmap((Bitmap)this.ListBt_image.get(paramInt));
    ((ImageView)paramView.findViewById(2131034201)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(Market_Adapter.this.context);
        localBuilder.setTitle("APPEL / SMS  ");
        localBuilder.setMessage("  Par SMS ou par Appel ? ");
        localBuilder.setIcon(2130837613);
        localBuilder.setPositiveButton("APPEL", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
          {
            Call_Management localCall_Management = new Call_Management();
            localCall_Management.setContext(Market_Adapter.this.context);
            localCall_Management.Gsm_Call(Long.toString(this.val$Contact1.longValue()));
          }
        });
        localBuilder.setNegativeButton("SMS", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
          {
            Sms_Mangment localSms_Mangment = new Sms_Mangment();
            localSms_Mangment.setContext(Market_Adapter.this.context);
            try
            {
              localSms_Mangment.SmsSend(Long.toString(this.val$Contact1.longValue()));
              Toast.makeText(Market_Adapter.this.context, "Ok votre SMS est en cour d envoie" + Long.toString(this.val$Contact1.longValue()), 0).show();
              paramAnonymous2DialogInterface.cancel();
              return;
            }
            catch (Exception localException)
            {
              for (;;)
              {
                localException.printStackTrace();
                Log.v("Erreur d 'envoi ", " Xa coince Au niveau de l envoie man ..........");
              }
            }
          }
        });
        localBuilder.show();
      }
    });
    return paramView;
  }
  
  public void setContext(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public void setListing_Markets(List<Market> paramList)
  {
    this.Listing_Markets = paramList;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.Market_Adapter
 * JD-Core Version:    0.7.0.1
 */