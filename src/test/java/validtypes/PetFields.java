package validtypes;

public class PetFields
{
    private boolean color;
    private boolean name;
    private ToyFields toyFields;

    public PetFields()
    {
        toyFields = new ToyFields();
    }

    public PetFields selectColor()
    {
        color = true;
        return this;
    }

    public PetFields selectName()
    {
        name = true;
        return this;
    }

    public PetFields selectToy()
    {
        toyFields.selectFormat();
        return this;
    }
}
