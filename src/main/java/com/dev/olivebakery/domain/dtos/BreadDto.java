package com.dev.olivebakery.domain.dtos;


import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.domain.enums.DayType;
import lombok.*;


import java.util.List;

@Data
@Builder
public class BreadDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadIngredient{
        private String name;
        private String origin;
        private Boolean exist;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadImageDto{
        private String name;
        private String encoded;
        private String imageUrl;
        private String contentType;
        private Long volume;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadGetAll {
        private String name;
        private int price;
        private String description;
        private Boolean isSoldOut;
        private BreadState breadState;
        private BreadImageDto breadImage;
        private List<BreadIngredient> breadIngredientList;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
        public static class BreadGetDetail{
            private String name;
            private int price;
            private String detailDescription;
            private String description;
            private List<BreadIngredient> ingredientsList;
            private Boolean isSoldOut;
            private BreadState breadState;
            private List<DayType> daysList;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadSave{
        private String name;
        private int price;
        private String description;
        private String detailDescription;
        private List<BreadIngredient> ingredientsList;
        private List<DayType> dayTypes;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadUpdateName {
        private String oldName;
        private String newName;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadUpdate {
        private String oldName;
        private String newName;
        private int price;
        private String description;
        private String detailDescription;
        private List<BreadIngredient> ingredientsList;
        private List<DayType> dayTypes;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadUpdateState {
        private String name;
        private BreadState breadState;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadUpdateSoldOut {
        private String name;
        private Boolean isSoldOut;
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class BreadUpdateIngredients {
        private String name;
        private List<BreadIngredient> ingredientsList;


    }

}
