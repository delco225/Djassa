package org.apache.http.auth;

public enum ChallengeState
{
  static
  {
    PROXY = new ChallengeState("PROXY", 1);
    ChallengeState[] arrayOfChallengeState = new ChallengeState[2];
    arrayOfChallengeState[0] = TARGET;
    arrayOfChallengeState[1] = PROXY;
    $VALUES = arrayOfChallengeState;
  }
  
  private ChallengeState() {}
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.ChallengeState
 * JD-Core Version:    0.7.0.1
 */