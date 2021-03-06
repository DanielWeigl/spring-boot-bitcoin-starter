package org.tbk.bitcoin.zeromq.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import java.util.Optional;

@Value
public class BitcoinZmqClientConfig {

    public static BitcoinZmqClientConfigBuilder builder() {
        return new BitcoinZmqClientConfigBuilder();
    }

    NetworkParameters network;

    String zmqpubhashtx; // Publishes transaction hashes
    String zmqpubhashblock; // Publishes block hashes
    String zmqpubrawblock; // Publishes raw block information
    String zmqpubrawtx; // Publishes raw transaction information

    public Optional<String> getZmqpubhashtx() {
        return Optional.ofNullable(zmqpubhashtx);
    }

    public Optional<String> getZmqpubhashblock() {
        return Optional.ofNullable(zmqpubhashblock);
    }

    public Optional<String> getZmqpubrawblock() {
        return Optional.ofNullable(zmqpubrawblock);
    }

    public Optional<String> getZmqpubrawtx() {
        return Optional.ofNullable(zmqpubrawtx);
    }

    public NetworkParameters getNetwork() {
        return Optional.ofNullable(network)
                .orElse(MainNetParams.get());
    }

    @Getter
    @RequiredArgsConstructor
    public static class BitcoinZmqClientConfigBuilder {
        private NetworkParameters network;

        private String zmqpubhashtx;
        private String zmqpubhashblock;
        private String zmqpubrawblock;
        private String zmqpubrawtx;

        public BitcoinZmqClientConfigBuilder network(NetworkParameters network) {
            this.network = network;
            return this;
        }

        public BitcoinZmqClientConfigBuilder zmqpubhashtx(String zmqpubhashtx) {
            this.zmqpubhashtx = zmqpubhashtx;
            return this;
        }

        public BitcoinZmqClientConfigBuilder zmqpubhashblock(String zmqpubhashblock) {
            this.zmqpubhashblock = zmqpubhashblock;
            return this;
        }

        public BitcoinZmqClientConfigBuilder zmqpubrawblock(String zmqpubrawblock) {
            this.zmqpubrawblock = zmqpubrawblock;
            return this;
        }

        public BitcoinZmqClientConfigBuilder zmqpubrawtx(String zmqpubrawtx) {
            this.zmqpubrawtx = zmqpubrawtx;
            return this;
        }

        public BitcoinZmqClientConfig build() {
            return new BitcoinZmqClientConfig(network, zmqpubhashtx, zmqpubhashblock, zmqpubrawblock, zmqpubrawtx);
        }
    }
}
