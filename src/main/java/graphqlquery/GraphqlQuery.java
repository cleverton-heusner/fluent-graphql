package graphqlquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraphqlQuery
{
    final ArgumentsCreator argumentsCreator = new ArgumentsCreator();
    private ChildFieldsCreator childFieldsCreator = new ChildFieldsCreator();
    private ParentFieldCreator createParentField = new ParentFieldCreator();

    private FieldRootRemover fieldRootRemover = new FieldRootRemover();

    private StringBuilder fields = new StringBuilder();
    private StringBuilder query = new StringBuilder();
    private String name;
    private List<Argument> arguments = new ArrayList<>();

    private Object fieldsParent;

    public GraphqlQuery withName(final String name)
    {
        this.name = name;
        return this;
    }

    public GraphqlQuery withArguments(final Argument... arguments)
    {
        this.arguments.addAll(Arrays.asList(arguments));
        return this;
    }

    public GraphqlQuery withFields(final Object fieldsParent)
    {
        this.fieldsParent = fieldsParent;
        return this;
    }

    public String create()
    {
        final String args = argumentsCreator.createArguments(arguments);

        createFields();
        openQuery().append(name).append(args).append(fields);
        closeQuery();

        return query.toString();
    }

    private void createFields()
    {
        createFields(fieldsParent);
        fieldRootRemover.removeRootFieldFromFields(fields, fieldsParent);
        trimFieldsToRight();
    }

    private void createFields(final Object parent)
    {
        if (isValidField(parent))
        {
            final Field[] fields = parent.getClass().getDeclaredFields();
            final boolean isAnyFieldSelected = isAnyFieldSelected(parent, fields);

            if (isAnyFieldSelected)
            {
                createParentField.createParentField(parent, this.fields);
                childFieldsCreator.createChildFields(getSelectedChildFields(parent, fields), this.fields);
            }

            createDescendantFields(parent, fields);

            if (isAnyFieldSelected)
            {
                addFieldsSelectionLimitToQuery();
            }
        }
    }

    private boolean isValidField(final Object parent)
    {
        return Optional.ofNullable(parent).isPresent() && !isWrapperType(parent.getClass());
    }

    private boolean isWrapperType(final Class<?> clazz)
    {
        return Arrays.asList("String", "Integer", "Long", "Short", "Byte", "Float", "Double")
                     .contains(clazz.getSimpleName());
    }

    private boolean isAnyFieldSelected(final Object parent, final Field[] fields)
    {
        if (Optional.ofNullable(parent).isPresent())
        {
            final List<Field> childFields = getSelectedChildFields(parent, fields);

            if (childFields.size() > 0)
            {
                return true;
            }

            Arrays.asList(fields).removeAll(childFields);

            for (final Field field : getDescendantFields(fields))
            {
                try
                {
                    boolean isAnyBooleanFieldSelected = isAnyFieldSelected(field.get(parent),
                                                                           field.getType().getDeclaredFields());
                    if (isAnyBooleanFieldSelected)
                    {
                        return true;
                    }
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private List<Field> getSelectedChildFields(final Object parent, final Field[] fields)
    {
        return Arrays.stream(fields).filter(field -> {
            try
            {
                field.setAccessible(true);
                return Boolean.TRUE.equals(field.get(parent));
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
    }

    private List<Field> getDescendantFields(final Field[] fields)
    {
        return Arrays.stream(fields).filter(field -> {
            field.setAccessible(true);
            return isParentField(field);
        }).collect(Collectors.toList());
    }

    private boolean isParentField(final Field field)
    {
        return !field.getType().isPrimitive() && !isWrapperType(field.getClass());
    }

    private void createDescendantFields(final Object parent, final Field[] fields)
    {
        getDescendantFields(fields).forEach(field -> {
            try
            {
                createFields(field.get(parent));
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        });
    }

    private void addFieldsSelectionLimitToQuery()
    {
        fields.append("} ");
    }

    private StringBuilder trimFieldsToRight()
    {
        return fields.deleteCharAt(fields.length() - 1);
    }

    private StringBuilder openQuery()
    {
        return query.append("{ \"query\": \"{ ");
    }

    private StringBuilder closeQuery()
    {
        return query.append(" }\"}");
    }
}
