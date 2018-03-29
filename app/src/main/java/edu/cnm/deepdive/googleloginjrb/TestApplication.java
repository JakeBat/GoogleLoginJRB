package edu.cnm.deepdive.googleloginjrb;

import android.app.Application;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class TestApplication extends Application{

  private GoogleSignInClient signInClient;
  private GoogleSignInAccount googleAccount;

  @Override
  public void onCreate() {
    super.onCreate();
    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestId()
        .requestIdToken(getString(R.string.sign_in_client_id))
        .build();
    signInClient = GoogleSignIn.getClient(this, options);
  }

  public GoogleSignInClient getSignInClient() {
    return signInClient;
  }

  public GoogleSignInAccount getGoogleAccount() {
    return googleAccount;
  }

  public void setGoogleAccount(GoogleSignInAccount googleAccount) {
    this.googleAccount = googleAccount;
  }
}
