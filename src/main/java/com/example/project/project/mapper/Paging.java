package com.example.project.project.mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    private Integer page;
    private Integer size;
    private Integer total;
}