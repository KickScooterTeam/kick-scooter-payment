package com.softserve.paymentservice.converter;

import com.softserve.paymentservice.dto.CardDto;
import com.softserve.paymentservice.model.Card;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CardDtoToModel implements Converter<CardDto, Card> {
    @Override
    public Card convert(CardDto cardDTO) {
        Card card = new Card();
        card.setCardNumber(cardDTO.getCardNumber());
        card.setExpMonth(cardDTO.getExpMonth());
        card.setExpYear(cardDTO.getExpYear());
        card.setUserId(cardDTO.getUserUUID());
        card.setWorking(false);
        return card;
    }
}
