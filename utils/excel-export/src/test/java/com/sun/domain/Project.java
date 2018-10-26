package com.sun.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String projectName;
    
    private Date createTime;

}