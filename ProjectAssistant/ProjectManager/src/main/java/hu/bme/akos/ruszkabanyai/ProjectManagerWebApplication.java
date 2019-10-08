package hu.bme.akos.ruszkabanyai;

import hu.bme.akos.ruszkabanyai.service.MessageService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.remoting.JmsInvokerProxyFactoryBean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import static hu.bme.akos.ruszkabanyai.helper.StringConstants.JMS_NAME;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class ProjectManagerWebApplication {

    public static void main(String[] args) {

        SpringApplication.run(ProjectManagerWebApplication.class, args);
    }

//    @Bean
//    Queue queue() {
//        return new ActiveMQQueue(JMS_NAME);
//    }
//
//    @Bean
//    FactoryBean invoker(@Qualifier("connectionFactory") ConnectionFactory factory, Queue queue) {
//        JmsInvokerProxyFactoryBean factoryBean = new JmsInvokerProxyFactoryBean();
//        factoryBean.setConnectionFactory(factory);
//        factoryBean.setServiceInterface(MessageService.class);
//        factoryBean.setQueue(queue);
//        return factoryBean;
//    }

}
