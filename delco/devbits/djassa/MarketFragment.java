package delco.devbits.djassa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class MarketFragment
  extends Fragment
{
  public Context context;
  public ListView malistview = null;
  public ProgressDialog monpDialog;
  
  public Context getContext()
  {
    return this.context;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903074, paramViewGroup, false);
    this.monpDialog = new ProgressDialog(this.context);
    this.malistview = ((ListView)localView.findViewById(2131034202));
    AsychrMarket localAsychrMarket = new AsychrMarket();
    localAsychrMarket.setContext(this.context);
    localAsychrMarket.setLv(this.malistview);
    localAsychrMarket.execute(new Context[0]);
    return localView;
  }
  
  public void setContext(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public class AsychrMarket
    extends AsyncTask<Context, String, String>
  {
    public Market_Adapter AdapterMarket = null;
    public Context Context1;
    public ListView lv0;
    
    public AsychrMarket() {}
    
    protected String doInBackground(Context... paramVarArgs)
    {
      this.AdapterMarket = new Market_Adapter(this.Context1);
      return null;
    }
    
    public Context getContext()
    {
      return this.Context1;
    }
    
    public ListView getLv()
    {
      return this.lv0;
    }
    
    public Market_Adapter getMarket_Adapter()
    {
      return this.AdapterMarket;
    }
    
    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
      if (this.lv0 == null) {
        Log.v("VERIFICATION est bien null ", this.lv0.toString());
      }
      Log.v("VERIFICATION ", this.lv0.toString());
      this.lv0.setAdapter(this.AdapterMarket);
      MarketFragment.this.monpDialog.dismiss();
    }
    
    protected void onPreExecute(Context paramContext)
    {
      Toast.makeText(getContext(), " connexion a Djassa Smart open Market .....", 1).show();
      super.onPreExecute();
      MarketFragment.this.monpDialog.setMessage("Connexion à \n Djassa Smart Open Market .....");
      MarketFragment.this.monpDialog.setIndeterminate(true);
      MarketFragment.this.monpDialog.setCancelable(false);
      MarketFragment.this.monpDialog.show();
    }
    
    public void setAdapter(Market_Adapter paramMarket_Adapter)
    {
      this.AdapterMarket = paramMarket_Adapter;
    }
    
    public void setContext(Context paramContext)
    {
      this.Context1 = paramContext;
    }
    
    public void setLv(ListView paramListView)
    {
      this.lv0 = paramListView;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.MarketFragment
 * JD-Core Version:    0.7.0.1
 */