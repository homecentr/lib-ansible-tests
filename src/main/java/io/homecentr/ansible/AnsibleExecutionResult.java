package io.homecentr.ansible;

import org.testcontainers.containers.Container;

import java.util.Map;

public class AnsibleExecutionResult {
    private final Container.ExecResult ansibleExecResult;
    private final Map<String, String> outputVariables;

    protected AnsibleExecutionResult(Container.ExecResult ansibleExecResult, Map<String, String> outputVariables) {
        this.ansibleExecResult = ansibleExecResult;
        this.outputVariables = outputVariables;
    }

    public boolean isSuccessful() {
        return ansibleExecResult.getExitCode() == 0;
    }

    public String getOutputVariable(String varName) {
        return outputVariables.get(varName);
    }
}
