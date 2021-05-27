package annotedtypes;

import fieldalias.GraphQLField;

@GraphQLField("pet")
public class PetFieldsWithAnnotation
{
    @GraphQLField("color")
    private boolean colorField;
    @GraphQLField("name")
    private boolean nameField;
    private ToyFieldsWithAnnotation toyFields;

    public PetFieldsWithAnnotation()
    {
        toyFields = new ToyFieldsWithAnnotation();
    }

    public PetFieldsWithAnnotation selectColor()
    {
        colorField = true;
        return this;
    }

    public PetFieldsWithAnnotation selectName()
    {
        nameField = true;
        return this;
    }

    public PetFieldsWithAnnotation selectToy()
    {
        toyFields.selectFormat();
        return this;
    }
}
