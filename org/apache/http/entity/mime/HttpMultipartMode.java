package org.apache.http.entity.mime;

public enum HttpMultipartMode
{
  static
  {
    BROWSER_COMPATIBLE = new HttpMultipartMode("BROWSER_COMPATIBLE", 1);
    RFC6532 = new HttpMultipartMode("RFC6532", 2);
    HttpMultipartMode[] arrayOfHttpMultipartMode = new HttpMultipartMode[3];
    arrayOfHttpMultipartMode[0] = STRICT;
    arrayOfHttpMultipartMode[1] = BROWSER_COMPATIBLE;
    arrayOfHttpMultipartMode[2] = RFC6532;
    $VALUES = arrayOfHttpMultipartMode;
  }
  
  private HttpMultipartMode() {}
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.HttpMultipartMode
 * JD-Core Version:    0.7.0.1
 */