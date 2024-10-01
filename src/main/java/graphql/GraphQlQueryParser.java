package graphql;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphQlQueryParser {

    private static final String ROOT_BEAN_NAME_SUFFIX = "Query";
    private static final String SPACE_BETWEEN_SELECTED_NODES = " ";

    public static String parse(final Object graphQlQueryBean) {
        final Class<?> rootNodeClass = graphQlQueryBean.getClass();
        final List<Field> nodes = Arrays.asList(rootNodeClass.getDeclaredFields());
        String subTrees = nodes.stream()
                .filter(node -> !node.getType().equals(boolean.class))
                .map(subTree -> iterateSubtree(graphQlQueryBean, subTree))
                .collect(Collectors.joining());
        String selectedNodes = retrieveSelectedNodesNames(graphQlQueryBean, nodes);
        if(selectedNodes.isEmpty()) {
            subTrees = subTrees.stripLeading();
        }

        return "query{" + retrieveRootNodeName(rootNodeClass) + "{" + selectedNodes + subTrees + "}}";
    }

    private static String iterateSubtree(final Object subTreeParent, final Field subTreeField) {

        final Object subTree = retrieveSubTreeFromParent(subTreeParent, subTreeField);
        final Class<?> subTreeClass = subTree.getClass();
        final List<Field> subTreeChildren = Arrays.asList(subTreeClass.getDeclaredFields());
        final String selectedChildrenNodes = retrieveSelectedNodesNames(subTree, subTreeChildren);
        final List<Field> childrenWithDescendants = retrieveChildrenWithDescendants(subTreeClass, subTreeChildren);

        String parentNodeWithSelectedChildren = childrenWithDescendants.stream()
                .map(node -> iterateSubtree(subTree, node))
                .collect(Collectors.joining());

        if (selectedChildrenNodes.isEmpty()) {
            parentNodeWithSelectedChildren = parentNodeWithSelectedChildren.stripLeading();
        }

        if (selectedChildrenNodes.isEmpty() && parentNodeWithSelectedChildren.isEmpty()) {
            return "";
        }
        String s = " " + retrieveParentNodeName(subTreeClass) + "{" + selectedChildrenNodes + parentNodeWithSelectedChildren + "}";
        return s;
    }

    private static Object retrieveSubTreeFromParent(final Object subTreeParent, final Field subTreeField) {
        subTreeField.setAccessible(true);
        try {
            return subTreeField.get(subTreeParent);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Field> retrieveChildrenWithDescendants(final Class<?> subTreeClass,
                                                               final List<Field> subTreeChildren) {

        final Optional<Parameter> nodeParentOptional = Arrays.stream(
                subTreeClass.getDeclaredConstructors()[0].getParameters()
        ).findFirst();
        final Stream<Field> childrenWithDescendantsStream = subTreeChildren.stream()
                .filter(node -> !node.getType().equals(boolean.class));

        return nodeParentOptional.map(nodeParent -> childrenWithDescendantsStream
                        .filter(childNode -> !childNode.getType().equals(nodeParent.getType()))
                        .toList()
                ).orElseGet(childrenWithDescendantsStream::toList);
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
        return rootNodeName.substring(0, 1).toLowerCase() +
                rootNodeName.substring(1, rootNodeName.length() - ROOT_BEAN_NAME_SUFFIX.length()
        );
    }

    private static String retrieveParentNodeName(final Class<?> rootNodeClass) {
        final String rootNodeName = rootNodeClass.getSimpleName();
        return Character.toLowerCase(rootNodeName.charAt(0)) + rootNodeName.substring(1);
    }
}