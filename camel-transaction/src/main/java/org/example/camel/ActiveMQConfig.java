package org.example.camel;

import javax.jms.ConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQConnectionFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class ActiveMQConfig {

    @Bean
    public ActiveMQComponent activemq(
            ConnectionFactory connectionFactory,
            JtaTransactionManager jtaTransactionManager) {
        ActiveMQComponent component = new ActiveMQComponent();
        component.setAcknowledgementMode(JmsProperties.AcknowledgeMode.CLIENT.getMode());
        component.setCacheLevelName("CACHE_CONSUMER");
        component.setConnectionFactory(connectionFactory);
        component.setTransacted(true);
        component.setTransactionManager(jtaTransactionManager);
        return component;
    }

    @Bean
    public ActiveMQConnectionFactoryCustomizer activeMQConnectionFactoryCustomizer() {
        return factory -> {
            RedeliveryPolicy policy = factory.getRedeliveryPolicy();
            policy.setRedeliveryDelay(1000);
            policy.setMaximumRedeliveries(1); // https://activemq.apache.org/message-redelivery-and-dlq-handling
            factory.setRedeliveryPolicy(policy);
        };
    }

}