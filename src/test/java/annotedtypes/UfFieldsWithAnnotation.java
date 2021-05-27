package annotedtypes;

import fieldalias.GraphQLField;

@GraphQLField("uf")
public class UfFieldsWithAnnotation
{
    @GraphQLField("name")
    private boolean nameField;
    @GraphQLField("officialName")    private boolean officialNameField;

    @GraphQLField("mainCities")
    private boolean mainCitiesField;

    public UfFieldsWithAnnotation selectName()
    {
        nameField = true;
        return this;
    }

    public UfFieldsWithAnnotation selectOfficialName()
    {
        officialNameField = true;
        return this;
    }

    public UfFieldsWithAnnotation selectCities()
    {
        mainCitiesField = true;
        return this;
    }
}
