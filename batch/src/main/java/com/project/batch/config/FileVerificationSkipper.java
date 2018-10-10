package com.project.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;

@Configuration
public class FileVerificationSkipper implements SkipPolicy {

    Logger logger = LoggerFactory.getLogger(FileVerificationSkipper.class);

    @Override
    public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {

        if(exception instanceof FileNotFoundException){
            return false;
        }else if(exception instanceof FlatFileParseException || exception instanceof IncorrectTokenCountException && skipCount <= 10){
            logger.warn("Record failed with: " + exception.getMessage());
            logger.warn("skipCount: " + skipCount);
            return true;
        }else
            return false;
    }
}
