package tecsor.andrei.dissertation.requester.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tecsor.andrei.dissertation.requester.service.ApiCaller;

@Component
public class RequesterConfig {
    @Bean
    public ApiCaller apiCaller() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(450, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(450, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        // TODO: 29.06.2023 extend timeout 

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8081")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiCaller.class);
    }
}