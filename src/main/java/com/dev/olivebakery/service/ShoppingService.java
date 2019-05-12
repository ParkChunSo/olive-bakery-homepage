package com.dev.olivebakery.service;

import org.springframework.stereotype.Service;

@Service
public class ShoppingService {

/*    public List<BreadDto> getBreadByDay(String day){
        List<Bread> breads = daysRepository.findBread(day);

        List<BreadDto> breadDtos = new ArrayList<>();
        for (Bread bread : breads){
            BreadDto breadDto = BreadDto.builder()
                    .name(bread.getName())
                    .description(bread.getDescription())
                    .picturePath(bread.getPicturePath())
                    .price(bread.getPrice())
                    .star(bread.getStar()).of();
            breadDtos.add(breadDto);
        }
        return breadDtos;
    }

    public List<ReviewDto> getReview(String breadName){
        Bread bread = breadRepository.findByName(breadName).orElseThrow(() -> new UserDefineException("해당 빵은 없습니다."));

        List<Review> reviews = reviewRepository.findByBread(bread);

        List<ReviewDto> reviewDtos = new ArrayList<ReviewDto>();

        for (Review review : reviews){
            ReviewDto reviewDto = ReviewDto.builder()
                    .userName(review.getMember().getName())
                    .content(review.getContent())
                    .date(review.getDate()).of();

            reviewDtos.add(reviewDto);
        }

        return reviewDtos;
    }*/
}