package invalidtypes;

public class CountryWithInvalidTypesFields
{
    private boolean name;
    private Double population;
    private String pib;
    private UfWithInvalidTypesFields ufFields;
    private PresidentWithInvalidTypesFieldsFields presidentFields;

    public CountryWithInvalidTypesFields()
    {
        ufFields = new UfWithInvalidTypesFields();
        presidentFields = new PresidentWithInvalidTypesFieldsFields();
    }

    public CountryWithInvalidTypesFields selectName()
    {
        name = true;
        return this;
    }

    public CountryWithInvalidTypesFields selectPopulation()
    {
        population = 0d;
        return this;
    }

    public CountryWithInvalidTypesFields selectPib()
    {
        pib = "true";
        return this;
    }

    public CountryWithInvalidTypesFields selectUf()
    {
        ufFields.selectName();
        ufFields.selectOfficialName();
        ufFields.selectCities();

        return this;
    }

    public CountryWithInvalidTypesFields selectUfFrom(final UfWithInvalidTypesFields ufFields)
    {
        this.ufFields = ufFields;
        return this;
    }

    public CountryWithInvalidTypesFields selectPresident()
    {
        presidentFields.selectAge();
        presidentFields.selectTerm();
        presidentFields.selectProfile();
        presidentFields.selectPet();

        return this;
    }

    public CountryWithInvalidTypesFields selectPresidentFrom(
            final PresidentWithInvalidTypesFieldsFields presidentFields)
    {
        this.presidentFields = presidentFields;
        return this;
    }
}