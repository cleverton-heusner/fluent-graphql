import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import graphqlquery.Argument;
import graphqlquery.GraphqlQuery;
import invalidtypes.CountryWithInvalidTypesFields;
import invalidtypes.PetWithInvalidTypesFields;
import invalidtypes.PresidentWithInvalidTypesFieldsFields;
import invalidtypes.UfWithInvalidTypesFields;
import org.junit.Test;

public class FieldWithInvalidTypeTest
{
    @Test
    public void When_Field_With_String_Type_Selected_Then_Query_Without_String_Type()
    {
        final CountryWithInvalidTypesFields countryWithInvalidTypesFields = new CountryWithInvalidTypesFields()
                .selectName()
                .selectPib();

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryWithInvalidTypesFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name } }\"}")));
    }

    @Test
    public void When_Field_With_Integer_Type_Selected_Then_Query_Without_Integer_Type()
    {
        final CountryWithInvalidTypesFields countryWithInvalidTypesFields = new CountryWithInvalidTypesFields()
                .selectName()
                .selectUf();

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryWithInvalidTypesFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name ufWithInvalidTypesFields { nomeUf } } }\"}")));
    }

    @Test
    public void When_Field_With_Float_Type_Selected_Then_Query_Without_Float_Type()
    {
        final UfWithInvalidTypesFields ufFields = new UfWithInvalidTypesFields().selectName().selectCities();

        final CountryWithInvalidTypesFields countryWithInvalidTypesFields = new CountryWithInvalidTypesFields()
                .selectName()
                .selectUfFrom(ufFields);

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryWithInvalidTypesFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name ufWithInvalidTypesFields { nomeUf } } }\"}")));
    }

    @Test
    public void When_Field_With_Long_Type_Selected_Then_Query_Without_Long_Type()
    {
        final PresidentWithInvalidTypesFieldsFields presidentFields = new PresidentWithInvalidTypesFieldsFields()
                .selectAge().selectTerm();

        final CountryWithInvalidTypesFields countryWithInvalidTypesFields = new CountryWithInvalidTypesFields()
                .selectName()
                .selectPresidentFrom(presidentFields);

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryWithInvalidTypesFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name presidentWithInvalidTypesFieldsFields { age } } }\"}")));
    }

    @Test
    public void When_Field_With_Byte_Type_Selected_Then_Query_Without_Byte_Type()
    {
        final PresidentWithInvalidTypesFieldsFields presidentFields = new PresidentWithInvalidTypesFieldsFields()
                .selectAge()
                .selectProfile();

        final CountryWithInvalidTypesFields countryWithInvalidTypesFields = new CountryWithInvalidTypesFields()
                .selectName()
                .selectPresidentFrom(presidentFields);

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryWithInvalidTypesFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name presidentWithInvalidTypesFieldsFields { age } } }\"}")));
    }

    @Test
    public void When_Field_With_Short_Type_Selected_Then_Query_Without_Short_Type()
    {
        final PetWithInvalidTypesFields petFields = new PetWithInvalidTypesFields().selectName()
                                                                                   .selectColor();

        final PresidentWithInvalidTypesFieldsFields presidentFields = new PresidentWithInvalidTypesFieldsFields()
                .selectAge()
                .selectPetFrom(petFields);

        final CountryWithInvalidTypesFields countryWithInvalidTypesFields = new CountryWithInvalidTypesFields()
                .selectName()
                .selectPresidentFrom(presidentFields);

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryWithInvalidTypesFields)
                                                     .create();
        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name presidentWithInvalidTypesFieldsFields { age petWithInvalidTypesFields { name } } } }\"}")));
    }

    @Test
    public void When_Field_With_Double_Type_Selected_Then_Query_Without_Double_Type()
    {
        final CountryWithInvalidTypesFields countryWithInvalidTypesFields = new CountryWithInvalidTypesFields()
                .selectName()
                .selectPopulation();

        final Argument arguments = new Argument().put("name", "japan");

        final String actualQuery = new GraphqlQuery().withName("countriesByName").withArguments(arguments)
                                                     .withFields(countryWithInvalidTypesFields)
                                                     .create();

        assertThat(actualQuery, is(equalTo(
                "{ \"query\": \"{ countriesByName(name: \\\"japan\\\"){ name } }\"}")));
    }
}