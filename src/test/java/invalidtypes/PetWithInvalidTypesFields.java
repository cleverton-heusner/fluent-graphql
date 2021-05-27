package invalidtypes;

public class PetWithInvalidTypesFields
{
    private Short color;
    private boolean name;

    public PetWithInvalidTypesFields selectColor(){
        color = 0;
        return this;
    }

    public PetWithInvalidTypesFields selectName(){
        name = true;
        return this;
    }
}
