package com.shortly.url.service;


import com.shortly.url.entity.UrlMapping;
import com.shortly.url.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private final String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random = new Random();

    @Autowired
    public UrlShortenerService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String shortenUrl(String longUrl) {
        Optional<UrlMapping> existingMapping = urlRepository.findByLongUrl(longUrl);
        if (existingMapping.isPresent()) {
            return existingMapping.get().getShortCode();
        }

        String shortCode = generateShortCode();
        while (urlRepository.findByShortCode(shortCode).isPresent()) {
            shortCode = generateShortCode();
        }

        UrlMapping urlMapping = new UrlMapping(shortCode, longUrl, LocalDateTime.now(), null);
        urlRepository.save(urlMapping);
        return shortCode;
    }

    public String getLongUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));
    }

    private String generateShortCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(randomIndex));
        }
        return sb.toString();
    }
}

