package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dto.BreadDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.enums.DayType;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.service.BreadService;
import com.dev.olivebakery.service.breadService.BreadGetService;
import com.dev.olivebakery.service.breadService.BreadSaveService;
import com.dev.olivebakery.service.breadService.BreadUpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(value = "/olive/bread")
@Log4j2
public class BreadController {

    private final BreadSaveService breadSaveService;
    private final BreadGetService breadGetService;
    private final BreadUpdateService breadUpdateService;


    private static final Logger logger = LoggerFactory.getLogger(BreadController.class);

    public BreadController(BreadSaveService breadSaveService, BreadGetService breadGetService,
                           BreadUpdateService breadUpdateService){
        this.breadSaveService = breadSaveService;
        this.breadGetService = breadGetService;
        this.breadUpdateService = breadUpdateService;
    }

    @ApiOperation("모든 빵 정보 가져오기")
    @GetMapping()
    public List<BreadDto.BreadGetAll> getAllBread(){

        return breadGetService.getAllBread();
    }

    @ApiOperation("요일별 빵 정보 가져오기")
    @GetMapping("/day/{day}")
    public List<BreadDto.BreadGetAll> getBread(@PathVariable String day){
        return breadGetService.getBreadByDay(day);
    }

    @ApiOperation("빵 상세정보 가져오기")
    @GetMapping("/name/{name}")
    public BreadDto.BreadGetDetail getDetail(@PathVariable String name){
        return breadGetService.getBreadDetails(name);
    }

    @ApiOperation("빵, 이미지 같이 저장")
    @PostMapping()
    public void saveBreadAndImage(@RequestPart MultipartFile file,
                                  @RequestParam String json) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        BreadDto.BreadSave breadSave = objectMapper.readValue(json, BreadDto.BreadSave.class);
        breadSaveService.saveBread(breadSave, file);
        //breadService.saveImage(files);
    }

    @ApiOperation("빵, 이미지 같이 저장 모델 명세")
    @PostMapping("/test")
    public void saveBreadAndImageTest( BreadDto.BreadSave breadSave) {
    }

    @ApiOperation("빵 이름 수정")
    @PutMapping(value = "/name")
    public void updateBreadName(@RequestBody BreadDto.BreadUpdateName bread){
        String updateName = breadUpdateService.updateBreadName(bread);

        if(!updateName.equals(bread.getNewName())){
            throw new UserDefineException("저장 실패");
        }
    }

    @ApiOperation("빵 정보 수정")
    @PutMapping()
    public void updateBread(@RequestBody BreadDto.BreadUpdate bread){
        breadUpdateService.updateBread(bread);
    }

    @ApiOperation("빵 사진 수정")
    @PutMapping("/image")
    public void updateBreadIamge(@RequestPart MultipartFile file,
                                 @RequestParam String breadName) throws Exception{

        breadUpdateService.updateBreadImage(file, breadName);
    }

    @ApiOperation("빵 상태 변경")
    @PutMapping("/state")
    public void changeBreadState(@PathVariable BreadDto.BreadUpdateState breadUpdateState){
        breadUpdateService.updateBreadState(breadUpdateState);
    }

    @ApiOperation("빵 매진 상태 변경")
    @PutMapping("/sold_out")
    public void changeBreadSoldOut(@PathVariable BreadDto.BreadUpdateSoldOut breadUpdateSoldOut){
        breadUpdateService.updateBreadSoldOut(breadUpdateSoldOut);
    }

    @ApiOperation("빵 삭제")
    @DeleteMapping("/name/{name}")
    public void deleteBread(@PathVariable String name){
        breadUpdateService.deleteBread(name);
    }

    @GetMapping(value = "/image/{image}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String image) throws IOException {

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(breadGetService.getImageResource(image));

    }

    @ApiOperation("빵 성분 추가")
    @PutMapping("/ingredient/add")
    public void addIngredient(@RequestBody BreadDto.BreadUpdateIngredients breadUpdateIngredients){

        logger.info(breadUpdateIngredients.getIngredientsList().get(0).getName());

        breadUpdateService.addBreadIngredients(breadUpdateIngredients);
    }

    @ApiOperation("빵 성분 삭제")
    @DeleteMapping("/ingredient/delete")
    public void deleteIngredient(@RequestBody BreadDto.BreadUpdateIngredients breadUpdateIngredients){

        logger.info(breadUpdateIngredients.getIngredientsList().get(0).getName());

        breadUpdateService.deleteBreadIngredients(breadUpdateIngredients);
    }


}