package com.project.batch.config;

import com.project.batch.model.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class FileInfoItemProcessor implements ItemProcessor<FileInfo, FileInfo> {

    Logger logger = LoggerFactory.getLogger(FileInfoItemProcessor.class);

    @Override
    public FileInfo process(FileInfo fileInfo) throws Exception {

        logger.info("In Process for: " + fileInfo.getName());

        return fileInfo;
    }
}
