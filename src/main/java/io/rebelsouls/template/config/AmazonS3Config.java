package io.rebelsouls.template.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {

    @Value("${app.aws.key}")
    private String awsKey;
    
    @Value("${app.aws.secret}")
    private String awsSecret;
    
    @Bean
    public AmazonS3 buildClient() {
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsKey, awsSecret)))
                .withRegion(Regions.US_EAST_2)
                .build();
        
        return client;
    }
}
