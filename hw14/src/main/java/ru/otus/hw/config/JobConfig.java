package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaBookComments;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoBookComments;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.List;


@SuppressWarnings("unused")
@Configuration
public class JobConfig {
    public static final String IMPORT_USER_JOB_NAME = "importBookJob";

    private static final int CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public JpaPagingItemReader<JpaBook> readerBook(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<JpaBook>()
                .name("bookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from JpaBook b")
                .pageSize(1000)
                .build();
    }

    @Bean
    public JpaPagingItemReader<JpaBookComments> readerComment(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<JpaBookComments>()
                .name("commentItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from JpaBookComments c")
                .pageSize(1000)
                .build();
    }

    @Bean
    public JpaPagingItemReader<JpaAuthor> readerAuthor(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<JpaAuthor>()
                .name("authorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from JpaAuthor a")
                .pageSize(1000)
                .build();
    }

    @Bean
    public JpaPagingItemReader<JpaGenre> readerGenre(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<JpaGenre>()
                .name("authorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from JpaGenre a")
                .pageSize(1000)
                .build();
    }

    @Bean
    public MongoItemWriter<MongoBook> writerBook(MongoTemplate template) {
        return new MongoItemWriterBuilder<MongoBook>()
                .template(template)
                .build();
    }

    @Bean
    public MongoItemWriter<MongoBookComments> writerComment(MongoTemplate template) {
        return new MongoItemWriterBuilder<MongoBookComments>()
                .template(template)
                .build();
    }

    @Bean
    public MongoItemWriter<MongoAuthor> writerAuthor(MongoTemplate template) {
        return new MongoItemWriterBuilder<MongoAuthor>()
                .template(template)
                .build();
    }

    @Bean
    public MongoItemWriter<MongoGenre> writerGenre(MongoTemplate template) {
        return new MongoItemWriterBuilder<MongoGenre>()
                .template(template)
                .build();
    }

    @Bean
    public Job importBookJob(Step transformAuthorsStep, Step transformGenresStep,
                             Step transformBooksStep, Step transformCommentsStep) {
        return new JobBuilder(IMPORT_USER_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformAuthorsStep)
                .next(transformGenresStep)
                .next(transformBooksStep)
                .next(transformCommentsStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step transformBooksStep(ItemReader<JpaBook> reader, MongoItemWriter<MongoBook> writer,
                                   ItemProcessor<JpaBook, MongoBook> itemProcessor) {
        return new StepBuilder("transformBooksStep", jobRepository)
                .<JpaBook, MongoBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(getListenerReader())
                .listener(getListenerWriter())
                .listener(getListenerProcess())
                .listener(getListenerChunk())
                .build();
    }

    private ChunkListener getListenerChunk() {
        return new ChunkListener() {
            public void beforeChunk(@NonNull ChunkContext chunkContext) {
                logger.info("Начало пачки");
            }

            public void afterChunk(@NonNull ChunkContext chunkContext) {
                logger.info("Конец пачки");
            }

            public void afterChunkError(@NonNull ChunkContext chunkContext) {
                logger.info("Ошибка пачки");
            }
        };
    }

    private ItemProcessListener<JpaBook, MongoBook> getListenerProcess() {
        return new ItemProcessListener<JpaBook, MongoBook>() {
            public void beforeProcess(@NonNull JpaBook o) {
                logger.info("Начало обработки");
            }

            public void afterProcess(@NonNull JpaBook o, JpaBook o2) {
                logger.info("Конец обработки");
            }

            public void onProcessError(@NonNull JpaBook o, @NonNull Exception e) {
                logger.info("Ошибка обработки");
            }
        };
    }

    private ItemWriteListener<JpaBook> getListenerWriter() {
        return new ItemWriteListener<JpaBook>() {
            public void beforeWrite(@NonNull List<JpaBook> list) {
                logger.info("Начало записи");
            }

            public void afterWrite(@NonNull List<JpaBook> list) {
                logger.info("Конец записи");
            }

            public void onWriteError(@NonNull Exception e, @NonNull List<JpaBook> list) {
                logger.info("Ошибка записи");
            }
        };
    }

    private ItemReadListener<JpaBook> getListenerReader() {
        return new ItemReadListener<>() {
            public void beforeRead() {
                logger.info("Начало чтения");
            }

            public void afterRead(@NonNull JpaBook o) {
                logger.info("Конец чтения");
            }

            public void onReadError(@NonNull Exception e) {
                logger.info("Ошибка чтения");
            }
        };
    }

    @Bean
    public Step transformCommentsStep(ItemReader<JpaBookComments> reader, MongoItemWriter<MongoBookComments> writer,
                                     ItemProcessor<JpaBookComments, MongoBookComments> itemProcessor) {
        return new StepBuilder("transformCommentsStep", jobRepository)
                .<JpaBookComments, MongoBookComments>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transformAuthorsStep(ItemReader<JpaAuthor> reader, MongoItemWriter<MongoAuthor> writer,
                                     ItemProcessor<JpaAuthor, MongoAuthor> itemProcessor) {
        return new StepBuilder("transformAuthorsStep", jobRepository)
                .<JpaAuthor, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transformGenresStep(ItemReader<JpaGenre> reader, MongoItemWriter<MongoGenre> writer,
                                    ItemProcessor<JpaGenre, MongoGenre> itemProcessor) {
        return new StepBuilder("transformGenresStep", jobRepository)
                .<JpaGenre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }
}
