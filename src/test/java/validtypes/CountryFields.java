package validtypes;

public class CountryFields<Uf>
{
    private boolean name;
    private boolean pib;

    private UfFields uf;
    private PresidentFields president;

    public CountryFields()
    {
        uf = new UfFields();
        president = new PresidentFields();
    }

    public CountryFields selectName()
    {
        name = true;
        return this;
    }

    public CountryFields selectPib()
    {
        pib = true;
        return this;
    }

    public CountryFields unselectPib()
    {
        pib = false;
        return this;
    }

    public CountryFields selectUfFrom(final UfFields ufFields)
    {
        this.uf = ufFields;
        return this;
    }

    public CountryFields selectUf()
    {
        uf.selectName();
        uf.selectOfficialName();
        uf.selectCities();

        return this;
    }

    public CountryFields selectPresidentFrom(final PresidentFields presidentFields)
    {
        this.president = presidentFields;
        return this;
    }

    public CountryFields selectPresident()
    {
        president.selectAge();
        president.selectTerm();
        president.selectProfile();
        president.selectPet();

        return this;
    }

    public CountryFields selectAllFields()
    {
        selectName();
        selectPib();
        selectUf();
        selectPresident();

        return this;
    }

    public CountryFields selectAllFieldsExceptPib()
    {
        selectName();
        unselectPib();
        selectUf();
        selectPresident();
        return this;
    }
}