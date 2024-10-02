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
        final GetPostQuery getPostQuery = new GetPostQuery().selectPostId().endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("{\"query\":\"query{getPost{postId}}\"}")));
    }

    @Test
    public void when_moreThanOneLeafNodeSelected_then_queryWithMoreThanOneLeafNode()
    {
        final GetPostQuery getPostQuery = new GetPostQuery().selectPostId()
                .selectPostTitle()
                .selectPostText()
                .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("{\"query\":\"query{getPost{postId postTitle postText}}\"}")));
    }

    @Test
    public void when_oneSubtreeWithOneLeafNodeAsChildSelected_then_queryWithOneSubtreeWithOneLeafNodeAsChild()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .fromComments()
                .selectCommentsId()
                .endCommentsSelection()
            .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("{\"query\":\"query{getPost{comments{commentsId}}}\"}")));
    }

    @Test
    public void when_oneSubtreeWithOneSubtreeAsChildSelected_then_queryWithOneSubtreeWithOneSubtreeAsChild()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .fromComments()
                    .fromAuthor()
                    .selectAuthorId()
                    .endAuthorSelection()
                .endCommentsSelection()
            .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("{\"query\":\"query{getPost{comments{author{authorId}}}}\"}")));
    }

    @Test
    public void when_oneSubtreeWithMoreThanOneLeafNodeAsChildrenSelected_then_queryWithOneSubtreeWithMoreThanOneLeafNodeAsChildren()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .fromComments()
                .selectCommentsId()
                .selectCommentsText()
                .selectCommentsDatePublished()
                .endCommentsSelection()
                .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("{\"query\":\"query{getPost{comments{commentsId commentsText" +
                " commentsDatePublished}}}\"}"))
        );
    }

    @Test
    public void when_oneLeafNodeAndOneSubtreeSelected_then_queryWithOneLeafNodeAndOneSubtree()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .selectPostId()
                .fromComments()
                .selectCommentsId()
                .endCommentsSelection()
                .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("{\"query\":\"query{getPost{postId comments{commentsId}}}\"}")));
    }

    @Test
    public void when_allFieldsSelected_then_queryWithAllFields()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .selectAllFields()
                .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("{\"query\":\"query{getPost{postId postTitle postText" +
                " postDatePublished postViews postLikes comments{commentsId commentsText commentsDatePublished" +
                " author{authorId authorName authorEmail authorProfilePicture authorJoinedDate friends{friendsId" +
                " friendsName friendsProfilePicture mutualFriends{mutualFriendsId mutualFriendsName}}}}" +
                " popularity{likes dislikes}}}\"}"))
        );
    }

    @Test
    public void when_lastLeafNodeSelected_then_queryWithLastLeafNode ()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
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
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("{\"query\":\"query{getPost{comments{author{friends{mutualFriends{" +
                "mutualFriendsId}}}}}}\"}")));
    }
}