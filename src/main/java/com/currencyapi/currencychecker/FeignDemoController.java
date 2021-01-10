package com.currencyapi.currencychecker;


import feign.Feign;
import feign.Request;
import feign.Target;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

@Controller
@Import(FeignClientsConfiguration.class)
public class FeignDemoController {
    @Value("${app_id}")
    private String id;

    @Value("${gif_app_id}")
    private String gif_id;

    @Value("${cur_url}")
    private String cur_url;

    @Value("${gif_url}")
    private String gif_url;

    @Value("${base}")
    private String base;

    CurrencyProfileAdapter currencyProfileAdapter;

    @Autowired
    public FeignDemoController(Decoder decoder, Encoder encoder){
        currencyProfileAdapter = Feign.builder().encoder(encoder).decoder(decoder)
                .target(Target.EmptyTarget.create(CurrencyProfileAdapter.class));
    }

    @GetMapping
    public ModelAndView getMain(){
        return new ModelAndView("main");
    }


    @PostMapping(value = "/check")
    public ModelAndView checkVal(@RequestParam String currency, RedirectAttributes redirectAttributes) throws URISyntaxException {
        ModelAndView modelAndView = new ModelAndView("redirect:/");

        if (currency==""){
            redirectAttributes.addFlashAttribute("message", "Задан пустой запрос");
            return modelAndView;
        }
        currency = currency.toUpperCase();

        String currentVal = currencyProfileAdapter
                .getValue(new URI(cur_url+"/latest.json?app_id="+id+"&base="+base+"&symbols="+currency));

        BigDecimal rateNow = null;
        try{
            rateNow = CurrencyUtil.getNumber(currentVal, currency);
        }catch (JSONException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message","введен неправильный код валюты");
            return modelAndView;
        }
        String yesterdayDate = CurrencyUtil.getYesterdayDate();

        String yesterdayVal = currencyProfileAdapter
                .getValue(new URI(cur_url+"/historical/"+yesterdayDate
                        + ".json?app_id="+id+"&base="+base+"&symbols="+currency));

        BigDecimal rateYesterday = null;
        try{
            rateYesterday = CurrencyUtil.getNumber(yesterdayVal, currency);
        }catch (JSONException e){
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("offset", new Random().nextInt(50));
        redirectAttributes.addFlashAttribute("gif_id", gif_id);
        redirectAttributes.addFlashAttribute("gif_url", gif_url);
        if (rateNow.compareTo(rateYesterday) > 0){
            redirectAttributes.addFlashAttribute("q","rich");
            return modelAndView;
        }
        else {
            redirectAttributes.addFlashAttribute("q", "broke");
            return modelAndView;
        }
    }

}
