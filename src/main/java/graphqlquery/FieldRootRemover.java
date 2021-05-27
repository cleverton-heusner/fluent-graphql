package graphqlquery;

public class FieldRootRemover
{
    public void removeRootFieldFromFields(final StringBuilder fields, final Object fieldsParent)
    {
        final int indexBeforeRootField = 0;
        final int indexAfterRootField = fields.indexOf("{");
        final String rootField = fields.substring(indexBeforeRootField, indexAfterRootField).trim();

        if (rootField.equalsIgnoreCase(fieldsParent.getClass().getSimpleName()))
        {
            fields.delete(indexBeforeRootField, indexAfterRootField);
        }
        else
        {
            fields.insert(0, "{ ");
            fields.append("}}");
        }
    }
}
