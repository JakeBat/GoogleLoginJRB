package edu.cnm.deepdive.googleloginjrb;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PassphraseService {

  @GET("diceware")
  Call<String[]> get(@Header("authorization") String authorization);

}
