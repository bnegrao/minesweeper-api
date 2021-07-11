package com.zica.minesweeper.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.zica.minesweeper.game.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("#{environment.MONGODB_USER}")
    private String mongodbUser;

    @Value("#{environment.MONGODB_PWD}")
    private String mongodbPwd;

    @Value("#{environment.MONGODB_SERVER}")
    private String mongodbServer;

    @Value("#{environment.MONGODB_DATABASE_NAME}")
    private String mongodbDatabaseName;

    @Override
    protected String getDatabaseName() {
        return mongodbDatabaseName;
    }

    private final List<Converter<?, ?>> converters = new ArrayList<>();

    @Override
    public MongoClient mongoClient() {
        String connStr = "mongodb+srv://"+mongodbUser+":"+mongodbPwd+"@"+mongodbServer+"/myFirstDatabase?retryWrites=true&w=majority";

        ConnectionString connectionString = new ConnectionString(connStr);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(settings);
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Collections.singleton("com.zica.minesweeper.game");
    }

    @Override
    public MongoCustomConversions customConversions() {
        converters.add(StringToPositionConverter.INSTANCE);
        converters.add(PositionToStringConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    @ReadingConverter
    enum StringToPositionConverter implements Converter<String, Position> {
        INSTANCE;
        public Position convert(String source) {
            int row = Integer.parseInt(source.split(",")[0]);
            int column = Integer.parseInt(source.split(",")[1]);
            return new Position(row, column);
        }
    }

    @WritingConverter
    enum PositionToStringConverter implements Converter<Position, String> {
        INSTANCE;
        public String convert(Position source) {
            return source.toString();
        }
    }
}