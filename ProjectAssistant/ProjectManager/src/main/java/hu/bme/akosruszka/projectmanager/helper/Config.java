package hu.bme.akosruszka.projectmanager.helper;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;


@Configuration
public class Config {

    @Value("${activemq.broker.url}")
    private String brokerUrl;

    @Value("${activemq.queue.name}")
    private String queueName;

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        return factory;
    }

    @Bean
    public Destination destination() {
        return new ActiveMQQueue(queueName);
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate(activeMQConnectionFactory());
        template.setDefaultDestination(destination());
        template.setReceiveTimeout(5000);
        return template;
    }
}
