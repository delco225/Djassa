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
import com.devbits.djassa.Objets.Offre;
import delco.devbits.network.Call_Management;
import delco.devbits.network.Sms_Mangment;
import delco.devbits.network.Websupport;
import java.util.ArrayList;
import java.util.List;

public class OffreAdapter
  extends BaseAdapter
{
  public static String Load_Path = "http://projects.emerginov.ci/Djassa/photo/";
  public List<Bitmap> ListBt_image = new ArrayList();
  public List<Offre> Listing_offre = null;
  Context context;
  public LayoutInflater inflater = null;
  
  public OffreAdapter(Context paramContext)
  {
    this.context = paramContext;
    this.inflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    this.Listing_offre = new GetData().getData("http://projects.emerginov.ci/Djassa/Offres.php");
    Log.v("TEST DE PReSENCE", ((Offre)this.Listing_offre.get(0)).getPrix() + " " + ((Offre)this.Listing_offre.get(1)).getPrix());
    for (int i = 0;; i++)
    {
      if (i >= this.Listing_offre.size()) {
        return;
      }
      Bitmap localBitmap = new Websupport().Telecharge_image(Load_Path + ((Offre)this.Listing_offre.get(i)).getPhoto_path());
      this.ListBt_image.add(localBitmap);
    }
  }
  
  public int getCount()
  {
    if (this.Listing_offre == null) {
      Log.v("Liste Null Attention ", " c est bien la elle est vide effectivement");
    }
    return this.Listing_offre.size();
  }
  
  public Object getItem(int paramInt)
  {
    return this.Listing_offre.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return 0L;
  }
  
  public List<Offre> getListing_offre()
  {
    return this.Listing_offre;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = LayoutInflater.from(this.context).inflate(2130903067, paramViewGroup, false);
    }
    TextView localTextView1 = (TextView)paramView.findViewById(2131034186);
    TextView localTextView2 = (TextView)paramView.findViewById(2131034192);
    TextView localTextView3 = (TextView)paramView.findViewById(2131034190);
    ImageView localImageView1 = (ImageView)paramView.findViewById(2131034187);
    ImageView localImageView2 = (ImageView)paramView.findViewById(2131034191);
    localImageView1.setImageBitmap((Bitmap)this.ListBt_image.get(paramInt));
    final Offre localOffre = (Offre)this.Listing_offre.get(paramInt);
    localTextView1.setText(String.valueOf(localOffre.getPrix()) + "F CFA");
    localTextView2.setText(localOffre.getNom());
    localTextView3.setText("Market zone  " + localOffre.getLieu_Vente());
    localImageView2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(OffreAdapter.this.context);
        localBuilder.setTitle(" SMS /APPEL ");
        localBuilder.setMessage(" Negocier Par SMS ou par Appel ? ");
        localBuilder.setIcon(2130837613);
        localBuilder.setPositiveButton("APPEL", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
          {
            Call_Management localCall_Management = new Call_Management();
            localCall_Management.setContext(OffreAdapter.this.context);
            localCall_Management.Gsm_Call(Long.toString(this.val$Offert.getContacts()));
          }
        });
        localBuilder.setNegativeButton("SMS", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
          {
            Sms_Mangment localSms_Mangment = new Sms_Mangment();
            localSms_Mangment.setContext(OffreAdapter.this.context);
            try
            {
              localSms_Mangment.SmsSend(Long.toString(this.val$Offert.getContacts()));
              Toast.makeText(OffreAdapter.this.context, "Ok votre SMS est en cour d envoie" + Long.toString(this.val$Offert.getContacts()), 0).show();
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
  
  public void setListing_offre(List<Offre> paramList)
  {
    this.Listing_offre = paramList;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.OffreAdapter
 * JD-Core Version:    0.7.0.1
 */