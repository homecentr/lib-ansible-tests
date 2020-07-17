package io.homecentr.ansible;

import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AnsibleDockerImages {
    private static ImageFromDockerfile _ansibleImage;

    public static ImageFromDockerfile defaultAnsibleImage() throws IOException {
        if(_ansibleImage == null) {
            String dockerfile = ReadResourceAsString("ansible-image/Dockerfile");

            _ansibleImage = new ImageFromDockerfile()
                    .withFileFromString("Dockerfile", dockerfile);
        }

        return _ansibleImage;
    }

    private static String ReadResourceAsString(String resourceName) throws IOException {
        StringBuilder sb = new StringBuilder();

        try(InputStream dockerFileStream = AnsibleDockerImages.class.getClassLoader().getResourceAsStream(resourceName)) {
            try (InputStreamReader reader = new InputStreamReader(dockerFileStream)) {
                try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                    String str;

                    while ((str = bufferedReader.readLine()) != null) {
                        sb.append(str);
                        sb.append("\n");
                    }
                }
            }
        }

        return sb.toString();
    }
}
