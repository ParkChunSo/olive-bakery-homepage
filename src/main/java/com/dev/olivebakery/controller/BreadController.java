package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dtos.BreadDto;
import com.dev.olivebakery.domain.dtos.bread.BreadListResponseDto;
import com.dev.olivebakery.domain.dtos.bread.BreadRequestDto;
import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.service.breadService.BreadGetService;
import com.dev.olivebakery.service.breadService.BreadSaveService;
import com.dev.olivebakery.service.breadService.BreadUpdateService;
import com.dev.olivebakery.utill.BreadRequestDtoDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/olive/bread")
@RequiredArgsConstructor
@Log
public class BreadController {

    private final BreadSaveService breadSaveService;
    private final BreadGetService breadGetService;
    private final BreadUpdateService breadUpdateService;

    @ApiOperation("모든 빵 정보 가져오기")
    @GetMapping()
    public ResponseEntity<List<BreadListResponseDto>> getAllBread(){
        return ResponseEntity.ok(breadGetService.getAllBreadList());
    }

    @ApiOperation("금일 빵 정보 리스트 가져오기")
    @GetMapping("/today")
    public ResponseEntity<List<BreadListResponseDto>> getTodayBread(){
        return ResponseEntity.ok(breadGetService.getTodayBreadList());
    }

    @ApiOperation("요일별 빵 정보 가져오기")
    @GetMapping("/day/{day}")
    public ResponseEntity<List<BreadListResponseDto>> getBread(@PathVariable String day){
        return ResponseEntity.ok(breadGetService.getBreadListByDay(day));
    }

    @ApiOperation("빵 상세정보 가져오기")
    @GetMapping("/name/{name}")
    public ResponseEntity<BreadListResponseDto> getDetail(@PathVariable String name){
        return ResponseEntity.ok(breadGetService.getBreadDetail(name));
    }

    @ApiOperation("빵, 이미지 같이 저장")
    @PostMapping()
    public ResponseEntity<Bread> saveBreadAndImage(@RequestPart MultipartFile file,
                                                        @RequestParam String json) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        BreadDto.BreadSave breadSave = objectMapper.readValue(json, BreadDto.BreadSave.class);
        Bread bread = breadSaveService.saveBread(breadSave, file);

        return ResponseEntity.ok(bread);
    }
    @PostMapping("/test")
    public void test(@RequestPart MultipartFile file,
                     @RequestParam String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(BreadRequestDto.class, new BreadRequestDtoDeserializer());
        objectMapper.registerModule(simpleModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        BreadRequestDto breadSave = objectMapper.readValue(json, BreadRequestDto.class);
        breadSaveService.saveBread1(breadSave, file);
    }

    @ApiOperation("빵 정보 수정")
    @PutMapping()
    public ResponseEntity<Bread> updateBread(@RequestPart(name = "file", required = false) MultipartFile file,
                                             @RequestParam String json) throws Exception {
        return ResponseEntity.ok(breadUpdateService.updateBread(file, json));
    }

    @ApiOperation("빵 상태 변경")
    @PutMapping("/state")
    public ResponseEntity<Bread> changeBreadState(@RequestBody BreadDto.BreadUpdateState breadUpdateState){
        log.info("-----빵 상태 변경 컨트롤러");
        return ResponseEntity.ok(breadUpdateService.updateBreadState(breadUpdateState));
    }

    @ApiOperation("빵 매진 상태 변경")
    @PutMapping("/sold_out")
    public ResponseEntity<Bread> changeBreadSoldOut(@RequestBody  BreadDto.BreadUpdateSoldOut breadUpdateSoldOut){
        log.info("-----빵 매진상태 변경 컨트롤러");
        return ResponseEntity.ok(breadUpdateService.updateBreadSoldOut(breadUpdateSoldOut));
    }

    @ApiOperation("빵 삭제")
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Bread> deleteBread(@PathVariable String name){
        return ResponseEntity.ok(breadUpdateService.deleteBread(name));
    }

    @ApiOperation("빵 이미지 가져오기")
    @GetMapping(value = "/image/{image}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String image) throws IOException {

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .body(breadGetService.getImageResource(image));
    }

//    @ApiOperation("빵 정보에 성분 추가")
//    @PutMapping("/ingredients/add")
//    public ResponseEntity<HttpStatus> addIngredient(@RequestBody BreadDto.BreadUpdateIngredients breadUpdateIngredients){
//
//        logger.info(breadUpdateIngredients.getIngredients().get(0).getName());
//
//        breadUpdateService.addBreadIngredients(breadUpdateIngredients);
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @ApiOperation("빵 정보에 성분 삭제")
//    @DeleteMapping("/ingredients/delete")
//    public ResponseEntity<HttpStatus> deleteIngredient(@RequestBody BreadDto.BreadUpdateIngredients breadUpdateIngredients){
//
//        logger.info(breadUpdateIngredients.getIngredients().get(0).getName());
//
//        breadUpdateService.deleteBreadIngredients(breadUpdateIngredients);
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
}