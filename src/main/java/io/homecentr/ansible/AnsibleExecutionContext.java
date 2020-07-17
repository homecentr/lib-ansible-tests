package io.homecentr.ansible;

import java.util.*;

public class AnsibleExecutionContext {
    private final String executedTask;
    private Map<String, Object> inputVariables;
    private ArrayList<String> outputVariables;
    private String executionId;

    public AnsibleExecutionContext(String executedTask) {
        this.executedTask = executedTask;
        this.inputVariables = new HashMap<>();
        this.outputVariables = new ArrayList<>();

        this.executionId = UUID.randomUUID().toString();
    }

    public void addInputVariable(String variableName, Object value){
        inputVariables.put(variableName, value);
    }

    public void addOutputVariable(String variableName){
        outputVariables.add(variableName);
    }

    public Iterable<String> getOutputVariables() {
        return outputVariables;
    }

    public String getOutputVarPath(String variableName) {
        return String.format("%s/out-var-%s", getExecutionDirectory(), variableName);
    }

    public String getExecutionDirectory() {
        return String.format("/tmp/%s", executionId);
    }

    public String getPlaybookPath() {
        return String.format("%s/playbook.yml", getExecutionDirectory());
    }

    public String getTasksMainPath() {
        return String.format("%s/tasks/main.yml", getExecutionDirectory());
    }

    public String getExecutedTask() {
        return executedTask;
    }

    public Iterable<String> getInputVariables() {
        return this.inputVariables.keySet();
    }

    public Object getInputVariableValue(String name) {
        return this.inputVariables.get(name);
    }
}
