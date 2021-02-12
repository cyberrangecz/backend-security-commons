package cz.muni.ics.kypo.commons.startup.config;

import cz.muni.ics.kypo.commons.webclient.config.WebClientConfigSecurityCommons;
import cz.muni.ics.kypo.commons.startup.StartUpRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebClientConfigSecurityCommons.class, StartUpRunner.class})
public class MicroserviceRegistrationConfiguration {
}
