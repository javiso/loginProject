package com.example.project.project.mapper;

import com.example.project.project.model.LogUrl;
import dev.akkinoc.spring.boot.orika.OrikaMapperFactoryConfigurer;
import ma.glasnost.orika.MapperFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class Mapper  implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(LogUrl.class, LogUrl.class).byDefault().register();
        orikaMapperFactory.classMap(Page.class, PageApi.class).
                field("content", "items")
                .field("totalElements", "paging.total")
                .field("number", "paging.page")
                .field("size", "paging.size")
                .byDefault().register();
    }
}