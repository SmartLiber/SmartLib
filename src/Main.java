import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("测试 MySQL 数据库连接...");
        
        Connection conn = DBUtil.getConnection();
        if (conn != null) {
            System.out.println("数据库连接成功！");
            DBUtil.closeConnection(conn);
        } else {
            System.out.println("数据库连接失败！");
            return;
        }

        BookDAO bookDAO = new BookDAO();
        
        System.out.println("\n获取所有书籍：");
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("暂无书籍数据");
        } else {
            for (Book book : books) {
                System.out.println("ID: " + book.getId() + ", 书名: " + book.getName() + ", 作者: " + book.getAuthor() + ", 状态: " + book.getStatus());
            }
        }
    }
}