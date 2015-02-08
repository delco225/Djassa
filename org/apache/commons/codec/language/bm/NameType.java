package org.apache.commons.codec.language.bm;

public enum NameType
{
  private final String name;
  
  static
  {
    NameType[] arrayOfNameType = new NameType[3];
    arrayOfNameType[0] = ASHKENAZI;
    arrayOfNameType[1] = GENERIC;
    arrayOfNameType[2] = SEPHARDIC;
    $VALUES = arrayOfNameType;
  }
  
  private NameType(String paramString)
  {
    this.name = paramString;
  }
  
  public String getName()
  {
    return this.name;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.language.bm.NameType
 * JD-Core Version:    0.7.0.1
 */