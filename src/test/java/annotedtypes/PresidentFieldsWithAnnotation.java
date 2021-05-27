package annotedtypes;

import fieldalias.GraphQLField;

@GraphQLField("president")
public class PresidentFieldsWithAnnotation
{
    @GraphQLField("age")
    private boolean ageField;
    @GraphQLField("term")
    private boolean termField;
    @GraphQLField("profile")
    private boolean profileField;
    private PetFieldsWithAnnotation petFields;

    public PresidentFieldsWithAnnotation()
    {
        petFields = new PetFieldsWithAnnotation();
    }

    public PresidentFieldsWithAnnotation selectAge()
    {
        ageField = true;
        return this;
    }

    public boolean selectTerm()
    {
        termField = true;
        return true;
    }

    public PresidentFieldsWithAnnotation selectProfile()
    {
        profileField = true;
        return this;
    }

    public PresidentFieldsWithAnnotation selectPet()
    {
        petFields.selectColor();
        petFields.selectName();
        return this;
    }

    public PresidentFieldsWithAnnotation selectPetFrom(final PetFieldsWithAnnotation petFields)
    {
        this.petFields = petFields;
        return this;
    }
}
