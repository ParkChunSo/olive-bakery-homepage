package com.dev.olivebakery.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class BreadImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long breadImageId;

    @NotNull
    private String imageName;

    @NotNull
    private String imageType;

    @NotNull
    private String imagePath;

    @NotNull
    private Long imageSize;

    @NotNull
    private String imageUrl;

    private Boolean current ;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bread_id")
    private Bread bread;

    public void changeCurrentStatus(Boolean status){
        this.current = status;
    }
}
