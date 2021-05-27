package annotedtypes;

import fieldalias.GraphQLField;

@GraphQLField("toy")
public class ToyFieldsWithAnnotation
{
    @GraphQLField("format")
    private boolean formatField;

    public ToyFieldsWithAnnotation selectFormat()
    {
        formatField = true;
        return this;
    }
}
