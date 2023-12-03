package org.example;

public class SymbolLookupResult {
    SymbolLookupResult(Boolean matches,
                       Integer x,
                       Integer y)
    {
        SymbolMatches = matches;
        SymbolLocation = String.format("%d_%d", x, y);
    }
    public Boolean SymbolMatches;
    public String SymbolLocation;
}
