package com.example.demo.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.demo.KafkaMessageVO;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final Environment env;

    KafkaConsumerConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,env.getProperty("spring.kafka.consumer.bootstrap-servers"));
        props.put(ConsumerConfig.GROUP_ID_CONFIG,env.getProperty("spring.kafka.consumer.group-id"));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,env.getProperty("spring.kafka.consumer.auto-offset-reset"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,JsonDeserializer.class);


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
    @Bean
    public ConsumerFactory<String, KafkaMessageVO> consumerFactory() {
        return new DefaultKafkaConsumerFactory<String, KafkaMessageVO>(this.consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(KafkaMessageVO.class,false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,KafkaMessageVO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String,KafkaMessageVO> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.consumerFactory());
        return factory;
    }
}
