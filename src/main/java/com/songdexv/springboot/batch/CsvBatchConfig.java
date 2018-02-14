/**
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.songdexv.springboot.batch;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import com.songdexv.springboot.dao.model.test.TUser;

/**
 * @author songdexv
 *
 */
@EnableBatchProcessing
public class CsvBatchConfig {
    @Bean
    public ItemReader<TUser> reader() throws Exception {
        FlatFileItemReader<TUser> reader = new FlatFileItemReader<TUser>(); // 1
        reader.setResource(new ClassPathResource("people.csv")); // 2
        reader.setLineMapper(new DefaultLineMapper<TUser>() {
            { // 3
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "name", "age", "nation", "address" });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<TUser>() {
                    {
                        setTargetType(TUser.class);
                    }
                });
            }
        });
        return reader;
    }
}
