package annotedtypes;

import fieldalias.GraphQLField;

public class CountryFieldsWithAnnotation
{
    @GraphQLField("name")
    private boolean nameField;
    @GraphQLField("pib")
    private boolean pibField;
    @GraphQLField("uf")
    private UfFieldsWithAnnotation ufFields;
    private PresidentFieldsWithAnnotation presidentFields;

    public CountryFieldsWithAnnotation()
    {
        ufFields = new UfFieldsWithAnnotation();
        presidentFields = new PresidentFieldsWithAnnotation();
    }

    public CountryFieldsWithAnnotation selectName()
    {
        nameField = true;
        return this;
    }

    public CountryFieldsWithAnnotation selectPib()
    {
        pibField = true;
        return this;
    }

    public CountryFieldsWithAnnotation unselectPib()
    {
        pibField = false;
        return this;
    }

    public CountryFieldsWithAnnotation selectUfFrom(final UfFieldsWithAnnotation ufFields)
    {
        this.ufFields = ufFields;
        return this;
    }

    public CountryFieldsWithAnnotation selectUf()
    {
        ufFields.selectName();
        ufFields.selectOfficialName();
        ufFields.selectCities();

        return this;
    }

    public CountryFieldsWithAnnotation selectPresidentFrom(final PresidentFieldsWithAnnotation presidentFields)
    {
        this.presidentFields = presidentFields;
        return this;
    }

    public CountryFieldsWithAnnotation selectPresident()
    {
        presidentFields.selectAge();
        presidentFields.selectTerm();
        presidentFields.selectProfile();
        presidentFields.selectPet();

        return this;
    }

    public CountryFieldsWithAnnotation selectAllFields()
    {
        selectName();
        selectPib();
        selectUf();
        selectPresident();

        return this;
    }

    public CountryFieldsWithAnnotation selectAllFieldsExceptPib()
    {
        selectName();
        unselectPib();
        selectUf();
        selectPresident();
        return this;
    }
}