package com.example.mctg.serializer;

import lombok.Getter;

@Getter
public class TradeData {
    private String id;
    private String cardToTrade;
    private String type;
    private float minimumDamage;
}
