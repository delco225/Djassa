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

public class OffreFragment
  extends Fragment
{
  public static final String ARG_OBJECT = "object";
  public Context context;
  public ListView myListView = null;
  public ProgressDialog p2Dialog;
  
  public void SetContext(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903075, paramViewGroup, false);
    this.myListView = ((ListView)localView.findViewById(2131034203));
    this.p2Dialog = new ProgressDialog(this.context);
    Asychro localAsychro = new Asychro();
    localAsychro.setContext(this.context);
    localAsychro.setLv(this.myListView);
    localAsychro.execute(new Context[0]);
    return localView;
  }
  
  public class Asychro
    extends AsyncTask<Context, String, String>
  {
    public OffreAdapter Adapter = null;
    public Context Context1;
    public ListView lv;
    
    public Asychro() {}
    
    protected String doInBackground(Context... paramVarArgs)
    {
      this.Adapter = new OffreAdapter(this.Context1);
      return null;
    }
    
    public OffreAdapter getAdapter()
    {
      return this.Adapter;
    }
    
    public Context getContext()
    {
      return this.Context1;
    }
    
    public ListView getLv()
    {
      return this.lv;
    }
    
    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
      if (this.lv == null) {
        Log.v("VERIFICATION est bien null ", this.lv.toString());
      }
      Log.v("VERIFICATION ", this.lv.toString());
      this.lv.setAdapter(this.Adapter);
      OffreFragment.this.p2Dialog.dismiss();
    }
    
    protected void onPreExecute(Context paramContext)
    {
      super.onPreExecute();
      Toast.makeText(getContext(), " connexion a Djassa Smart open Market .....", 1).show();
      OffreFragment.this.p2Dialog.setMessage("Connexion à \n Djassa Smart Open Market .....");
      OffreFragment.this.p2Dialog.setIndeterminate(true);
      OffreFragment.this.p2Dialog.setCancelable(false);
      OffreFragment.this.p2Dialog.show();
    }
    
    public void setAdapter(OffreAdapter paramOffreAdapter)
    {
      this.Adapter = paramOffreAdapter;
    }
    
    public void setContext(Context paramContext)
    {
      this.Context1 = paramContext;
    }
    
    public void setLv(ListView paramListView)
    {
      this.lv = paramListView;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.djassa.OffreFragment
 * JD-Core Version:    0.7.0.1
 */