package com.sun.helpers;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "files.docservice")
public class OfficeProperties {
    private String viewedDocs;
    private String editedDocs;
    private String convertDocs;
    private long timeout;

    private String server;

    private String secret;
    private String header;

    private Url url;

    @Data
    public static class Url {
        private String converter;
        private String tempstorage;
        private String api;
        private String preloade;
    }

}
