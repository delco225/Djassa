package org.apache.commons.logging.impl;

import java.util.Enumeration;

class WeakHashtable$1
  implements Enumeration
{
  private final WeakHashtable this$0;
  private final Enumeration val$enumer;
  
  WeakHashtable$1(WeakHashtable paramWeakHashtable, Enumeration paramEnumeration)
  {
    this.this$0 = paramWeakHashtable;
    this.val$enumer = paramEnumeration;
  }
  
  public boolean hasMoreElements()
  {
    return this.val$enumer.hasMoreElements();
  }
  
  public Object nextElement()
  {
    return WeakHashtable.Referenced.access$100((WeakHashtable.Referenced)this.val$enumer.nextElement());
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.WeakHashtable.1
 * JD-Core Version:    0.7.0.1
 */