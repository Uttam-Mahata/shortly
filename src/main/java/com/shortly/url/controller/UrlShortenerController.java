package com.shortly.url.controller;


import com.shortly.url.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @Autowired
    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestBody String longUrl) {
        return urlShortenerService.shortenUrl(longUrl);
    }

    @GetMapping("/{shortCode}")
    public String redirect(@PathVariable String shortCode) {
        return urlShortenerService.getLongUrl(shortCode);
    }
}

