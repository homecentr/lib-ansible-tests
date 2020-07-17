package io.homecentr.ansible;

import io.homecentr.testcontainers.containers.GenericContainerEx;
import org.testcontainers.containers.Container;

import java.io.IOException;
import java.util.Map;

public class JinjaTemplateExecution {
    public static JinjaTemplateExecution create(String templatePath) {
        return new JinjaTemplateExecution(templatePath);
    }

    private final String templatePath;
    private final AnsibleExecutionContext context;

    private JinjaTemplateExecution(String templatePath) {
        String setFact = String.format("set_fact: rendered_template=\"{{ lookup('template', '%s') }}\"", templatePath);

        this.templatePath = templatePath;
        this.context = new AnsibleExecutionContext(setFact);

        this.context.addOutputVariable("rendered_template");
    }

    public JinjaTemplateExecution withInputVariable(String name, Object value){
        this.context.addInputVariable(name, value);
        return this;
    }

    public String executeInContainer(GenericContainerEx container) throws Exception {
        AnsibleExecutionDirectory execDirectory = new AnsibleExecutionDirectory(container, context);

        execDirectory.create();
        execDirectory.writePlaybook(AnsiblePlaybookGenerator.generate(context));

        Container.ExecResult ansibleExecResult = this.executeAnsible(container);

        if(ansibleExecResult.getExitCode() != 0) {
            throw new Exception("Template execution has failed.");
        }

        Map<String, String> outputVariables = execDirectory.readOutputVariables();

        return outputVariables.get("rendered_template");
    }

    private Container.ExecResult executeAnsible(GenericContainerEx container) throws IOException, InterruptedException {
        return container.executeShellCommand(String.format("ansible-playbook %s", this.context.getPlaybookPath()));
    }
}
