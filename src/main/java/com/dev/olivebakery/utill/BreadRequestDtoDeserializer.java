package com.dev.olivebakery.utill;

import com.dev.olivebakery.domain.dtos.bread.BreadRequestDto;
import com.dev.olivebakery.domain.dtos.bread.IngredientListResponseDto;
import com.dev.olivebakery.domain.enums.BreadState;
import com.dev.olivebakery.domain.enums.DayType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.List;

public class BreadRequestDtoDeserializer extends StdDeserializer<BreadRequestDto> {

  public BreadRequestDtoDeserializer() {
    this(null);
  }

  protected BreadRequestDtoDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public BreadRequestDto deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    ObjectCodec codec = jsonParser.getCodec();
    JsonNode node = codec.readTree(jsonParser);

    List<DayType> dayTypeList = mapper
        .convertValue(node.get("days"), new TypeReference<List<DayType>>() {
        });
    BreadState breadState = mapper.convertValue(node.get("state"), new TypeReference<BreadState>() {
    });
    List<IngredientListResponseDto> ingredients = mapper.convertValue(node.get("ingredientsList"),
        new TypeReference<List<IngredientListResponseDto>>() {
        });

    return BreadRequestDto.builder()
        .name(node.get("name").asText())
        .price(node.get("price").asInt())
        .description(node.get("description").asText())
        .detailDescription(node.get("detailDescription").asText())
        .days(dayTypeList)
        .state(breadState)
        .ingredientsList(ingredients)
        .build()
        ;
  }
}
