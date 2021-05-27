package graphqlquery;

import java.util.HashMap;

public class Argument extends HashMap<String, Object>
{
    @Override
    public Argument put(final String key, final Object value)
    {
        super.put(key, value);
        return this;
    }
}
