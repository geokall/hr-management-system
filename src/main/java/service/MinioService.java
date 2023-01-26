package service;

import dto.FileDTO;

public interface MinioService {

    void test();

    void updateBucketWithFileBy(String bucketName, FileDTO fileDTO);
}
