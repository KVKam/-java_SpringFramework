package org.example.config;

import org.example.ContactsProfile;
import org.example.InitProfileContact;
import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:application.properties")
@Profile("init")
public class InitAppConfig {

    @Bean
    public ContactsProfile contactsProfile(){
        return new InitProfileContact();
    }
}
