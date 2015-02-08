package delco.devbits.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class Charger_d_Image
  extends AsyncTask<Bitmap, Bitmap, Bitmap>
{
  public static final String url = "http://10.0.2.2/Djassa/photo/XperiaX10.jpg";
  public ImageView image;
  public String route;
  
  public Bitmap doInBackground(Bitmap... paramVarArgs)
  {
    Bitmap localBitmap = new Websupport().Telecharge_image("http://10.0.2.2/Djassa/photo/XperiaX10.jpg");
    this.image.setImageBitmap(localBitmap);
    return localBitmap;
  }
  
  public ImageView getImage()
  {
    return this.image;
  }
  
  public String getRoute()
  {
    return this.route;
  }
  
  public void setImage(ImageView paramImageView)
  {
    this.image = paramImageView;
  }
  
  public void setRoute(String paramString)
  {
    this.route = paramString;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.network.Charger_d_Image
 * JD-Core Version:    0.7.0.1
 */