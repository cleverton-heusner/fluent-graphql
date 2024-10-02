import graphql.GraphQlQueryParser;
import org.junit.Test;
import fixtures.GetPostQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class GraphQlQueryParserTest
{
    @Test
    public void When_One_Field_Selected_Then_Query_With_One_Field1()
    {
        final GetPostQuery getPostQuery = new GetPostQuery().selectPostId().endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("query{getPost{postId}}")));
    }

    @Test
    public void When_More_Than_One_Field_Selected_Then_Query_With_More_Than_One_Field1()
    {
        final GetPostQuery getPostQuery = new GetPostQuery().selectPostId()
                .selectPostTitle()
                .selectPostText()
                .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("query{getPost{postId postTitle postText}}")));
    }

    @Test
    public void When_One_Field_With_One_Child_Selected_Then_Query_With_One_Field_With_One_Child1()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .fromComments()
                .selectCommentsId()
                .endCommentsSelection()
            .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("query{getPost{comments{commentsId}}}")));
    }

    @Test
    public void When_One_Field_With_More_Than_One_Child_Selected_Then_Query_With_One_Field_With_More_Than_One_Child1()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .fromComments()
                    .fromAuthor()
                    .selectAuthorId()
                    .endAuthorSelection()
                .endCommentsSelection()
            .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("query{getPost{comments{author{authorId}}}}")));
    }

    @Test
    public void When_One_Field_With_One_Descendant_Selected_Then_Query_With_One_Field_With_One_Descendant1()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .fromComments()
                .selectCommentsId()
                .selectCommentsText()
                .selectCommentsDatePublished()
                .endCommentsSelection()
                .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("query{getPost{comments{commentsId commentsText commentsDatePublished}}}")));
    }

    @Test
    public void When_One_Field_Without_Child_And_Other_One_With_One_Chield_Selected_Then_Query_With_One_Field_Without_Child_And_Other_One_With_One_Child1()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .selectPostId()
                .fromComments()
                .selectCommentsId()
                .endCommentsSelection()
                .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("query{getPost{postId comments{commentsId}}}")));
    }

    // selecionar todos
    @Test
    public void When_First_And_Last_Fields_Each_Without_Children_Selected_Then_Query_With_First_And_Last_Fields_Each_Without_Children1()
    {
        final GetPostQuery getPostQuery = new GetPostQuery()
                .selectAllFields()
                .endSelection();
        final String actualQuery = GraphQlQueryParser.parse(getPostQuery);

        assertThat(actualQuery, is(equalTo("query{getPost{postId postTitle postText postDatePublished postViews postLikes comments{commentsId commentsText commentsDatePublished author{authorId authorName authorEmail authorProfilePicture authorJoinedDate friends{friendsId friendsName friendsProfilePicture mutualFriends{mutualFriendsId mutualFriendsName}}}} popularity{likes dislikes}}}")));
    }
}