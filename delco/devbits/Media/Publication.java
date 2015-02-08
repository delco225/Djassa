package delco.devbits.Media;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import com.devbits.djassa.Objets.Client;
import com.devbits.djassa.Objets.Offre;
import delco.devbits.djassa.Djassa_Main;
import delco.devbits.network.Websupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

public class Publication
  extends Activity
{
  private static int RESULT_LOAD_IMAGE = 1;
  public Bitmap bmp;
  ProgressDialog pDialog;
  public String picturePath;
  
  public void envoideDonnées()
  {
    EditText localEditText1 = (EditText)findViewById(2131034178);
    EditText localEditText2 = (EditText)findViewById(2131034177);
    ((EditText)findViewById(2131034181));
    EditText localEditText3 = (EditText)findViewById(2131034179);
    EditText localEditText4 = (EditText)findViewById(2131034180);
    Double localDouble = Double.valueOf(localEditText2.getText().toString());
    Offre localOffre = new Offre(localEditText1.getText().toString(), localDouble, new Client("moi", 3666957));
    localOffre.setContacts(Long.valueOf(localEditText3.getText().toString()).longValue());
    localOffre.setLieu_Vente(localEditText4.getText().toString());
    for (;;)
    {
      String str;
      try
      {
        File localFile = new File(this.picturePath);
        this.bmp = BitmapFactory.decodeFile(localFile.getAbsolutePath());
        MultipartEntityBuilder localMultipartEntityBuilder = MultipartEntityBuilder.create();
        localMultipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
        HttpPost localHttpPost = new HttpPost("http://projects.emerginov.ci/Djassa/offre_received.php");
        try
        {
          localMultipartEntityBuilder.addPart("Offre_image", new FileBody(localFile));
          localMultipartEntityBuilder.addTextBody("Nom", localOffre.getNom());
          localMultipartEntityBuilder.addTextBody("contact", String.valueOf(localOffre.getContacts()));
          localMultipartEntityBuilder.addTextBody("Prix", String.valueOf(localOffre.getPrix()));
        }
        catch (Exception localException2)
        {
          BufferedReader localBufferedReader;
          localException2.printStackTrace();
          Log.v("Erreur dans la conversion de l image ", " Lapp ses pas connecter ");
          localMultipartEntityBuilder.addTextBody("Image_envoye_manke", " rien");
          continue;
        }
        try
        {
          localMultipartEntityBuilder.addTextBody("lieu_de_vente", localOffre.getLieu_Vente());
          localHttpPost.setEntity(localMultipartEntityBuilder.build());
          localBufferedReader = new BufferedReader(new InputStreamReader(localDefaultHttpClient.execute(localHttpPost).getEntity().getContent(), "UTF-8"));
          localStringBuilder = new StringBuilder();
          str = localBufferedReader.readLine();
          if (str == null) {
            return;
          }
        }
        catch (Exception localException3)
        {
          localException3.printStackTrace();
          Log.v("Eurreur c est ic ", " xa coince man ");
          localMultipartEntityBuilder.addTextBody("lieu_de_vente", "INCONNU");
          continue;
        }
        StringBuilder localStringBuilder = localStringBuilder.append(str);
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
        return;
      }
      Log.v("REponse du sever ", str);
    }
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if ((paramInt1 == RESULT_LOAD_IMAGE) && (paramInt2 == -1) && (paramIntent != null))
    {
      Uri localUri = paramIntent.getData();
      String[] arrayOfString = { "_data" };
      Cursor localCursor = getContentResolver().query(localUri, arrayOfString, null, null, null);
      localCursor.moveToFirst();
      this.picturePath = localCursor.getString(localCursor.getColumnIndex(arrayOfString[0]));
      localCursor.close();
      ((ImageView)findViewById(2131034183)).setImageBitmap(BitmapFactory.decodeFile(this.picturePath));
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    new Websupport();
    setContentView(2130903066);
    ((ImageView)findViewById(2131034184)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Publication.this.startActivityForResult(localIntent, Publication.RESULT_LOAD_IMAGE);
      }
    });
    ((ImageView)findViewById(2131034182)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        new Publication.Envoie(Publication.this).execute(new String[0]);
      }
    });
  }
  
  public class Envoie
    extends AsyncTask<String, Integer, String>
  {
    public Envoie() {}
    
    protected String doInBackground(String... paramVarArgs)
    {
      Publication.this.envoideDonnées();
      return null;
    }
    
    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
      Publication.this.pDialog.setMessage("Article Publié avec succès ");
      Publication.this.pDialog.dismiss();
      Intent localIntent = new Intent(Publication.this, Djassa_Main.class);
      Publication.this.startActivity(localIntent);
    }
    
    protected void onPreExecute()
    {
      Publication.this.pDialog = new ProgressDialog(Publication.this);
      Publication.this.pDialog.setMessage("Publication en cours \n Patientez ....");
      Publication.this.pDialog.setIndeterminate(true);
      Publication.this.pDialog.setCancelable(false);
      Publication.this.pDialog.show();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.Media.Publication
 * JD-Core Version:    0.7.0.1
 */