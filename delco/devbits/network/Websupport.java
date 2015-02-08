package delco.devbits.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import com.devbits.djassa.Objets.Market;
import com.devbits.djassa.Objets.Offre;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

public class Websupport
{
  private static final String TAG = null;
  Context context;
  private String url;
  
  public static String convertInputStreamToString(InputStream paramInputStream)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (;;)
    {
      try
      {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
        str = localBufferedReader.readLine();
        if (str != null) {
          continue;
        }
      }
      catch (Exception localException)
      {
        String str;
        Log.e(TAG, "Erreur dans la convertion " + localException.toString());
        continue;
      }
      return localStringBuilder.toString();
      localStringBuilder.append(str + "\n");
    }
  }
  
  public List<Market> Telecharge_Market(String paramString)
  {
    this.url = paramString;
    ArrayList localArrayList = new ArrayList();
    JSONParser localJSONParser = new JSONParser();
    new JSONArray();
    JSONArray localJSONArray = localJSONParser.getJSONFromUrl(paramString);
    for (int i = 0;; i++)
    {
      if (i >= localJSONArray.length()) {
        return localArrayList;
      }
      localArrayList.add(new Market(localJSONArray.optJSONObject(i)));
    }
  }
  
  public List<Offre> Telecharge_Offres(String paramString)
  {
    this.url = paramString;
    ArrayList localArrayList = new ArrayList();
    JSONParser localJSONParser = new JSONParser();
    new JSONArray();
    JSONArray localJSONArray = localJSONParser.getJSONFromUrl(paramString);
    for (int i = 0;; i++)
    {
      if (i >= localJSONArray.length()) {
        return localArrayList;
      }
      localArrayList.add(new Offre(localJSONArray.optJSONObject(i)));
    }
  }
  
  public Bitmap Telecharge_image(String paramString)
  {
    try
    {
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)new URL(paramString).openConnection();
      localHttpURLConnection.setDoInput(true);
      localHttpURLConnection.connect();
      Bitmap localBitmap = BitmapFactory.decodeStream(localHttpURLConnection.getInputStream());
      return localBitmap;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
  
  public boolean Yaconnexion_internet()
  {
    ConnectivityManager localConnectivityManager = (ConnectivityManager)this.context.getSystemService("connectivity");
    NetworkInfo[] arrayOfNetworkInfo;
    if (localConnectivityManager != null)
    {
      arrayOfNetworkInfo = localConnectivityManager.getAllNetworkInfo();
      if (arrayOfNetworkInfo == null) {}
    }
    for (int i = 0;; i++)
    {
      if (i >= arrayOfNetworkInfo.length) {
        return false;
      }
      if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
        return true;
      }
    }
  }
  
  public Context getContext()
  {
    return this.context;
  }
  
  public InputStream getServerData_Get()
  {
    try
    {
      InputStream localInputStream = new DefaultHttpClient().execute(new HttpPost(this.url)).getEntity().getContent();
      return localInputStream;
    }
    catch (Exception localException)
    {
      Log.e("erreur dans laconnexion ", localException.getMessage());
    }
    return null;
  }
  
  /* Error */
  public String getServerData_post()
  {
    // Byte code:
    //   0: ldc 207
    //   2: astore_1
    //   3: ldc 207
    //   5: astore_2
    //   6: new 72	java/util/ArrayList
    //   9: dup
    //   10: invokespecial 73	java/util/ArrayList:<init>	()V
    //   13: astore_3
    //   14: aload_3
    //   15: new 209	org/apache/http/message/BasicNameValuePair
    //   18: dup
    //   19: ldc 211
    //   21: ldc 213
    //   23: invokespecial 216	org/apache/http/message/BasicNameValuePair:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   26: invokevirtual 217	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   29: pop
    //   30: new 176	org/apache/http/impl/client/DefaultHttpClient
    //   33: dup
    //   34: invokespecial 177	org/apache/http/impl/client/DefaultHttpClient:<init>	()V
    //   37: astore 5
    //   39: new 179	org/apache/http/client/methods/HttpPost
    //   42: dup
    //   43: aload_0
    //   44: getfield 70	delco/devbits/network/Websupport:url	Ljava/lang/String;
    //   47: invokespecial 180	org/apache/http/client/methods/HttpPost:<init>	(Ljava/lang/String;)V
    //   50: astore 6
    //   52: aload 6
    //   54: new 219	org/apache/http/client/entity/UrlEncodedFormEntity
    //   57: dup
    //   58: aload_3
    //   59: invokespecial 222	org/apache/http/client/entity/UrlEncodedFormEntity:<init>	(Ljava/util/List;)V
    //   62: invokevirtual 226	org/apache/http/client/methods/HttpPost:setEntity	(Lorg/apache/http/HttpEntity;)V
    //   65: aload 5
    //   67: aload 6
    //   69: invokeinterface 186 2 0
    //   74: invokeinterface 192 1 0
    //   79: invokeinterface 197 1 0
    //   84: astore 23
    //   86: aload 23
    //   88: astore 9
    //   90: new 25	java/io/BufferedReader
    //   93: dup
    //   94: new 27	java/io/InputStreamReader
    //   97: dup
    //   98: aload 9
    //   100: ldc 228
    //   102: invokespecial 32	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   105: bipush 8
    //   107: invokespecial 231	java/io/BufferedReader:<init>	(Ljava/io/Reader;I)V
    //   110: astore 10
    //   112: new 22	java/lang/StringBuilder
    //   115: dup
    //   116: invokespecial 23	java/lang/StringBuilder:<init>	()V
    //   119: astore 11
    //   121: aload 10
    //   123: invokevirtual 39	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   126: astore 20
    //   128: aload 20
    //   130: ifnonnull +81 -> 211
    //   133: aload 9
    //   135: invokevirtual 236	java/io/InputStream:close	()V
    //   138: aload 11
    //   140: invokevirtual 42	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   143: astore 21
    //   145: aload 21
    //   147: astore_1
    //   148: new 80	org/json/JSONArray
    //   151: dup
    //   152: aload_1
    //   153: invokespecial 237	org/json/JSONArray:<init>	(Ljava/lang/String;)V
    //   156: astore 14
    //   158: iconst_0
    //   159: istore 15
    //   161: aload 14
    //   163: invokevirtual 89	org/json/JSONArray:length	()I
    //   166: istore 18
    //   168: iload 15
    //   170: iload 18
    //   172: if_icmplt +99 -> 271
    //   175: aload_2
    //   176: areturn
    //   177: astore 7
    //   179: ldc 239
    //   181: new 22	java/lang/StringBuilder
    //   184: dup
    //   185: ldc 241
    //   187: invokespecial 51	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   190: aload 7
    //   192: invokevirtual 60	java/lang/Exception:toString	()Ljava/lang/String;
    //   195: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: invokevirtual 42	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   201: invokestatic 66	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   204: pop
    //   205: aconst_null
    //   206: astore 9
    //   208: goto -118 -> 90
    //   211: aload 11
    //   213: new 22	java/lang/StringBuilder
    //   216: dup
    //   217: aload 20
    //   219: invokestatic 48	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
    //   222: invokespecial 51	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   225: ldc 53
    //   227: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   230: invokevirtual 42	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   233: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   236: pop
    //   237: goto -116 -> 121
    //   240: astore 12
    //   242: ldc 239
    //   244: new 22	java/lang/StringBuilder
    //   247: dup
    //   248: ldc 243
    //   250: invokespecial 51	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   253: aload 12
    //   255: invokevirtual 60	java/lang/Exception:toString	()Ljava/lang/String;
    //   258: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: invokevirtual 42	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   264: invokestatic 66	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   267: pop
    //   268: goto -120 -> 148
    //   271: new 22	java/lang/StringBuilder
    //   274: dup
    //   275: ldc 245
    //   277: invokespecial 51	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   280: aload 14
    //   282: iload 15
    //   284: invokevirtual 248	org/json/JSONArray:getJSONObject	(I)Lorg/json/JSONObject;
    //   287: invokevirtual 251	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   290: invokevirtual 42	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   293: astore 19
    //   295: aload 19
    //   297: astore_2
    //   298: iinc 15 1
    //   301: goto -140 -> 161
    //   304: astore 16
    //   306: ldc 239
    //   308: new 22	java/lang/StringBuilder
    //   311: dup
    //   312: ldc 253
    //   314: invokespecial 51	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   317: aload 16
    //   319: invokevirtual 254	org/json/JSONException:toString	()Ljava/lang/String;
    //   322: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   325: invokevirtual 42	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   328: invokestatic 66	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   331: pop
    //   332: aload_2
    //   333: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	334	0	this	Websupport
    //   2	151	1	localObject1	Object
    //   5	328	2	localObject2	Object
    //   13	46	3	localArrayList	ArrayList
    //   37	29	5	localDefaultHttpClient	DefaultHttpClient
    //   50	18	6	localHttpPost	HttpPost
    //   177	14	7	localException1	Exception
    //   88	119	9	localInputStream1	InputStream
    //   110	12	10	localBufferedReader	BufferedReader
    //   119	93	11	localStringBuilder	StringBuilder
    //   240	14	12	localException2	Exception
    //   156	125	14	localJSONArray	JSONArray
    //   159	140	15	i	int
    //   304	14	16	localJSONException	org.json.JSONException
    //   166	7	18	j	int
    //   293	3	19	str1	String
    //   126	92	20	str2	String
    //   143	3	21	str3	String
    //   84	3	23	localInputStream2	InputStream
    // Exception table:
    //   from	to	target	type
    //   30	86	177	java/lang/Exception
    //   90	121	240	java/lang/Exception
    //   121	128	240	java/lang/Exception
    //   133	145	240	java/lang/Exception
    //   211	237	240	java/lang/Exception
    //   148	158	304	org/json/JSONException
    //   161	168	304	org/json/JSONException
    //   271	295	304	org/json/JSONException
  }
  
  public String getURL()
  {
    return this.url;
  }
  
  public void setContext(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public void setURL(String paramString)
  {
    this.url = paramString;
  }
  
  public int uploadFile(String paramString)
  {
    i = 0;
    File localFile = new File(paramString);
    if (!localFile.isFile())
    {
      Log.e("uploadFile", "Source File not exist :");
      return 0;
    }
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(localFile);
      URL localURL = new URL("http://10.0.2.2/Djassa/received.php");
      HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURL.openConnection();
      localHttpURLConnection.setDoInput(true);
      localHttpURLConnection.setDoOutput(true);
      localHttpURLConnection.setUseCaches(false);
      localHttpURLConnection.setRequestMethod("POST");
      localHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
      localHttpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
      localHttpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "*****");
      localHttpURLConnection.setRequestProperty("uploaded_file", paramString);
      DataOutputStream localDataOutputStream = new DataOutputStream(localHttpURLConnection.getOutputStream());
      int j;
      byte[] arrayOfByte;
      int k;
      String str;
      int m;
      label435:
      label475:
      return i;
    }
    catch (MalformedURLException localMalformedURLException1)
    {
      try
      {
        localDataOutputStream.writeBytes("--" + "*****" + "\r\n");
        localDataOutputStream.writeBytes("Content-Disposition: form-data; name=" + "" + ";filename=" + paramString + "\r\n");
        localDataOutputStream.writeBytes("\r\n");
        j = Math.min(localFileInputStream.available(), 1048576);
        arrayOfByte = new byte[j];
        for (k = localFileInputStream.read(arrayOfByte, 0, j);; k = m)
        {
          i = 0;
          if (k <= 0)
          {
            localDataOutputStream.writeBytes("\r\n");
            localDataOutputStream.writeBytes("--" + "*****" + "--" + "\r\n");
            i = localHttpURLConnection.getResponseCode();
            str = localHttpURLConnection.getResponseMessage();
            Log.i("uploadFile", "HTTP Response is : " + str + ": " + i);
            localFileInputStream.close();
            localDataOutputStream.flush();
            localDataOutputStream.close();
            break;
          }
          localDataOutputStream.write(arrayOfByte, 0, j);
          j = Math.min(localFileInputStream.available(), 1048576);
          m = localFileInputStream.read(arrayOfByte, 0, j);
        }
        localMalformedURLException1 = localMalformedURLException1;
      }
      catch (Exception localException2)
      {
        break label475;
      }
      catch (MalformedURLException localMalformedURLException2)
      {
        break label435;
      }
      localMalformedURLException1.printStackTrace();
      Log.e("Upload file to server", "error: " + localMalformedURLException1.getMessage(), localMalformedURLException1);
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
      Log.e("Upload file to server Exception", "Exception : " + localException1.getMessage(), localException1);
    }
  }
  
  public class JSONParser
  {
    InputStream is = null;
    JSONArray jObj = null;
    String json = "";
    
    public JSONParser() {}
    
    /* Error */
    public JSONArray getJSONFromUrl(String paramString)
    {
      // Byte code:
      //   0: new 39	org/apache/http/impl/client/DefaultHttpClient
      //   3: dup
      //   4: invokespecial 40	org/apache/http/impl/client/DefaultHttpClient:<init>	()V
      //   7: astore_2
      //   8: new 42	org/apache/http/client/methods/HttpGet
      //   11: dup
      //   12: aload_1
      //   13: invokespecial 45	org/apache/http/client/methods/HttpGet:<init>	(Ljava/lang/String;)V
      //   16: astore_3
      //   17: aload_2
      //   18: aload_3
      //   19: invokeinterface 51 2 0
      //   24: astore 12
      //   26: aload 12
      //   28: astore 5
      //   30: aload 5
      //   32: invokeinterface 57 1 0
      //   37: astore 6
      //   39: aload_0
      //   40: aload 6
      //   42: invokestatic 63	org/apache/http/util/EntityUtils:toString	(Lorg/apache/http/HttpEntity;)Ljava/lang/String;
      //   45: putfield 27	delco/devbits/network/Websupport$JSONParser:json	Ljava/lang/String;
      //   48: aload_0
      //   49: new 65	org/json/JSONArray
      //   52: dup
      //   53: aload_0
      //   54: getfield 27	delco/devbits/network/Websupport$JSONParser:json	Ljava/lang/String;
      //   57: invokespecial 66	org/json/JSONArray:<init>	(Ljava/lang/String;)V
      //   60: putfield 23	delco/devbits/network/Websupport$JSONParser:jObj	Lorg/json/JSONArray;
      //   63: aload_0
      //   64: getfield 23	delco/devbits/network/Websupport$JSONParser:jObj	Lorg/json/JSONArray;
      //   67: areturn
      //   68: astore 11
      //   70: aload 11
      //   72: invokevirtual 69	org/apache/http/client/ClientProtocolException:printStackTrace	()V
      //   75: aconst_null
      //   76: astore 5
      //   78: goto -48 -> 30
      //   81: astore 4
      //   83: aload 4
      //   85: invokevirtual 70	java/io/IOException:printStackTrace	()V
      //   88: aconst_null
      //   89: astore 5
      //   91: goto -61 -> 30
      //   94: astore 10
      //   96: aload 10
      //   98: invokevirtual 71	org/apache/http/ParseException:printStackTrace	()V
      //   101: goto -53 -> 48
      //   104: astore 7
      //   106: aload 7
      //   108: invokevirtual 70	java/io/IOException:printStackTrace	()V
      //   111: goto -63 -> 48
      //   114: astore 8
      //   116: ldc 73
      //   118: new 75	java/lang/StringBuilder
      //   121: dup
      //   122: ldc 77
      //   124: invokespecial 78	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   127: aload 8
      //   129: invokevirtual 81	org/json/JSONException:toString	()Ljava/lang/String;
      //   132: invokevirtual 85	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   135: invokevirtual 86	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   138: invokestatic 92	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   141: pop
      //   142: goto -79 -> 63
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	145	0	this	JSONParser
      //   0	145	1	paramString	String
      //   7	11	2	localDefaultHttpClient	DefaultHttpClient
      //   16	3	3	localHttpGet	org.apache.http.client.methods.HttpGet
      //   81	3	4	localIOException1	IOException
      //   28	62	5	localHttpResponse1	HttpResponse
      //   37	4	6	localHttpEntity	HttpEntity
      //   104	3	7	localIOException2	IOException
      //   114	14	8	localJSONException	org.json.JSONException
      //   94	3	10	localParseException	org.apache.http.ParseException
      //   68	3	11	localClientProtocolException	org.apache.http.client.ClientProtocolException
      //   24	3	12	localHttpResponse2	HttpResponse
      // Exception table:
      //   from	to	target	type
      //   17	26	68	org/apache/http/client/ClientProtocolException
      //   17	26	81	java/io/IOException
      //   39	48	94	org/apache/http/ParseException
      //   39	48	104	java/io/IOException
      //   48	63	114	org/json/JSONException
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     delco.devbits.network.Websupport
 * JD-Core Version:    0.7.0.1
 */