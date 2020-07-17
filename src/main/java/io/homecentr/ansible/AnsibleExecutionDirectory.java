package io.homecentr.ansible;

import io.homecentr.testcontainers.containers.GenericContainerEx;
import org.testcontainers.containers.Container;
import org.testcontainers.utility.MountableFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AnsibleExecutionDirectory {
    private final GenericContainerEx container;
    private final AnsibleExecutionContext context;

    public AnsibleExecutionDirectory(GenericContainerEx container, AnsibleExecutionContext context){
        this.container = container;
        this.context = context;
    }

    public void create() throws Exception {
        executeShellCommand("mkdir -p %s", this.context.getExecutionDirectory());
    }

    public void copyRole(String rolePath) throws Exception {
        executeShellCommand("cp -R %s/* %s", rolePath, this.context.getExecutionDirectory());
    }

    public void copyFile(String filePath) throws Exception {
        executeShellCommand("cp %s %s/", filePath, this.context.getExecutionDirectory());
    }

    public Map<String, String> readOutputVariables() throws IOException {
        HashMap<String, String> outputVars = new HashMap<>();

        for(String outputVar : this.context.getOutputVariables()) {
            String filePath = this.context.getOutputVarPath(outputVar);
            String value = readTextFile(filePath);

            outputVars.put(outputVar, value);
        }

        return outputVars;
    }

    public void writePlaybook(String playbookContent) throws IOException {
        writeTextFile(this.context.getPlaybookPath(), playbookContent);
    }

    public void writeTasksMain(String taskFileContent) throws IOException {
        writeTextFile(this.context.getTasksMainPath(), taskFileContent);
    }

    private void writeTextFile(String path, String content) throws IOException {
        File tmpFile = File.createTempFile("ansible-exec", "write");

        try (FileWriter writer = new FileWriter(tmpFile)){
            writer.write(content);
        }

        MountableFile mountableFile = MountableFile.forHostPath(tmpFile.getAbsolutePath());

        container.copyFileToContainer(mountableFile, path);

        tmpFile.delete();
    }

    private String readTextFile(String path) throws IOException {
        File tmpFile = File.createTempFile("ansible-exec", "read");

        container.copyFileFromContainer(path, tmpFile.getAbsolutePath());

        String content = new Scanner(tmpFile).useDelimiter("\\Z").next();

        tmpFile.delete();

        return content;
    }

    private void executeShellCommand(String command, String... args) throws Exception {
        String expandedCommand = String.format(command, (Object[]) args);
        Container.ExecResult result = this.container.executeShellCommand(expandedCommand);

        if(result.getExitCode() != 0) {
            String msg = String.format("The command '%s' has returned unexpected exit code %d.", expandedCommand, result.getExitCode());

            throw new Exception(msg);
        }
    }
}