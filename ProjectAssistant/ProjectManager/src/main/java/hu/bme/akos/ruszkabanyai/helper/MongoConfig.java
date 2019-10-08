package hu.bme.akos.ruszkabanyai.helper;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "hu.bme.akos.ruszkabanyai.dao")
public class MongoConfig extends AbstractMongoConfiguration {
    @Override
    public MongoClient mongoClient() {
        return new MongoClient("localhost");
    }

    @Override
    protected String getDatabaseName() {
        return "projectAssistant";
    }

}
