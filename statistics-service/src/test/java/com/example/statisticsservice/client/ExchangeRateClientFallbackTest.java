package com.example.statisticsservice.client;
import com.example.statisticsservice.domain.Currency;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExchangeRateClientFallbackTest {
    private MockWebServer mockWebServer;

    @Autowired
    private ExchangeRateClient exchangeRateClient;

    @BeforeEach
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        exchangeRateClient  = new ExchangeRateClient(WebClient.builder() , mockWebServer.url("/").toString());
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testFallbackOn401(){
        mockWebServer.enqueue(new MockResponse().setResponseCode(401));

        Map<Currency, BigDecimal> res =  exchangeRateClient.getExchangeRate("sd");
        assertNotNull(res);
        assertTrue(res.isEmpty());

    }





}