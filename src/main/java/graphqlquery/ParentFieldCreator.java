package graphqlquery;

import fieldalias.GraphQLField;
import utils.Decapitalizer;

public class ParentFieldCreator
{
    private Decapitalizer decapitalizer = new Decapitalizer();

    public void createParentField(final Object parent, final StringBuilder fields)
    {
        final Class<?> parentClass = parent.getClass();

        final String parentFieldName = parentClass.isAnnotationPresent(GraphQLField.class)
                ? parentClass.getAnnotation(GraphQLField.class).value()
                : parentClass.getSimpleName();

        fields.append(decapitalizer.decapitalize(parentFieldName)).append(" { ");
    }
}
