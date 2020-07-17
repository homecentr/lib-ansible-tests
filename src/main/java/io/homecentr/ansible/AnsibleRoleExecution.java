package io.homecentr.ansible;

import io.homecentr.testcontainers.containers.GenericContainerEx;
import org.testcontainers.containers.Container;

import java.io.IOException;
import java.util.Map;

public class AnsibleRoleExecution {
    public static AnsibleRoleExecution create(String rolePath) {
        return new AnsibleRoleExecution(rolePath);
    }

    private final String rolePath;
    private final AnsibleExecutionContext context;

    private String[] mainOverrideTaskFiles;

    private AnsibleRoleExecution(String rolePath) {
        String includeRoleTask = "include_role: name=./";

        this.rolePath = rolePath;
        this.context = new AnsibleExecutionContext(includeRoleTask);
    }

    public AnsibleRoleExecution withInputVariable(String name, Object value) {
        this.context.addInputVariable(name, value);
        return this;
    }

    public AnsibleRoleExecution withOutputVariable(String name) {
        this.context.addOutputVariable(name);
        return this;
    }

    public AnsibleRoleExecution withMainOverride(String... taskFiles) {
        mainOverrideTaskFiles = taskFiles;
        return this;
    }

    public AnsibleExecutionResult executeInContainer(GenericContainerEx container) throws Exception {
        AnsibleExecutionDirectory execDirectory = new AnsibleExecutionDirectory(container, context);

        execDirectory.create();
        execDirectory.copyRole(this.rolePath);
        execDirectory.writePlaybook(AnsiblePlaybookGenerator.generate(context));

        if(mainOverrideTaskFiles != null) {
            execDirectory.writeTasksMain(AnsibleTasksMainGenerator.generate(mainOverrideTaskFiles));
        }

        Container.ExecResult ansibleExecResult = this.executeAnsible(container);

        Map<String, String> outputVariables = execDirectory.readOutputVariables();

        return new AnsibleExecutionResult(ansibleExecResult, outputVariables);
    }

    private Container.ExecResult executeAnsible(GenericContainerEx container) throws IOException, InterruptedException {
        return container.executeShellCommand(String.format("ansible-playbook %s", this.context.getPlaybookPath()));
    }
}