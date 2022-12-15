package com.example.eatwhat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
  
  
  
  // For image upload
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    exposeDirectory("recipe-photos", registry);
    registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/templates/","classpath:/static/")
            .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
  }
  
  private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
    Path uploadDir = Paths.get(dirName);
    String uploadPath = uploadDir.toFile().getAbsolutePath();
    
    if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
    
    registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
  }




}
