package com.example.demo.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.demo.KafkaMessageVO;

@Configuration
public class KafkaProducerConfig {

    private final Environment env;

    KafkaProducerConfig(Environment environment) {
        this.env = environment;
    }

    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,env.getProperty("spring.kafka.producer.bootstrap-servers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", env.getProperty("spring.kafka.ssl.trust-store-location"));
        props.put("ssl.truststore.password", env.getProperty("spring.kafka.ssl.trust-store-password"));
        props.put("ssl.keystore.password", env.getProperty("spring.kafka.ssl.key-store-password"));
        props.put("ssl.keystore.location", env.getProperty("spring.kafka.ssl.key-store-location"));
        props.put("ssl.key.password", env.getProperty("spring.kafka.ssl.key-password"));
        props.put("ssl.enabled.protocols", "TLSv1.2");
        props.put("ssl.endpoint.identification.algorithm", "");
        return props;
    }


    public ProducerFactory<String, KafkaMessageVO> producerFactory() {
       return new DefaultKafkaProducerFactory<>(this.producerConfig());
    }

    @Bean
    public KafkaTemplate<String, KafkaMessageVO> kafkaTemplate() {
        return new KafkaTemplate<String, KafkaMessageVO>(this.producerFactory());
    }
}
