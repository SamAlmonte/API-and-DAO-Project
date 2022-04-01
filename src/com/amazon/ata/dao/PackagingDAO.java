package com.amazon.ata.dao;

import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;

import java.util.*;

/**
 * Access data for which packaging is available at which fulfillment center.
 */
public class PackagingDAO {
    /**
     * A list of fulfillment centers with a packaging options they provide.
     */
    private List<FcPackagingOption> fcPackagingOptions;
    private Map<FulfillmentCenter, Set<FcPackagingOption>> fcsWithPackageOptionsMap;

    /**
     * Instantiates a PackagingDAO object.
     * @param datastore Where to pull the data from for fulfillment center/packaging available mappings.
     */
    public PackagingDAO(PackagingDatastore datastore) {
        this.fcPackagingOptions =  new ArrayList<>(datastore.getFcPackagingOptions());
        this.fcsWithPackageOptionsMap = new HashMap<>();
        for(FcPackagingOption fc : fcPackagingOptions){
            if(!this.fcsWithPackageOptionsMap.containsKey(fc.getFulfillmentCenter())){
                Set<FcPackagingOption> aSet = new HashSet<>();
                aSet.add(fc);
                this.fcsWithPackageOptionsMap.put(fc.getFulfillmentCenter(), aSet);
            }
            //bracket conditions, contains key, constains box material
            else if (this.fcsWithPackageOptionsMap.containsKey(fc.getFulfillmentCenter()) && fc.getPackaging().getMaterial().equals(Material.CORRUGATE)){
                //if the key is in the map
                Box aBox = (Box) fc.getPackaging();
                //iterate the set that this key belongs to, if boxes is not found add it to the hashSet?
                Set<Box> boxes = new HashSet<>();
                Set<FcPackagingOption> aSet = fcsWithPackageOptionsMap.get(fc.getFulfillmentCenter());
                for(FcPackagingOption ith : aSet){
                    if(ith.getPackaging().getMaterial().equals(Material.CORRUGATE))
                        boxes.add((Box) ith.getPackaging());
                }
                if(!boxes.contains(aBox))
                    aSet.add(fc);
            }
            else if ((this.fcsWithPackageOptionsMap.containsKey(fc.getFulfillmentCenter()) && fc.getPackaging().getMaterial().equals(Material.LAMINATED_PLASTIC))){
                //this bracket will differentiate unique polyBags
                //if the key is in the map
                PolyBag aBox = (PolyBag) fc.getPackaging(); //We will test to see if this package is unique
                //iterate the set that this key belongs to, if package is unique, add it to hashSet
                Set<PolyBag> boxes = new HashSet<>();
                Set<FcPackagingOption> aSet = fcsWithPackageOptionsMap.get(fc.getFulfillmentCenter());
                //get all the packaging associated with this fulfilment center
                for(FcPackagingOption ith : aSet){
                    if(ith.getPackaging().getMaterial().equals(Material.LAMINATED_PLASTIC))
                        boxes.add((PolyBag) ith.getPackaging());
                }
                //if packaging is unique add it to the set
                if(!boxes.contains(aBox))
                    aSet.add(fc);
            }
        }
    }

    /**
     * Returns the packaging options available for a given item at the specified fulfillment center. The API
     * used to call this method handles null inputs, so we don't have to.
     *
     * @param item the item to pack
     * @param fulfillmentCenter fulfillment center to fulfill the order from
     * @return the shipping options available for that item; this can never be empty, because if there is no
     * acceptable option an exception will be thrown
     * @throws UnknownFulfillmentCenterException if the fulfillmentCenter is not in the fcPackagingOptions list
     * @throws NoPackagingFitsItemException if the item doesn't fit in any packaging at the FC
     */
    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        // Check all FcPackagingOptions for a suitable Packaging in the given FulfillmentCenter
        List<ShipmentOption> result = new ArrayList<>();
        boolean fcFound = false;
        if(fcsWithPackageOptionsMap.get(fulfillmentCenter) == null)
            throw new UnknownFulfillmentCenterException("Unable to find FC object with given code");
        for (FcPackagingOption fcPackagingOption : fcsWithPackageOptionsMap.get(fulfillmentCenter)) {
            Packaging packaging = fcPackagingOption.getPackaging();
            String fcCode = fcPackagingOption.getFulfillmentCenter().getFcCode();

            if (fcCode.equals(fulfillmentCenter.getFcCode())) {
                fcFound = true;
                if (packaging.canFitItem(item)) {
                    result.add(ShipmentOption.builder()
                            .withItem(item)
                            .withPackaging(packaging)
                            .withFulfillmentCenter(fulfillmentCenter)
                            .build());
                }
            }
        }

        // Notify caller about unexpected results
        if (!fcFound) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }

        if (result.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
        }

        return result;
    }



/*    public List<ShipmentOption> findShipmentOptions(Item item, FulfillmentCenter fulfillmentCenter)
            throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {

        // Check all FcPackagingOptions for a suitable Packaging in the given FulfillmentCenter
        List<ShipmentOption> result = new ArrayList<>();
        boolean fcFound = false;
        for (FcPackagingOption fcPackagingOption : fcPackagingOptions) {
            Packaging packaging = fcPackagingOption.getPackaging();
            String fcCode = fcPackagingOption.getFulfillmentCenter().getFcCode();

            if (fcCode.equals(fulfillmentCenter.getFcCode())) {
                fcFound = true;
                if (packaging.canFitItem(item)) {
                    result.add(ShipmentOption.builder()
                            .withItem(item)
                            .withPackaging(packaging)
                            .withFulfillmentCenter(fulfillmentCenter)
                            .build());
                }
            }
        }

        // Notify caller about unexpected results
        if (!fcFound) {
            throw new UnknownFulfillmentCenterException(
                    String.format("Unknown FC: %s!", fulfillmentCenter.getFcCode()));
        }

        if (result.isEmpty()) {
            throw new NoPackagingFitsItemException(
                    String.format("No packaging at %s fits %s!", fulfillmentCenter.getFcCode(), item));
        }

        return result;
    }

 */


} //end Class
