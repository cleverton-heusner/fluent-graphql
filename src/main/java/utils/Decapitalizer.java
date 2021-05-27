package utils;

import java.beans.Introspector;

public class Decapitalizer
{
    public String decapitalize(final String field)
    {
        return Introspector.decapitalize(field);
    }
}
