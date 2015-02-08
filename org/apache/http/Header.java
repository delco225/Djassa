package org.apache.http;

public abstract interface Header
{
  public abstract HeaderElement[] getElements()
    throws ParseException;
  
  public abstract String getName();
  
  public abstract String getValue();
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.Header
 * JD-Core Version:    0.7.0.1
 */