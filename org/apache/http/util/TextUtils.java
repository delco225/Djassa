package org.apache.http.util;

public final class TextUtils
{
  public static boolean isBlank(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null) {}
    for (;;)
    {
      return true;
      for (int i = 0; i < paramCharSequence.length(); i++) {
        if (!Character.isWhitespace(paramCharSequence.charAt(i))) {
          return false;
        }
      }
    }
  }
  
  public static boolean isEmpty(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null) {}
    while (paramCharSequence.length() == 0) {
      return true;
    }
    return false;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.util.TextUtils
 * JD-Core Version:    0.7.0.1
 */