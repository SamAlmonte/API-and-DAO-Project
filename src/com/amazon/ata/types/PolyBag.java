package com.amazon.ata.types;

import java.math.BigDecimal;
import java.util.Objects;

public class PolyBag extends Packaging{
    BigDecimal volume;
    public PolyBag(Material material, BigDecimal volume) {
        super(material);
        this.volume = volume;
    }
    public BigDecimal getMass() {
        double theVolume = volume.doubleValue();
        return BigDecimal.valueOf(Math.ceil(Math.sqrt(theVolume) * 0.6));
    }

    /**
     * Returns whether the given item will fit in this packaging.
     *
     * @param item the item to test fit for
     * @return whether the item will fit in this packaging
     */
    public boolean canFitItem(Item item) {
        //a Poly bag can fit any item that has less volume than the bag
        BigDecimal itemVolume = item.getHeight().multiply(item.getLength()).multiply(item.getWidth());
        return volume.compareTo(itemVolume) > 0;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    @Override
    public boolean equals(Object o) {
        // Can't be equal to null
        if (o == null) {
            return false;
        }

        // Referentially equal
        if (this == o) {
            return true;
        }

        // Check if it's a different type
        if (getClass() != o.getClass()) {
            return false;
        }

        PolyBag packaging = (PolyBag) o;
        return super.equals(packaging) && this.volume.equals(packaging.volume);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        return hash + Objects.hash(getVolume());
    }


}
