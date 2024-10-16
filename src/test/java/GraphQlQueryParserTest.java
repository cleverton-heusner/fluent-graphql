import graphql.GraphQlQueryParser;
import fixtures.GetPostQuery;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GraphQlQueryParserTest
{
    @Test
    public void when_oneLeafNodeSelected_then_queryWithOneNode()
    {
        // Arrange
        final String expectedQuery = "{\"query\":\"query{getPost{postId}}\"}";
        final var getPostQuery = new GetPostQuery().selectPostId().endSelection();

        // Act
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        // Assert
        assertThat(actualQuery, is(equalTo(expectedQuery)));
    }

    @Test
    public void when_moreThanOneLeafNodeSelected_then_queryWithMoreThanOneLeafNode()
    {
        // Arrange
        final String expectedQuery = "{\"query\":\"query{getPost{postId postTitle postText}}\"}";
        final var getPostQuery = new GetPostQuery().selectPostId()
                .selectPostTitle()
                .selectPostText()
                .endSelection();

        // Act
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        // Assert
        assertThat(actualQuery, is(equalTo(expectedQuery)));
    }

    @Test
    public void when_oneSubtreeWithOneLeafNodeAsChildSelected_then_queryWithOneSubtreeWithOneLeafNodeAsChild()
    {
        // Arrange
        final String expectedQuery = "{\"query\":\"query{getPost{comments{commentsId}}}\"}";
        final var getPostQuery = new GetPostQuery()
                .fromComments()
                .selectCommentsId()
                .endCommentsSelection()
            .endSelection();

        // Act
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        // Assert
        assertThat(actualQuery, is(equalTo(expectedQuery)));
    }

    @Test
    public void when_oneSubtreeWithOneSubtreeAsChildSelected_then_queryWithOneSubtreeWithOneSubtreeAsChild()
    {
        // Arrange
        final String expectedQuery = "{\"query\":\"query{getPost{comments{author{authorId}}}}\"}";
        final var getPostQuery = new GetPostQuery()
                .fromComments()
                    .fromAuthor()
                    .selectAuthorId()
                    .endAuthorSelection()
                .endCommentsSelection()
            .endSelection();

        // Act
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        // Assert
        assertThat(actualQuery, is(equalTo(expectedQuery)));
    }

    @Test
    public void when_oneSubtreeWithMoreThanOneLeafNodeAsChildrenSelected_then_queryWithOneSubtreeWithMoreThanOneLeafNodeAsChildren()
    {
        // Arrange
        final String expectedQuery = "{\"query\":\"query{getPost{comments{commentsId commentsText " +
                "commentsDatePublished}}}\"}";
        final var getPostQuery = new GetPostQuery()
                .fromComments()
                .selectCommentsId()
                .selectCommentsText()
                .selectCommentsDatePublished()
                .endCommentsSelection()
                .endSelection();

        // Act
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        // Assert
        assertThat(actualQuery, is(equalTo(expectedQuery)));
    }

    @Test
    public void when_oneLeafNodeAndOneSubtreeSelected_then_queryWithOneLeafNodeAndOneSubtree()
    {
        // Arrange
        final String expectedQuery = "{\"query\":\"query{getPost{postId comments{commentsId}}}\"}";
        final var getPostQuery = new GetPostQuery()
                .selectPostId()
                .fromComments()
                .selectCommentsId()
                .endCommentsSelection()
                .endSelection();

        // Act
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        // Assert
        assertThat(actualQuery, is(equalTo(expectedQuery)));
    }

    @Test
    public void when_allFieldsSelected_then_queryWithAllFields()
    {
        // Arrange
        final String expectedQuery = "{\"query\":\"query{getPost{postId postTitle postText postDatePublished postViews" +
                " postLikes comments{commentsId commentsText commentsDatePublished author{authorId authorName" +
                " authorEmail authorProfilePicture authorJoinedDate friends{friendsId friendsName friendsProfilePicture" +
                " mutualFriends{mutualFriendsId mutualFriendsName}}}} popularity{likes dislikes}}}\"}";
        final var getPostQuery = new GetPostQuery().selectAllFields().endSelection();

        // Act
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        // Assert
        assertThat(actualQuery, is(equalTo(expectedQuery)));
    }

    @Test
    public void when_lastLeafNodeSelected_then_queryWithLastLeafNode ()
    {
        // Arrange
        final String expectedQuery = "{\"query\":\"query{getPost{comments{author{friends{mutualFriends{" +
                "mutualFriendsId}}}}}}\"}";
        final var getPostQuery = new GetPostQuery()
                .fromComments()
                    .fromAuthor()
                        .fromFriends()
                            .fromMutualFriends()
                                .selectMutualFriendsId()
                            .endMutualFriendsSelection()
                        .endFriendsSelection()
                    .endAuthorSelection()
                .endCommentsSelection()
            .endSelection();

        // Act
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        // Assert
        assertThat(actualQuery, is(equalTo(expectedQuery)));
    }
}