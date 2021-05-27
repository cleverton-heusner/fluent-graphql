package validtypes;

public class UfFields
{
    private boolean nomeUf;
    private boolean officialName;
    private boolean mainCities;

    public UfFields selectName()
    {
        nomeUf = true;
        return this;
    }

    public UfFields selectOfficialName()
    {
        officialName = true;
        return this;
    }

    public UfFields selectCities()
    {
        mainCities = true;
        return this;
    }
}
