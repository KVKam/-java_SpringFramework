package org.example.config;


import org.example.ContactsProfile;
import org.example.DefaultProfileContact;
import org.springframework.context.annotation.*;


@Configuration
@PropertySource("classpath:application.properties")
@Profile("default")
public class DefaultAppConfig {

    @Bean
    public ContactsProfile contactsProfile(){
        return new DefaultProfileContact();
    }
}
