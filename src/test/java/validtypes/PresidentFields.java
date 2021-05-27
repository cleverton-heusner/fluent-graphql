package validtypes;

public class PresidentFields
{
    private boolean age;
    private boolean term;
    private boolean profile;
    private PetFields pet;

    public PresidentFields()
    {
        pet = new PetFields();
    }

    public PresidentFields selectAge()
    {
        age = true;
        return this;
    }

    public boolean selectTerm()
    {
        term = true;
        return true;
    }

    public PresidentFields selectProfile()
    {
        profile = true;
        return this;
    }

    public PresidentFields selectPet()
    {
        pet.selectColor();
        pet.selectName();
        return this;
    }

    public PresidentFields selectPetFrom(final PetFields petFields)
    {
        this.pet = petFields;
        return this;
    }
}
