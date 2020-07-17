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

import static org.junit.Assert.assertFalse;

public class ValidationPartialShould {
    private static final Logger logger = LoggerFactory.getLogger(ValidationPartialShould.class);

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
    public void failGiveEmptyFirstName() throws Exception {
        PersonVars person = PersonVarsBuilder.fromValid()
                .setFirstName(null)
                .build();

        AnsibleExecutionResult result =  AnsibleRoleExecution.create("/role")
                .withMainOverride("partials/validate.yml")
                .withInputVariable("person", person)
                .executeInContainer(_container);

        assertFalse(result.isSuccessful());
    }
}