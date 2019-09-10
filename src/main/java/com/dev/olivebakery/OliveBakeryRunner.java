package com.dev.olivebakery;

import com.dev.olivebakery.domain.entity.Bread;
import com.dev.olivebakery.domain.entity.Ingredients;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.repository.BreadRepository;
import com.dev.olivebakery.repository.IngredientsRepository;
import com.dev.olivebakery.repository.MemberRepository;
import com.dev.olivebakery.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OliveBakeryRunner implements ApplicationRunner {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BreadRepository breadRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    IngredientsRepository ingredientsRepository;
    @Autowired
    PasswordEncoder encoder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
//        List<Member> members = setMemberList();
        List<Bread> breads = setBreadList();

//        memberRepository.saveAll(members);
//        breadRepository.saveAll(breads);
    }

    private List<Member> setMemberList(){
        List<Member> members = new ArrayList<>();
        for(int i=0 ; i < 10 ; i++){
            members.add(
              Member.builder()
                      .name("사용자" + i)
                      .id("user" + i)
                      .age(20)
                      .pw(encoder.encode("1234"))
                      .phoneNumber("010-1234-1234")
                      .stamp(0)
                      .isMale(true)
                      .build()
            );
        }
        return members;
    }

    private List<Bread> setBreadList(){
        List<Bread> breads = new ArrayList<>();
        List<Ingredients> ingredients = ingredientsRepository.findAll();

        breads.add(
          Bread.builder()
                  .name("올리브 치아바타")
                  .price(3000)
                  .description("올리브와 큐브치즈를 넣은 빵입니다..")
                  .detailDescription("밀가루, 효모, 물, 소금을 사용해 만든 이탈리아 빵으로, 샌드위치용 빵으로서 바게트를 대체할 목적으로 1982년 베네토(Veneto) 주 아드리아(Adria)의 아르날도 카발라리(Arnaldo Cavallari)가 개발한 빵이다.")
                  .state(BreadState.NORMAL)
                  .ingredientsList(ingredients)
                  .isSoldOut(false)
                  .deleteFlag(false)
                  .build()
        );
        breads.add(
                Bread.builder()
                        .name("모닝빵")
                        .price(3900)
                        .description("주먹만한 모닝빵 9개가 들어간 빵입니다..")
                        .detailDescription("영미권에서는 보통 Dinner Rolls라고 부르며 식사용으로도 자주 먹기 때문에 일반적인 유럽권의 주식용 빵과는 달리 고율배합이다(우유 및 유지의 비중이 높다). 대한민국의 경우 간단한 아침식사나 간식으로 즐겨 먹는 경우가 많아서 모닝빵이라는 이름이 붙었다.")
                        .state(BreadState.NORMAL)
                        .ingredientsList(ingredients)
                        .isSoldOut(false)
                        .deleteFlag(false)
                        .build()
        );
        breads.add(
                Bread.builder()
                        .name("베이글")
                        .price(3000)
                        .description("베이글 빵입니다.")
                        .detailDescription("밀가루 반죽을 끓는 물에 데치고 굽는 빵의 일종으로, 유대인들이 빵을 숙성할 시간이 없어서 반죽을 데친 것에서 유래했다. 실제 어원이 이디시어의 'בײגל(Beygl)'이다.")
                        .state(BreadState.NORMAL)
                        .ingredientsList(ingredients)
                        .isSoldOut(false)
                        .deleteFlag(false)
                        .build()
        );

        return breads;
    }
}
