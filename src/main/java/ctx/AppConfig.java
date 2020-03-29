package ctx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class only creates the {@link App} object
 * when the Spring Framework starts.
 */
@Configuration
public class AppConfig {

    @Bean
    public App getApplicationContextProvider() {
        return new App();
    }
}
