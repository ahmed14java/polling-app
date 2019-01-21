package com.callicoder;

import com.callicoder.controller.AuthController;
import com.callicoder.controller.TestController;
import com.callicoder.model.Role;
import com.callicoder.model.RoleName;
import com.callicoder.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.convert.Jsr310Converters;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@EntityScan(basePackageClasses = {
        CallicoderJwtApplication.class,
        Jsr310Converters.class
})
@SpringBootApplication
public class CallicoderJwtApplication  implements CommandLineRunner{

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(CallicoderJwtApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName(RoleName.ROLE_USER) == null){
            Role role_user = new Role();
            Role role_admin = new Role();
            role_user.setName(RoleName.ROLE_USER);
            role_admin.setName(RoleName.ROLE_ADMIN);

            roleRepository.save(role_user);
            roleRepository.save(role_admin);
        }
    }
}
