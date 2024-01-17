package np.vtor_kolok._37_Post;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
class Comment{
    String user;
    String id;
    String content;
    int likes;

    public Comment(String user, String id, String content) {
        this.user = user;
        this.id = id;
        this.content = content;
        likes =0;
    }
    public void addLike(){
        likes++;
    }

    public int getLikes() {
        return likes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("        Comment: ").append(content).append("\n");
        sb.append("        Written by: ").append(user).append("\n");
        sb.append("        Likes: ").append(likes).append("\n");
        return sb.toString();
    }
}
class Post{
    String username;
    String postContent;
    Map<String, Comment> commentsById;

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        commentsById = new HashMap<>();
    }
    public void addComment (String username, String commentId, String content, String replyToId){
        Comment comment = new Comment(username, commentId, content);
        //if (replyToId == null){
            commentsById.put(commentId, comment);
        //}
    }
    public void likeComment(String commentId){
        commentsById.get(commentId).addLike();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Post: ").append(postContent).append("\n");
        sb.append("Written by: ").append(username).append("\n");
        sb.append("Comments: ").append("\n");
        commentsById.values().stream()
                .sorted(Comparator.comparing(Comment::getLikes).reversed())
                .forEach(sb::append);
        return sb.toString();
    }
}

public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}

