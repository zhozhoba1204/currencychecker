package com.currencyapi.currencychecker;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import java.net.URI;


@FeignClient(name="customerProfileAdapter")
public interface CurrencyProfileAdapter {

    @RequestLine("GET")
    public String getValue(URI baseUri);
}
