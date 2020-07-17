package io.homecentr.ansible;

public class AnsibleTasksMainGenerator {
    public static String generate(String... taskFiles){
        StringBuilder sb = new StringBuilder();

        for (String taskFile : taskFiles){
            sb.append(String.format("- include_tasks: %s\n", taskFile));
        }

        return sb.toString();
    }
}
