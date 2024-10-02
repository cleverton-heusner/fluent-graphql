package graphql;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GraphQlQueryParser {

    private static final String NO_SELECTED_NODES = "";
    private static final String ROOT_BEAN_NAME_SUFFIX = "Query";
    private static final String SPACE_BETWEEN_SELECTED_NODES = " ";

    public static String parse(final Object graphQlQueryBean) {

        final Class<?> rootNodeClass = graphQlQueryBean.getClass();
        final List<Field> subTreeChildren = Arrays.asList(rootNodeClass.getDeclaredFields());
        final String selectedChildrenNodes = retrieveSelectedNodesNames(graphQlQueryBean, subTreeChildren);
        String parentNodeWithSelectedChildren = retrieveParentNodeWithSelectedChildren(graphQlQueryBean, subTreeChildren);

        if (selectedChildrenNodes.isEmpty()) {
            parentNodeWithSelectedChildren = parentNodeWithSelectedChildren.stripLeading();
        }

        return "query{" +
                retrieveRootNodeName(rootNodeClass) +
                "{" +
                selectedChildrenNodes +
                parentNodeWithSelectedChildren +
                "}}";
    }

    private static String iterateSubtree(final Object subTreeParent, final Field subTreeField) {

        final Object subTree = retrieveSubTreeFromParent(subTreeParent, subTreeField);
        final Class<?> subTreeClass = subTree.getClass();
        final List<Field> subTreeChildren = Arrays.asList(subTreeClass.getDeclaredFields());
        final String selectedChildrenNodes = retrieveSelectedNodesNames(subTree, subTreeChildren);
        String parentNodeWithSelectedChildren = retrieveParentNodeWithSelectedChildren(subTree, subTreeChildren);

        if (selectedChildrenNodes.isEmpty()) {
            parentNodeWithSelectedChildren = parentNodeWithSelectedChildren.stripLeading();

            if (parentNodeWithSelectedChildren.isEmpty()) {
                return NO_SELECTED_NODES;
            }
        }

        return " " +
                retrieveParentNodeName(subTreeClass) +
                "{" +
                selectedChildrenNodes +
                parentNodeWithSelectedChildren +
                "}";
    }

    private static Object retrieveSubTreeFromParent(final Object subTreeParent, final Field subTreeField) {
        subTreeField.setAccessible(true);
        try {
            return subTreeField.get(subTreeParent);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String retrieveParentNodeWithSelectedChildren(final Object subTree,
                                                                 final List<Field> subTreeChildren) {

        return subTreeChildren.stream()
                .filter(subTreeChild -> !subTreeChild.getType().equals(boolean.class))
                .filter(subTreeChild -> !subTreeChild.getType().equals(retrieveNodeParentType(subTree)))
                .map(childWithDescendant -> iterateSubtree(subTree, childWithDescendant))
                .collect(Collectors.joining());
    }

    private static Type retrieveNodeParentType(final Object subTree) {
        final Parameter[] constructors = subTree.getClass().getDeclaredConstructors()[0].getParameters();
        return constructors.length == 0 ? null : constructors[0].getType();
    }

    private static String retrieveSelectedNodesNames(final Object graphQlQueryBean,
                                                     final List<Field> nodes) {
        return nodes.stream()
                .filter(node -> node.getType().equals(boolean.class))
                .filter(node -> isSelectedNode(node, graphQlQueryBean))
                .map(Field::getName)
                .collect(Collectors.joining(SPACE_BETWEEN_SELECTED_NODES));
    }

    private static boolean isSelectedNode(final Field node, final Object graphQlQueryBean) {
        try {
            node.setAccessible(true);
            return node.get(graphQlQueryBean).equals(Boolean.TRUE);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String retrieveRootNodeName(final Class<?> rootNodeClass) {
        final String rootNodeName = rootNodeClass.getSimpleName();
        return lowercase(rootNodeName)
                .substring(0, rootNodeName.length() - ROOT_BEAN_NAME_SUFFIX.length());
    }

    private static String retrieveParentNodeName(final Class<?> rootNodeClass) {
        return lowercase(rootNodeClass.getSimpleName());
    }
    
    private static String lowercase(final String word) {
        return Character.toLowerCase(word.charAt(0)) + word.substring(1);
    }
}