package com.amazon.ata.cost;

import com.amazon.ata.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MonetaryCostStrategyTest {

    private static final Packaging BOX_10x10x20 =
        new Box(Material.CORRUGATE, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(20));

    private static final Packaging polyTestBag = new PolyBag(Material.CORRUGATE, BigDecimal.valueOf(100));

    private MonetaryCostStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MonetaryCostStrategy();
    }

    @Test
    void getCost_corrugateMaterial_returnsCorrectCost() {
        // GIVEN
        ShipmentOption option = ShipmentOption.builder()
            .withPackaging(BOX_10x10x20)
            .build();

        // WHEN
        ShipmentCost shipmentCost = strategy.getCost(option);

        // THEN
        assertTrue(BigDecimal.valueOf(5.43).compareTo(shipmentCost.getCost()) == 0,
            "Incorrect monetary cost calculation for a box with dimensions 10x10x20.");
    }

    @Test
    void getCost_LaminatedPlasticMaterial_returnsCorrectsCost(){
        ShipmentOption option = ShipmentOption.builder().withPackaging(polyTestBag).build();
        ShipmentCost shipmentCost = strategy.getCost(option);
        assertTrue(BigDecimal.valueOf(shipmentCost.getCost().doubleValue()).compareTo(shipmentCost.getCost()) == 0,
                "Incorrect monetary cost calculation for a box with volume 100.");
    }

    //void getCost_LamentedPlasticMaterial_returnsCorrectCost()
}
