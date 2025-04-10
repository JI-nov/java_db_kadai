package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Posts_Chapter07 {
    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement insertStatement = null;
        Statement selectStatement = null;

        // ポストリスト
        String[][] posts = {
            {"1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13"},
            {"1002", "2023-02-08", "お疲れ様です！", "12"},
            {"1003", "2023-02-09", "今日も頑張ります！", "18"},
            {"1001", "2023-02-09", "無理は禁物ですよ！", "17"},
            {"1002", "2023-02-10", "明日から連休ですね！", "20"}
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "tsax0604"
            );

            System.out.println("データベース接続成功：" + con.toString());

            // SQLクエリを準備
            String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
            insertStatement = con.prepareStatement(sql);

            int rowCnt = 0;
            for (int i = 0; i < posts.length; i++) {
                insertStatement.setString(1, posts[i][0]); // ユーザーID
                insertStatement.setString(2, posts[i][1]); // 投稿日時
                insertStatement.setString(3, posts[i][2]); // 投稿内容
                insertStatement.setString(4, posts[i][3]); // いいね数

                System.out.println("レコード追加を実行します: " + insertStatement.toString());
                rowCnt += insertStatement.executeUpdate();
            }
            System.out.println(rowCnt + "件のレコードが追加されました");

            // SELECTクエリ
            selectStatement = con.createStatement();
            String selectSql = "SELECT * FROM posts WHERE user_id = 1002;";

            ResultSet result = selectStatement.executeQuery(selectSql);

            System.out.println("ユーザーIDが1002のレコードを検索しました");
            int recordNumber = 1;
            while (result.next()) {
                String postedAt = result.getString("posted_at");
                String postContent = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(recordNumber + "件目：投稿日時=" + postedAt + "／投稿内容=" + postContent + "／いいね数=" + likes);
                recordNumber++;
            }

        } catch (SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            if (insertStatement != null) {
                try { insertStatement.close(); } catch (SQLException ignore) {}
            }
            if (selectStatement != null) {
                try { selectStatement.close(); } catch (SQLException ignore) {}
            }
            if (con != null) {
                try { con.close(); } catch (SQLException ignore) {}
            }
        }
    }
}