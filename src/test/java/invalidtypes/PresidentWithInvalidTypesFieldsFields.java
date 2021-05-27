package invalidtypes;

public class PresidentWithInvalidTypesFieldsFields
{
    private boolean age;
    private Long term;
    private Byte profile;
    private PetWithInvalidTypesFields petFields;

    public PresidentWithInvalidTypesFieldsFields()
    {
        petFields = new PetWithInvalidTypesFields();
    }

    public PresidentWithInvalidTypesFieldsFields selectAge()
    {
        age = true;
        return this;
    }

    public PresidentWithInvalidTypesFieldsFields selectTerm()
    {
        term = 0l;
        return this;
    }

    public PresidentWithInvalidTypesFieldsFields selectProfile()
    {
        profile = 0;
        return this;
    }

    public PresidentWithInvalidTypesFieldsFields selectPet()
    {
        petFields.selectColor();
        petFields.selectName();
        return this;
    }

    public PresidentWithInvalidTypesFieldsFields selectPetFrom(final PetWithInvalidTypesFields petFields)
    {
        this.petFields = petFields;
        return this;
    }
}