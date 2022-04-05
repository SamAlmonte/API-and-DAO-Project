package com.amazon.ata.cost;

import com.amazon.ata.types.Material;
import com.amazon.ata.types.Packaging;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CarbonCostStrategy implements CostStrategy {
    private final Map<Material, BigDecimal> carbonUnitsCostPerGram;

    public CarbonCostStrategy() {
        carbonUnitsCostPerGram = new HashMap<>();
        carbonUnitsCostPerGram.put(Material.CORRUGATE, BigDecimal.valueOf(0.017));
        carbonUnitsCostPerGram.put(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(0.012));
    }


    @Override
    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        Packaging packaging = shipmentOption.getPackaging();
        BigDecimal grams = packaging.getMass();
        return new ShipmentCost(shipmentOption, grams.multiply(carbonUnitsCostPerGram.get(packaging.getMaterial())));
    }
}
