package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dtos.bread.BreadListResponseDto;
import com.dev.olivebakery.domain.dtos.bread.BreadRequestDto;
import com.dev.olivebakery.domain.dtos.bread.IngredientListResponseDto;
import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.service.breadService.BreadGetService;
import com.dev.olivebakery.service.breadService.BreadSaveService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    @ApiOperation("모든 빵 정보 가져오기")
    @GetMapping
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

    @ApiOperation("빵 재료의 이름과 원산지 가져오기")
    @GetMapping("/ingredients")
    public ResponseEntity<List<IngredientListResponseDto>> getIngredients(){
        return ResponseEntity.ok(breadGetService.getIngredientList());
    }

    @ApiOperation("빵, 이미지 같이 저장")
    @PostMapping
    public void saveBreadAndImage(@RequestPart MultipartFile file,
                                  @RequestParam String json) throws Exception{
        BreadRequestDto breadSave = objectMapper.readValue(json, BreadRequestDto.class);
        breadSaveService.saveBread(breadSave, file);
    }

    @ApiOperation("빵 정보 수정")
    @PutMapping
    public void updateBread(@RequestPart(name = "file", required = false) MultipartFile file,
                                             @RequestParam String json) throws Exception {
        BreadRequestDto breadSave = objectMapper.readValue(json, BreadRequestDto.class);
        breadSaveService.updateBread(breadSave, file);
    }

    @ApiOperation("빵 상태 변경")
    @PutMapping("/id/{id}/state/{state}")
    public void changeBreadState(@PathVariable Long id, @PathVariable BreadState state){
        breadSaveService.updateBreadState(id, state);
    }

    @ApiOperation("빵 매진 상태 변경")
    @PutMapping("/id/{id}/sold_out/{isSoldOut}")
    public void changeBreadSoldOut(@PathVariable Long id, @PathVariable boolean isSoldOut){
        breadSaveService.updateBreadSoldOut(id, isSoldOut);
    }

    @ApiOperation("빵 삭제")
    @DeleteMapping("/id/{id}/delete/{delete}")
    public void deleteBread(@PathVariable Long id, @PathVariable boolean delete){
        breadSaveService.deleteBread(id, delete);
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