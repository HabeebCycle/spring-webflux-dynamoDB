package com.habeebcycle.dynamodbapi;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;

@SpringBootTest
class DynamodbApiApplicationTests {

	@Test
	void contextLoads() throws Exception {
		final String[] localArgs = { "-inMemory" };
		DynamoDBProxyServer server = ServerRunner.createServerFromCommandLineArgs(localArgs);
		server.start();
		DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-east-1"))
				.build());
		dynamoDB.listTables();
		server.stop();
	}

	@Test
	public void worksWithAwsDynamoDb() {
		final String PORT = System.getProperty("dynamodb.port");
		AmazonDynamoDB aws = new AmazonDynamoDBClient(
				new BasicAWSCredentials("xyz", "123xyz")
		);
		aws.setEndpoint(String.format("http://localhost:%s", PORT));
		aws.listTables();
		// and now your code
	}

}
