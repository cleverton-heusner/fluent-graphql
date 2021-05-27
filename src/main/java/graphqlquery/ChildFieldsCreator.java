package graphqlquery;

import fieldalias.GraphQLField;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import utils.Decapitalizer;

public class ChildFieldsCreator
{
    private Decapitalizer decapitalizer = new Decapitalizer();

    public long createChildFields(final List<Field> selectedChildFields, final StringBuilder fields)
    {
        final AtomicLong numChildFieldAddedToQuery = new AtomicLong();

        selectedChildFields.stream()
                           .map(this::getFieldName)
                           .map(fieldName -> decapitalizer.decapitalize(fieldName))
                           .forEach(fieldName -> {
                               fields.append(fieldName).append(" ");
                               numChildFieldAddedToQuery.getAndIncrement();
                           });

        return numChildFieldAddedToQuery.get();
    }

    private String getFieldName(final Field field)
    {
        return field.isAnnotationPresent(GraphQLField.class)
                ? field.getDeclaredAnnotation(GraphQLField.class).value()
                : field.getName();
    }
}