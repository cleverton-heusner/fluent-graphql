package graphql;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GraphQlQueryParser {

    private static final byte ROOT_NODE_PARAM = 0;
    private static final byte SUBTREE_PARENT_PARAM = 0;
    private static final byte SUBTREE_PARAM = 1;

    private static final String NO_SELECTED_NODES = "";
    private static final String ROOT_BEAN_NAME_SUFFIX = "Query";
    private static final String SPACE_BETWEEN_SELECTED_NODES = " ";

    public static String parse(final Object ...params) {

        final Object node = retrieveCurrentNode(params);
        final Class<?> nodeClass = node.getClass();
        final List<Field> nodeChildren = Arrays.asList(nodeClass.getDeclaredFields());
        final String selectedChildrenNodes = retrieveSelectedNodesNames(node, nodeChildren);
        String selectedParentNodeWithChildren = retrieveSelectedParentNodeWithChildren(node, nodeChildren);

        if (selectedChildrenNodes.isEmpty()) {
            selectedParentNodeWithChildren = selectedParentNodeWithChildren.stripLeading();

            if (isSubtreeNode(params) && selectedParentNodeWithChildren.isEmpty()) {
                return NO_SELECTED_NODES;
            }
        }

        return formatSelectedFields(nodeClass, selectedChildrenNodes, selectedParentNodeWithChildren, params);
    }

    private static Object retrieveCurrentNode(final Object... params) {
        if (isRootNode(params)) {
            return params[ROOT_NODE_PARAM];
        }

        return retrieveSubtreeFromParent(params[SUBTREE_PARENT_PARAM], (Field) params[SUBTREE_PARAM]);
    }

    private static boolean isRootNode(final Object... params) {
        return params.length == 1;
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

    private static Object retrieveSubtreeFromParent(final Object subtreeParent, final Field subtreeNode) {
        subtreeNode.setAccessible(true);
        try {
            return subtreeNode.get(subtreeParent);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String retrieveSelectedParentNodeWithChildren(final Object subtree,
                                                                 final List<Field> subtreeChildren) {
        return subtreeChildren.stream()
                .filter(subtreeChild -> !subtreeChild.getType().equals(boolean.class))
                .filter(subtreeChild -> !subtreeChild.getType().equals(retrieveNodeParentType(subtree)))
                .map(childWithDescendant -> parse(subtree, childWithDescendant))
                .collect(Collectors.joining());
    }

    private static Type retrieveNodeParentType(final Object subtree) {
        final Parameter[] constructors = subtree.getClass().getDeclaredConstructors()[0].getParameters();
        return constructors.length == 0 ? null : constructors[0].getType();
    }

    private static boolean isSubtreeNode(final Object... params) {
        return params.length == 2;
    }

    private static String retrieveRootNodeName(final Class<?> rootNodeClass) {
        final String rootNodeName = rootNodeClass.getSimpleName();
        return lowercase(rootNodeName).substring(0, rootNodeName.length() - ROOT_BEAN_NAME_SUFFIX.length());
    }

    private static String retrieveParentNodeName(final Class<?> rootNodeClass) {
        return lowercase(rootNodeClass.getSimpleName());
    }
    
    private static String lowercase(final String word) {
        return Character.toLowerCase(word.charAt(0)) + word.substring(1);
    }

    private static String formatSelectedFields(final Class<?> nodeClass,
                                               final String selectedChildrenNodes,
                                               final String selectedParentNodeWithChildren,
                                               final Object... params) {

        final boolean isRootNode = isRootNode(params);
        final String fieldsSelectionBegin = isRootNode ? "{\"query\":\"query{" : " ";
        final String nodeName = isRootNode ? retrieveRootNodeName(nodeClass) : retrieveParentNodeName(nodeClass);
        final String fieldsSelectionEnd = isRootNode ? "}}\"}" : "}";

        return fieldsSelectionBegin + nodeName + "{" + selectedChildrenNodes + selectedParentNodeWithChildren +
                fieldsSelectionEnd;
    }
}