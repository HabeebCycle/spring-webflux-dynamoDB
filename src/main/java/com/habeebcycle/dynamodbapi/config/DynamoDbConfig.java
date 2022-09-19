package com.habeebcycle.dynamodbapi.config;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.annotation.PreDestroy;
import java.net.URI;

@Configuration
public class DynamoDbConfig {

    private DynamoDBProxyServer server;

    @Bean
    public DynamoDbClient dynamoDbClient() throws Exception {
        //System.setProperty("sqlite4java.library.path", "native-libs");
        System.out.println(System.getProperty("sqlite4java.library.path"));
        startDynamoDbProxyServer();
        return DynamoDbClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                // The region is meaningless for local DynamoDb but required for client builder validation
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("dummy-key", "dummy-secret")))
                .build();
    }

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient() throws Exception {
        startDynamoDbProxyServer();
        return DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create("http://localhost:8000"))
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("dummy-key", "dummy-secret")))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() throws Exception {
        return DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(dynamoDbClient())
                        .build();

    }

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient() throws Exception {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbAsyncClient())
                .build();
    }

    private void startDynamoDbProxyServer() throws Exception {
        if(server == null) {
            server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-inMemory", "-port", "8000"});
            server.start();
        }
    }

    @PreDestroy
    public void stopProcessing() throws Exception {
        if(server != null) {
            server.stop();
        }
    }
}
