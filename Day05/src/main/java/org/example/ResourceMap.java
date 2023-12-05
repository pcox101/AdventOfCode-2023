package org.example;

import java.util.ArrayList;
import java.util.List;

public class ResourceMap {
    public String Source;
    public String Destination;
    public List<SourceOffset> SourceOffsets;
    ResourceMap(String source, String destination)
    {
        Source = source;
        Destination = destination;
        SourceOffsets = new ArrayList<SourceOffset>();
    }

    public void addResourceLine(String line) {
        String[] resourceString = line.split(" ");
        SourceOffset so = new SourceOffset();
        so.DestinationStart = Long.parseLong(resourceString[0]);
        so.SourceStart = Long.parseLong(resourceString[1]);
        so.Length = Long.parseLong(resourceString[2]);
        SourceOffsets.add(so);
    }
}
