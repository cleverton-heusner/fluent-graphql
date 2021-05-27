package invalidtypes;

public class UfWithInvalidTypesFields
{
    private boolean nomeUf;
    private Integer officialName;
    private Float mainCities;

    public UfWithInvalidTypesFields selectName()
    {
        nomeUf = true;
        return this;
    }

    public UfWithInvalidTypesFields selectOfficialName()
    {
        officialName = 0;
        return this;
    }

    public UfWithInvalidTypesFields selectCities()
    {
        mainCities = 0f;
        return this;
    }
}
