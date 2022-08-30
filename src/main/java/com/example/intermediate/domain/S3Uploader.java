package com.example.intermediate.domain;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.intermediate.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.lang.String;
import static com.example.intermediate.exception.ErrorCode.FAILURE_CONVERSION_FILE;
import static com.example.intermediate.exception.ErrorCode.NOT_IMAGE_FILE;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        if (!multipartFile.isEmpty()) {
            isImage(multipartFile);
        }

        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new CustomException(FAILURE_CONVERSION_FILE));

        return upload(uploadFile, dirName);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드

        removeNewFile(uploadFile);  //로컬에 생성된 File 삭제(MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 리턴
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead));  // Public Read 권한으로 업로드 됨
        return amazonS3Client.getUrl(bucket, fileName).toString();    // URL 받아옴
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기 / MultipartFile -> File로 변경
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }


    public void deleteImage(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }


    // 이미지 파일인지 확인하는 메소드
    private void isImage(MultipartFile multipartFile) throws IOException {

        // tika를 이용해 파일 MIME 타입 체크
        // 파일명에 .jpg 식으로 붙는 확장자는 없앨 수도 있고 조작도 가능하므로 MIME 타입을 체크하는 것이 좋다.
        Tika tika = new Tika();
        String mimeType = tika.detect(multipartFile.getInputStream());

        // MIME타입이 이미지가 아니면 exception 발생
        if (!mimeType.startsWith("image/")) {
            throw new CustomException(NOT_IMAGE_FILE);
        }
    }

}
