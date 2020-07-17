package io.homecentr.ansible;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.StringWriter;

public class AnsiblePlaybookGenerator {

    public static String generate(AnsibleExecutionContext context) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("- hosts: 127.0.0.1\n");
        sb.append("  connection: local\n");
        sb.append("  tasks:\n");
        sb.append(String.format("    - %s \n", context.getExecutedTask()));

        for(String outputVar : context.getOutputVariables()) {
            sb.append(String.format("    - local_action: copy content={{ %s }} dest=\"%s\" \n", outputVar, context.getOutputVarPath(outputVar)));
        }

        sb.append("  vars:\n");

        for(String varName : context.getInputVariables()) {
            sb.append(String.format("    %s:\n", varName));

            Object varValue = context.getInputVariableValue(varName);
            String varYaml = getValueAsYaml(varValue);

            for(String line : varYaml.split("\\r?\\n")) {
                sb.append("      ");
                sb.append(line);
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    private static String getValueAsYaml(Object value) throws IOException {
        try (StringWriter writer = new StringWriter()){
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);
            mapper.writeValue(writer, value);

            writer.flush();

            // To remove the leading ---
            return writer.getBuffer().toString().substring(4);
        }
    }
}
