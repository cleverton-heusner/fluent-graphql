import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import annotedtypes.CountryFieldsWithAnnotation;
import annotedtypes.PetFieldsWithAnnotation;
import annotedtypes.PresidentFieldsWithAnnotation;
import annotedtypes.UfFieldsWithAnnotation;
import graphqlquery.Argument;
import graphqlquery.GraphqlQuery;
import org.junit.Test;

public class FieldWithAliasTest
{
    @Test
    public void When_One_Field_Selected_Then_Query_With_One_Field()
    {
        final CountryFieldsWithAnnotation countryFields = new CountryFieldsWithAnnotation().selectName();
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName")
                                                     .withArguments(arguments)
                                                     .withFields(countryFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name } }\"}")));
    }

    @Test
    public void When_More_Than_One_Field_Selected_Then_Query_With_More_Than_One_Field()
    {
        final CountryFieldsWithAnnotation countryFields = new CountryFieldsWithAnnotation().selectName()
                                                               .selectPib();
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name pib } }\"}")));
    }

    @Test
    public void When_One_Field_With_One_Child_Selected_Then_Query_With_One_Field_With_One_Child()
    {
        final UfFieldsWithAnnotation ufFields = new UfFieldsWithAnnotation().selectOfficialName();
        final CountryFieldsWithAnnotation countryFields = new CountryFieldsWithAnnotation().selectUfFrom(ufFields);

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryFields)
                                                     .create();
        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ uf { officialName } } }\"}")));
    }

    @Test
    public void When_One_Field_With_More_Than_One_Child_Selected_Then_Query_With_One_Field_With_More_Than_One_Child()
    {
        final UfFieldsWithAnnotation ufFields = new UfFieldsWithAnnotation().selectOfficialName().selectCities();
        final CountryFieldsWithAnnotation countryFields = new CountryFieldsWithAnnotation().selectUfFrom(ufFields);

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ uf { officialName mainCities } } }\"}")));
    }

    @Test
    public void When_One_Field_With_One_Descendant_Selected_Then_Query_With_One_Field_With_One_Descendant()
    {
        final PetFieldsWithAnnotation petFields = new PetFieldsWithAnnotation().selectToy();
        final PresidentFieldsWithAnnotation presidentFields = new PresidentFieldsWithAnnotation().selectPetFrom(petFields);
        final CountryFieldsWithAnnotation countryFields = new CountryFieldsWithAnnotation().selectPresidentFrom(presidentFields);

        final Argument argument = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(argument)
                                                     .withFields(countryFields)
                                                     .create();
        assertThat(actualQuery,
                   is(equalTo(
                           "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ president { pet { toy { format } } } } }\"}")));
    }

    @Test
    public void When_One_Field_Without_Child_And_Other_One_With_One_Chield_Selected_Then_Query_With_One_Field_Without_Child_And_Other_One_With_One_Child()
    {
        final CountryFieldsWithAnnotation countryFields = new CountryFieldsWithAnnotation().selectName()
                                                         .selectUfFrom(new UfFieldsWithAnnotation().selectName());

        final Argument argument = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(argument)
                                                     .withFields(countryFields)
                                                     .create();

        assertThat(actualQuery,
                   is(equalTo(
                           "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name uf { name } } }\"}")));
    }

    @Test
    public void When_All_Fields_Selected_One_By_One_Then_Query_With_All_Fields()
    {
        final CountryFieldsWithAnnotation fields = new CountryFieldsWithAnnotation().selectName().selectPib().selectPresident().selectUf();
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(fields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\")"
                                                   + "{ name pib uf { name officialName mainCities } "
                                                   + "president { age term profile pet { color name } } } }\"}")));
    }

    @Test
    public void When_All_Fields_Selected_At_Once_Then_Query_With_All_Fields()
    {
        final CountryFieldsWithAnnotation fields = new CountryFieldsWithAnnotation().selectAllFields();
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(fields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\")"
                                                   + "{ name pib uf { name officialName mainCities } "
                                                   + "president { age term profile pet { color name } } } }\"}")));
    }

    @Test
    public void When_One_Field_Set_To_False_Then_Query_Without_This_Field()
    {
        final CountryFieldsWithAnnotation fields = new CountryFieldsWithAnnotation().selectAllFieldsExceptPib();
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(fields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\")"
                                                   + "{ name uf { name officialName mainCities } "
                                                   + "president { age term profile pet { color name } } } }\"}")));
    }
}