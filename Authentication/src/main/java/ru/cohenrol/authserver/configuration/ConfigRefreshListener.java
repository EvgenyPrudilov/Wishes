package ru.cohenrol.authserver.configuration;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigRefreshListener {

    private final Environment environment;

    // Внедряем окружение Spring через конструктор
    public ConfigRefreshListener(Environment environment) {
        this.environment = environment;
    }

    @EventListener(EnvironmentChangeEvent.class)
    public void onConfigRefresh(EnvironmentChangeEvent event) {
        // Укажите имя вашей переменной для отслеживания
        String targetKey = "app.test-prop";

        if (event.getKeys().contains(targetKey)) {
            String newValue = environment.getProperty(targetKey);

            System.out.println("=========================================");
            System.out.println("[CONFIG UPDATED] Key '" + targetKey + "' has changed!");
            System.out.println("[CONFIG UPDATED] New value is: " + newValue);
            System.out.println("=========================================");
        }
    }
}