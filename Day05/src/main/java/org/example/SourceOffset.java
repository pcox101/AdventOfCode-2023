package org.example;

public class SourceOffset {
    public Long SourceStart;
    public Long DestinationStart;
    public Long Length;

    public Long getDestinationValue(Long sourceValue) {
        //System.out.println(String.format("Source %d looking at start %d and length %d", sourceValue, SourceStart, Length));
        Long offset = sourceValue - SourceStart;
        if ((offset > -1) && (offset < Length)) {
            return DestinationStart + offset;
        }
        return null;
    }
}

