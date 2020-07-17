package io.homecentr.ansible;

import io.homecentr.ansible.vars.PersonVars;
import io.homecentr.ansible.vars.PersonVarsBuilder;
import io.homecentr.testcontainers.containers.GenericContainerEx;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class DefaultPartialShould {
    private static final Logger logger = LoggerFactory.getLogger(DefaultPartialShould.class);

    private static GenericContainerEx _container;

    @BeforeClass
    public static void setUp() throws IOException {
        _container = new GenericContainerEx<>(AnsibleDockerImages.defaultAnsibleImage())
            .withRelativeFileSystemBind(Paths.get("./role"), "/role");

        _container.start();
        _container.followOutput(new Slf4jLogConsumer(logger));
    }

    @AfterClass
    public static void cleanUp() {
        _container.close();
    }

    @Test
    public void defaultEmailGivenNone() throws Exception {
        PersonVars person = PersonVarsBuilder.fromValid()
                .setEmail(null)
                .build();

        AnsibleExecutionResult result =  AnsibleRoleExecution.create("/role")
                .withMainOverride("partials/default.yml")
                .withOutputVariable("person.email")
                .withInputVariable("person", person)
                .executeInContainer(_container);

        assertEquals("noreply@default.com", result.getOutputVariable("person.email"));
    }
}