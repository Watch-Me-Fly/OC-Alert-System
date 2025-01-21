package com.safetynet.alertsystem.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsystem.model.Data;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class JsonReaderRepository {

    private final ResourceLoader loader;
    private static final Logger LOGGER = Logger.getLogger(JsonReaderRepository.class.getName());
    @Getter private Data data;

    @Autowired
    public JsonReaderRepository(ResourceLoader loader, @Value("${test.json.path}") String jsonPath)
    {
        this.loader = loader;
        this.data = new Data();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // load json file from ressources
            Resource resource = loader.getResource("classpath:" + jsonPath);
            LOGGER.log(Level.INFO, "Loading file from : " + resource.getURI());
            // check if resource exists
            if (!resource.exists()) {
                LOGGER.log(Level.SEVERE, "Resource not found: " + jsonPath);
            }
            // read json contents
            data = mapper.readValue(resource.getInputStream(), Data.class);
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Error reading json file : ", e);
        }
    }
}
