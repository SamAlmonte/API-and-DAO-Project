package com.amazon.ata.cost;
import com.amazon.ata.types.ShipmentCost;
import com.amazon.ata.types.ShipmentOption;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WeightedCostStrategy implements CostStrategy {
    CarbonCostStrategy carbonCostStrategy;
    MonetaryCostStrategy monetaryCostStrategy;
    private Map<CostStrategy, BigDecimal> weighted_cost;

    public WeightedCostStrategy(MonetaryCostStrategy monetaryCostStrategy, CarbonCostStrategy carbonCostStrategy) {
        this.carbonCostStrategy = carbonCostStrategy;
        this.monetaryCostStrategy = monetaryCostStrategy;
        weighted_cost = new HashMap<>();
        weighted_cost.put(monetaryCostStrategy, new BigDecimal("0.80"));
        weighted_cost.put(carbonCostStrategy, new BigDecimal("0.20"));
    }
    /*public WeightedCostStrategy(Map<CostStrategy, BigDecimal> weighted_cost){
        this.weighted_cost = weighted_cost;
    }*/

    public ShipmentCost getCost(ShipmentOption shipmentOption) {
        //carbonCostStrategy.
        BigDecimal monetaryCost = monetaryCostStrategy.getCost(shipmentOption).getCost().multiply(BigDecimal.valueOf(0.80));
        BigDecimal carbonCost = carbonCostStrategy.getCost(shipmentOption).getCost().multiply(BigDecimal.valueOf(0.20));

        return new ShipmentCost(shipmentOption, carbonCost.add(monetaryCost));
    }
}
