package edu.cnm.deepdive.googleloginjrb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.Arrays;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  private String idToken;
  private PassphraseService service;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    Button request = findViewById(R.id.request_passphrase);
    request.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        new RequestPassphraseTask().execute();
      }
    });
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(getString(R.string.service_url))
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    service = retrofit.create(PassphraseService.class);
    idToken = getTestApplication().getGoogleAccount().getIdToken();
  }

  private TestApplication getTestApplication() {
    return (TestApplication) getApplication();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_logout:
        getTestApplication().getSignInClient().signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                getTestApplication().setGoogleAccount(null);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              }
            });
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private class RequestPassphraseTask extends AsyncTask<Void, Void, String[]> {

    @Override
    protected String[] doInBackground(Void... voids) {
      try {
        Response<String[]> response = service.get(getString(R.string.authorization_header, idToken)).execute();
        return response.body();
      } catch (Exception e) {
        Log.d("debug", e.getMessage());
        cancel(true);
        return null;
      }
    }

    @Override
    protected void onPostExecute(String[] response) {
      EditText passphrase = findViewById(R.id.passphrase);
      String concatResponse = Arrays.toString(response)
          .replaceAll(getString(R.string.comma_regex), " ");
      passphrase.setText(concatResponse.substring(1, concatResponse.length() - 1));
    }
  }
}
