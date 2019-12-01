package ru.sberbank.optdemo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@EnableScheduling
@RequestMapping("/demo1")
public class Opdemo1Controller {

    private Client client;
    private static int counter = 0;

    @Autowired
    public void setClient(Client client) {
        this.client = client;
    }



    @RequestMapping("/quotes")
    public @ResponseBody
    List<Quote> getQuotes(@RequestParam("days") int days) {
        try {
            if (days <= 100) {
                List<Quote> listDays = client.getDays(100);
                return listDays.subList(0, days);
            } else {
                return client.getDays(days);
            }
        } catch (Exception e) {
            try {
                return client.getDays(days);
            } catch (ParseException | ExecutionException | InterruptedException e1) {
                e1.printStackTrace();
                return null;
            }
        }

    }

    @RequestMapping("/co")
    synchronized public void main() throws InterruptedException {
        counter += 1;
        if (counter == 3){
            Thread.sleep(5000);
        }
    }

}

