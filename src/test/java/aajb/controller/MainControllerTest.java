package aajb.controller;

import aajb.repository.ParentRepository;
import aajb.domain.school.Parent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.databene.benerator.anno.InvocationCount;
import org.databene.benerator.anno.Stochastic;
import org.databene.feed4junit.Feeder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by ayed.h on 23/02/2016.
 */
@RunWith(Feeder.class)
@Stochastic
public class MainControllerTest extends WebConfigurationsTest{

    @Autowired
    Environment env;

    @Autowired
    private ParentRepository parentRepository;


    @Test
    @InvocationCount(100)
    public void testCreateValidAndInvalidParents(
            @NotNull @Pattern(regexp = "[a-z A-Z]+[a-zA-Z]+") String firstName,
            @NotNull @Pattern(regexp = "[a-z A-Z]+[a-zA-Z]+") String lastName,
            @NotNull @Pattern(regexp = "[a-zA-Z0-9_\\-]{3,15}") String login,
            @NotNull @Pattern(regexp = "[0-9a-zA-Z?=\\.*]{6,20}") String password,
            @NotNull @Pattern(regexp = "[A-Za-z0-9]+[\\+_\\.\\-]*[A-Za-z0-9]+[@]([a-zA-Z0-9\\-]{1,10}\\.){1,3}[a-zA-Z]{2,6}") String email

    ) throws Exception {
        Assert.notNull(wac);

        //given
        Parent parent = new Parent();
        parent.setFirstName(firstName);
        parent.setLastName(lastName);
        parent.setLogin(login);
        parent.setEmail(email);
        parent.setPassword(password);

        assert  java.util.regex.Pattern.compile(env.getProperty("pattern.login"))
                .matcher(parent.getLogin()).matches();
        assert  java.util.regex.Pattern.compile(env.getProperty("pattern.firstName"))
                .matcher(parent.getFirstName()).matches();
        assert  java.util.regex.Pattern.compile(env.getProperty("pattern.lastName"))
                .matcher(parent.getLastName()).matches();
        assert  java.util.regex.Pattern.compile(env.getProperty("pattern.email"))
                .matcher(parent.getEmail()).matches();
        assert  java.util.regex.Pattern.compile(env.getProperty("pattern.password"))
                .matcher(parent.getPassword()).matches();

        //check login
        if (parentRepository.findByLogin(login)!=null) {
            return;
        }
        //check the email
        if (parentRepository.findByEmail(email)!=null) {
            return;
        }

        StringWriter sw = new StringWriter();
        (new ObjectMapper()).writeValue(sw,parent);

        //when
        String responseContent = mockMvc.perform(post("/api/parent")
                .contentType(APPLICATION_JSON_UTF8)
                .content(sw.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        Assert.notNull(responseContent);
        HashMap result = (new ObjectMapper()).readValue(responseContent, HashMap.class);

        Assert.isTrue("true".equals(result.get("status")));

        String id = (((LinkedHashMap) result.get("parent")).get("id")).toString();
        Assert.notNull(id);

        //delete created parent
        parentRepository.delete(Integer.parseInt(id));
        //assert that delete works
        Assert.isNull(parentRepository.findByLogin(login));
    }



    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8")
    );
}