package service;

import dto.BooleanOnlyDTO;
import dto.FileDTO;

public interface MinioService {

    BooleanOnlyDTO isBucketExistBy(String bucketName);

    void updateBucketWithFileBy(String bucketName, FileDTO fileDTO);
}
