package org.apache.http.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public final class RegistryBuilder<I>
{
  private final Map<String, I> items = new HashMap();
  
  public static <I> RegistryBuilder<I> create()
  {
    return new RegistryBuilder();
  }
  
  public Registry<I> build()
  {
    return new Registry(this.items);
  }
  
  public RegistryBuilder<I> register(String paramString, I paramI)
  {
    Args.notEmpty(paramString, "ID");
    Args.notNull(paramI, "Item");
    this.items.put(paramString.toLowerCase(Locale.US), paramI);
    return this;
  }
  
  public String toString()
  {
    return this.items.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.config.RegistryBuilder
 * JD-Core Version:    0.7.0.1
 */