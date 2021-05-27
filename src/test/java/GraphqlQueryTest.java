import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import graphqlquery.Argument;
import graphqlquery.GraphqlQuery;
import org.junit.Test;
import validtypes.CountryFields;
import validtypes.PetFields;
import validtypes.PresidentFields;
import validtypes.UfFields;

public class GraphqlQueryTest
{
    @Test
    public void When_One_Field_Selected_Then_Query_With_One_Field()
    {
        final CountryFields countryFields = new CountryFields().selectName();
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
        final CountryFields countryFields = new CountryFields().selectName()
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
        final UfFields ufFields = new UfFields().selectOfficialName();
        final CountryFields countryFields = new CountryFields().selectUfFrom(ufFields);
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryFields)
                                                     .create();




        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ ufFields { officialName } } }\"}")));
    }

    @Test
    public void When_One_Field_With_More_Than_One_Child_Selected_Then_Query_With_One_Field_With_More_Than_One_Child()
    {
        final UfFields ufFields = new UfFields().selectOfficialName().selectCities();
        final CountryFields countryFields = new CountryFields().selectUfFrom(ufFields);
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ ufFields { officialName mainCities } } }\"}")));
    }

    @Test
    public void When_One_Field_With_One_Descendant_Selected_Then_Query_With_One_Field_With_One_Descendant()
    {
        final PetFields petFields = new PetFields().selectToy();
        final PresidentFields presidentFields = new PresidentFields().selectPetFrom(petFields);
        final CountryFields countryFields = new CountryFields().selectPresidentFrom(presidentFields);

        final Argument argument = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(argument)
                                                     .withFields(countryFields)
                                                     .create();
        assertThat(actualQuery,
                   is(equalTo(
                           "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ presidentFields { petFields { toyFields { format } } } } }\"}")));
    }

    @Test
    public void When_One_Field_Without_Child_And_Other_One_With_One_Chield_Selected_Then_Query_With_One_Field_Without_Child_And_Other_One_With_One_Child()
    {
        CountryFields countryFields = new CountryFields().selectName()
                                                         .selectUfFrom(new UfFields().selectName());

        final Argument argument = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(argument)
                                                     .withFields(countryFields)
                                                     .create();

        assertThat(actualQuery,
                   is(equalTo(
                           "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name ufFields { nomeUf } } }\"}")));
    }

    @Test
    public void When_First_And_Last_Fields_Each_Without_Children_Selected_Then_Query_With_First_And_Last_Fields_Each_Without_Children()
    {
        final PresidentFields presidentFields = new PresidentFields().selectPetFrom(new PetFields().selectToy());
        final CountryFields fields = new CountryFields().selectName().selectPresidentFrom(presidentFields);

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(fields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\")"
                                                   + "{ name presidentFields { petFields { toyFields { format } } } } }\"}")));
    }

    @Test
    public void When_All_Fields_Selected_One_By_One_Then_Query_With_All_Fields()
    {
        final CountryFields fields = new CountryFields().selectName().selectPib().selectPresident().selectUf();
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(fields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\")"
                                                   + "{ name pib ufFields { nomeUf officialName mainCities } "
                                                   + "presidentFields { age term profile petFields { color name } } } }\"}")));
    }

    @Test
    public void When_All_Fields_Selected_At_Once_Then_Query_With_All_Fields()
    {
        final CountryFields fields = new CountryFields().selectAllFields();
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(fields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\")"
                                                   + "{ name pib ufFields { nomeUf officialName mainCities } "
                                                   + "presidentFields { age term profile petFields { color name } } } }\"}")));
    }

    //@TODO inteiro nao deveria ter aspas
    @Test
    public void When_More_Than_One_Argument_Selected_Then_Query_With_More_Than_One_Argument()
    {
        final CountryFields fields = new CountryFields().selectAllFields();
        final Argument arguments = new Argument().put("name", "japan").put("alias", "JPN");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(fields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\", alias: \\\"JPN\\\")"
                                                   + "{ name pib ufFields { nomeUf officialName mainCities } "
                                                   + "presidentFields { age term profile petFields { color name } } } }\"}")));
    }

    @Test
    public void When_One_Field_Set_To_False_Then_Query_Without_This_Field()
    {
        final CountryFields fields = new CountryFields().selectAllFieldsExceptPib();
        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(fields)
                                                     .create();

        assertThat(actualQuery, is(equalTo("{ \"query\": \"{ countriesByName(name: \\\"japan\\\")"
                                                   + "{ name ufFields { nomeUf officialName mainCities } "
                                                   + "presidentFields { age term profile petFields { color name } } } }\"}")));
    }
}