package com.dev.olivebakery.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class BreadImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long breadImageId;

    private String imageName;

    private String imageType;

    private String imagePath;

    private Long imageSize;

    private String imageUrl;

    private Boolean current ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bread_id")
    private Bread bread;

    public void changeCurrentStatus(Boolean status){
        this.current = status;
    }

    public static BreadImage of(MultipartFile imageFile, Bread bread, String imagePath) throws IOException {

        UUID uid = UUID.randomUUID(); // 유니크 값 생성

        String fileName = uid + "_" + imageFile.getOriginalFilename();


        String savePath = calcPath(imagePath);

        File destinationFile = new File(imagePath + savePath, fileName);

        imageFile.transferTo(destinationFile);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/olive/bread/image/" + bread.getName())
                .toUriString();

        return BreadImage.builder()
                .imageName(imageFile.getOriginalFilename())
                .imageSize(imageFile.getSize())
                .imageType(imageFile.getContentType())
                .imageUrl(imageUrl)
                .imagePath(imagePath + savePath + File.separator + fileName)
                .current(true)
                .bread(bread)
                .build();
    }

    public static String calcPath(String uploadPath) {

        Calendar cal = Calendar.getInstance();

        String yearPath = File.separator + cal.get(Calendar.YEAR); // 연도 별 폴더 경로

        String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1); // 월 별 폴더 경로

        String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE)); // 일 별 폴더 경로

        makeDir(uploadPath, yearPath, monthPath, datePath); // 폴더 생성

        return datePath;
    }

    // 폴더 생성 함수
    public static void makeDir(String uploadPath, String... paths) {

        if (new File(uploadPath + paths[paths.length - 1]).exists()) {
            return;
        } // 해당 경로의 폴더가 존재하면 반환

        for (String path : paths) {
            File dirPath = new File(uploadPath + path);

            if (!dirPath.exists()) {
                dirPath.mkdir();
            } // 해당 경로의 폴더 생성
        }
    }
}
