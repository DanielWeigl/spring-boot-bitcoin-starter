package org.tbk.bitcoin.tool.fee.bitcoinerlive.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tbk.bitcoin.tool.fee.bitcoinerlive.BitcoinerliveFeeApiClient;
import org.tbk.bitcoin.tool.fee.bitcoinerlive.BitcoinerliveFeeApiClientImpl;
import org.tbk.bitcoin.tool.fee.bitcoinerlive.BitcoinerliveFeeProvider;

import static java.util.Objects.requireNonNull;

@Configuration
@EnableConfigurationProperties(BitcoinerliveFeeClientAutoConfigProperties.class)
@ConditionalOnProperty(name = {
        "org.tbk.bitcoin.tool.fee.enabled",
        "org.tbk.bitcoin.tool.fee.bitcoinerlive.enabled"
}, havingValue = "true", matchIfMissing = true)
public class BitcoinerliveFeeClientAutoConfiguration {

    private BitcoinerliveFeeClientAutoConfigProperties properties;

    public BitcoinerliveFeeClientAutoConfiguration(BitcoinerliveFeeClientAutoConfigProperties properties) {
        this.properties = requireNonNull(properties);
    }

    @Bean
    @ConditionalOnClass(BitcoinerliveFeeApiClient.class)
    @ConditionalOnMissingBean(BitcoinerliveFeeApiClient.class)
    public BitcoinerliveFeeApiClient bitcoinerliveFeeApiClient() {
        return new BitcoinerliveFeeApiClientImpl(properties.getBaseUrl(), properties.getToken().orElse(null));
    }

    @Bean
    @ConditionalOnClass(BitcoinerliveFeeProvider.class)
    @ConditionalOnMissingBean(BitcoinerliveFeeProvider.class)
    public BitcoinerliveFeeProvider bitcoinerliveFeeProvider(BitcoinerliveFeeApiClient bitcoinerliveFeeApiClient) {
        return new BitcoinerliveFeeProvider(bitcoinerliveFeeApiClient);
    }

}