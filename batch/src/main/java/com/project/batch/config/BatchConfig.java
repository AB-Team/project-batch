package com.project.batch.config;

import com.project.batch.model.FileInfo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public FlatFileItemReader<FileInfo> reader() {
        return new FlatFileItemReaderBuilder<FileInfo>()
                .name("fileInfoItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"url", "name", "username"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<FileInfo>() {{
                    setTargetType(FileInfo.class);
                }})
                .build();
    }

    @Bean
    public FileInfoItemProcessor processor() {
        return new FileInfoItemProcessor();
    }

    @Bean
    public MongoItemWriter<FileInfo> writer() {
        MongoItemWriter<FileInfo> writer = new MongoItemWriter();

        writer.setTemplate(mongoTemplate);
        writer.setCollection("batchFiles");

        return writer;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("batchJob").incrementer(new RunIdIncrementer()).start(step1()).build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<FileInfo, FileInfo> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
