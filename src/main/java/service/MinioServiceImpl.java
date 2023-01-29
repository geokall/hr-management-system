package service;

import dto.BooleanOnlyDTO;
import dto.FileDTO;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

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
    public BooleanOnlyDTO isBucketExistBy(String bucketName) {
        BucketExistsArgs bucketBuilder = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();

        boolean isExist = minioClient.bucketExists(bucketBuilder);

        BooleanOnlyDTO dto = new BooleanOnlyDTO();
        dto.setExist(isExist);

        return dto;
    }

    @SneakyThrows
    @Override
    public void updateBucketWithFileBy(String bucketName, FileDTO fileDTO) {
        BucketExistsArgs bucketBuilder = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();

        if (!minioClient.bucketExists(bucketBuilder)) {
            MakeBucketArgs makeBucket = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();

            minioClient.makeBucket(makeBucket);
            LOGGER.info("created bucket with name:" + bucketName);
        }

        String fileName = fileDTO.getFileName();

        if (!ObjectUtils.isEmpty(fileName)) {

            String actualFile = fileDTO.getActualFile();
            byte[] decodedFile = Base64.getDecoder().decode(actualFile);
            InputStream targetStream = new ByteArrayInputStream(decodedFile);
            long length = targetStream.available();

            PutObjectArgs putBuilder = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(targetStream, length, -1)
                    .contentType(fileDTO.getMimeType()).build();

            minioClient.putObject(putBuilder);
            LOGGER.info("added file on bucket: {} with filename :{}", bucketName, fileName);
        }

    }
}
