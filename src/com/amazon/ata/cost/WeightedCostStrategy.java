package com.amazon.ata.cost;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WeightedCostStrategy implements CostStrategy {
    CarbonCostStrategy carbonCostStrategy;
    MonetaryCostStrategy monetaryCostStrategy;

    public WeightedCostStrategy(MonetaryCostStrategy monetaryCostStrategy, CarbonCostStrategy carbonCostStrategy) {
        this.carbonCostStrategy = carbonCostStrategy;
        this.monetaryCostStrategy = monetaryCostStrategy;
;
    }

    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        BigDecimal monetaryCost = monetaryCostStrategy.getCost(shipmentOption).getCost().multiply(BigDecimal.valueOf(0.80));
        BigDecimal carbonCost = carbonCostStrategy.getCost(shipmentOption).getCost().multiply(BigDecimal.valueOf(0.20));

        return new ShipmentCost(shipmentOption, carbonCost.add(monetaryCost));
    }
}
