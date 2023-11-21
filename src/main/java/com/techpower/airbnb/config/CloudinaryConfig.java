package com.techpower.airbnb.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Cloudinary result = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzhdgoh2y",
                "api_key", "865541557228275",
                "api_secret", "g2fLw8LWY9sZ7JC5UTvEcle6udA",
                "secure", true
        ));
        return result;
    }
}
