package ru.sberbank;

import org.HdrHistogram.Histogram;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.sberbank.optdemo1.AsyncHttpClientFactory;
import ru.sberbank.optdemo1.Client;
import ru.sberbank.optdemo1.Quote;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class Optdemo1Application {
    static Histogram histogram = new Histogram(3600000000000L, 4);
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SpringApplication.run(Optdemo1Application.class, args);
        AsyncHttpClient client = AsyncHttpClientFactory.create(new AsyncHttpClientFactory.AsyncHttpClientConfig());
        for (int i = 0; i < 100; i++) {
            long startTime = System.nanoTime();
            Response response = client.preparePost("http://localhost:8080/demo1/quotes?days=100").execute().get();
            long stopTime = System.nanoTime();
            histogram.recordValue(stopTime - startTime);
        }
        histogram.outputPercentileDistribution(System.out, 100.0);
    }

/*    @Autowired
    Client client;

    @Scheduled(fixedDelay = 1000000)
    public void preCaching() {
        try{
            List<Quote> listDays = client.getDays(100);
        } catch (Exception e){

        }
    }*/
}
