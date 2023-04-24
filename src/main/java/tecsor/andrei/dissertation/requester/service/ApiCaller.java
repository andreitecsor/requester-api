package tecsor.andrei.dissertation.requester.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiCaller {
    @GET("/provider/api/v1/{pid}")
    Call<Boolean> isClientAvailable(@Path("pid") String pid);
}