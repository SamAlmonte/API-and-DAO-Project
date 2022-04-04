package com.amazon.ata.service;

import com.amazon.ata.cost.CarbonCostStrategy;
import com.amazon.ata.cost.CostStrategy;
import com.amazon.ata.cost.MonetaryCostStrategy;
import com.amazon.ata.cost.WeightedCostStrategy;
import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.FulfillmentCenter;
import com.amazon.ata.types.Item;
import com.amazon.ata.types.ShipmentOption;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentServiceTest {

    private Item smallItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1))
            .withWidth(BigDecimal.valueOf(1))
            .withLength(BigDecimal.valueOf(1))
            .withAsin("abcde")
            .build();

    private Item largeItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1000))
            .withWidth(BigDecimal.valueOf(1000))
            .withLength(BigDecimal.valueOf(1000))
            .withAsin("12345")
            .build();

    private FulfillmentCenter existentFC = new FulfillmentCenter("ABE2");
    private FulfillmentCenter nonExistentFC = new FulfillmentCenter("NonExistentFC");

    //private ShipmentService shipmentService = new ShipmentService(new PackagingDAO(new PackagingDatastore()),new MonetaryCostStrategy());

    @InjectMocks
    private ShipmentService shipmentService1;

    @Mock
    private CostStrategy costStrategy;

    @Mock
    private PackagingDAO packagingDAO;

    @Test
    void findBestShipmentOption_existentFCAndItemCanFit_returnsShipmentOption() {
        // GIVEN & WHEN
        PackagingDAO packagingDAO1 = new PackagingDAO(new PackagingDatastore());
        ShipmentService shipmentService = new ShipmentService(packagingDAO1, new WeightedCostStrategy(new MonetaryCostStrategy(), new CarbonCostStrategy()));
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, existentFC);

        // THEN
        assertNotNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_existentFCAndItemCannotFit_returnsShipmentOption() {
        // GIVEN & WHEN
        //ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, existentFC);
        PackagingDAO packagingDAO1 = new PackagingDAO(new PackagingDatastore());
        ShipmentService shipmentService = new ShipmentService(packagingDAO1, new WeightedCostStrategy(new MonetaryCostStrategy(), new CarbonCostStrategy()));
        ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, existentFC);
        // THEN
        assertNotNull(shipmentOption);
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCanFit_returnsShipmentOption() {
        // GIVEN & WHEN
        //ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, nonExistentFC);
        PackagingDAO packagingDAO1 = new PackagingDAO(new PackagingDatastore());
        ShipmentService shipmentService = new ShipmentService(packagingDAO1, new WeightedCostStrategy(new MonetaryCostStrategy(), new CarbonCostStrategy()));
        //ShipmentOption shipmentOption = shipmentService.findShipmentOption(smallItem, nonExistentFC);

        // THEN
        assertThrows(RuntimeException.class,
                () -> shipmentService.findShipmentOption(smallItem, nonExistentFC));
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCannotFitThrows_UnknownFulfillmentCenterException() {
        // GIVEN & WHEN
        //ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, nonExistentFC);
        PackagingDAO packagingDAO1 = new PackagingDAO(new PackagingDatastore());
        ShipmentService shipmentService = new ShipmentService(packagingDAO1, new WeightedCostStrategy(new MonetaryCostStrategy(), new CarbonCostStrategy()));
        //ShipmentOption shipmentOption = shipmentService.findShipmentOption(largeItem, nonExistentFC);

        // THEN
        //assertNotNull(shipmentOption);
        assertThrows(RuntimeException.class,
                () -> shipmentService.findShipmentOption(largeItem, nonExistentFC));
    }
}