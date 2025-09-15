package cz.cyberrange.platform.commons.startup.config;

import cz.cyberrange.platform.commons.startup.StartUpRunner;
import cz.cyberrange.platform.commons.webclient.config.WebClientConfigSecurityCommons;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebClientConfigSecurityCommons.class, StartUpRunner.class})
public class MicroserviceRegistrationConfiguration {}
