package com.example.project.project.mapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageApi<T extends Serializable> implements Serializable {
    private List<T> items;
    private Paging paging;

    public PageApi(List<T> items) { this.items = items; }
}
