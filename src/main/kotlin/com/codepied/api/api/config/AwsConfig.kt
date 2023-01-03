package com.codepied.api.api.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesAsyncClient

/**
 * AWS Configurations
 *
 * @author Aivyss
 * @since 2022/12/18
 */
@Configuration
class AwsConfig(
    private val awsProperty: AwsProperty,
) {
    @Bean
    fun awsCredentials(): AwsCredentials = AwsBasicCredentials.create(awsProperty.accessKeyId, awsProperty.secretAccessKey)

    @Bean
    fun sesClient(): SesAsyncClient {
        return SesAsyncClient.builder()
            .credentialsProvider { awsCredentials() }
            .region(Region.AP_NORTHEAST_2)
            .build()
    }

    @Bean
    fun s3Client(): AmazonS3 {
        val awsCreds = with(awsProperty) {
            BasicAWSCredentials(accessKeyId, secretAccessKey)
        }

        return AmazonS3ClientBuilder.standard()
            .withRegion(Regions.AP_NORTHEAST_2)
            .withCredentials(AWSStaticCredentialsProvider(awsCreds))
            .build()
    }
}