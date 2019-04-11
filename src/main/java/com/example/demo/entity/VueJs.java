package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "logstash-*", type = "doc", shards = 1, replicas = 0)
@Getter
@Setter
@ToString
public class VueJs {
    @Id
    private long id;
    private String thread_name;
    private String timestamp;
    private String host;
    private String level_value;
    private String port;
    private String logger_name;
    private String level;
    private String message;
    private String version;

}
