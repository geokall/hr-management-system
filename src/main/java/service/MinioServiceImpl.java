package service;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MinioServiceImpl implements MinioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioServiceImpl.class);

    private final MinioClient minioClient;

    @Inject
    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @SneakyThrows
    @Override
    public void test() {
//        BucketExistsArgs qq = new BucketExistsArgs.Builder().bucket("test").build();
//        MakeBucketArgs test = new MakeBucketArgs.Builder().bucket("test").build();
//        boolean b = minioClient.bucketExists(qq);

        boolean test = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket("test").build());
        LOGGER.info("lalakis: {}", test);

    }
}
