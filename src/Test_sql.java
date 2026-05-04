import java.sql.Connection;
import java.util.List;

public class Test_sql {
    public static void main(String[] args) {
        System.out.println("========== 测试数据库连接 ==========");
        Connection conn = DBUtil.getConnection();
        if (conn != null) {
            System.out.println("数据库连接成功！");
            DBUtil.closeConnection(conn);
        } else {
            System.out.println("数据库连接失败！");
            return;
        }

        BookDAO bookDAO = new BookDAO();
        UserDAO userDAO = new UserDAO();
        BookCategoryDAO categoryDAO = new BookCategoryDAO();

        System.out.println("\n========== 测试 BookCategory 功能 ==========");

        System.out.println("\n1. 查询所有分类：");
        List<BookCategory> categories = categoryDAO.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("暂无分类");
        } else {
            for (BookCategory c : categories) {
                System.out.println(c.getCategoryId() + ". " + c.getCategoryName());
            }
        }

        System.out.println("\n2. 添加测试分类：");
        BookCategory testCategory = new BookCategory();
        testCategory.setCategoryName("测试分类");
        boolean categoryAdded = categoryDAO.addCategory(testCategory);
        System.out.println("添加分类结果: " + (categoryAdded ? "成功" : "失败"));

        System.out.println("\n3. 根据名称查询分类：");
        BookCategory foundCategory = categoryDAO.getCategoryByName("测试分类");
        int testCategoryId = 0;
        if (foundCategory != null) {
            testCategoryId = foundCategory.getCategoryId();
            System.out.println("找到分类：" + foundCategory.getCategoryName());
        }

        System.out.println("\n4. 更新分类：");
        if (foundCategory != null) {
            foundCategory.setCategoryName("测试分类-更新版");
            boolean categoryUpdated = categoryDAO.updateCategory(foundCategory);
            System.out.println("更新分类结果: " + (categoryUpdated ? "成功" : "失败"));
        }

        System.out.println("\n========== 测试 Book 功能 ==========");

        System.out.println("\n1. 查询所有书籍：");
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("暂无书籍");
        } else {
            for (Book b : books) {
                System.out.println(b.getId() + ". " + b.getName() + " - " + b.getAuthor() + " [" + b.getStatus() + "]");
            }
        }

        System.out.println("\n2. 添加测试书籍：");
        Book testBook = new Book();
        testBook.setName("测试书籍");
        testBook.setAuthor("测试作者");
        testBook.setCategoryId(testCategoryId > 0 ? testCategoryId : 1);
        testBook.setIntroduction("这是一本测试书籍");
        testBook.setStatus("可借阅");
        boolean bookAdded = bookDAO.addBook(testBook);
        System.out.println("添加书籍结果: " + (bookAdded ? "成功" : "失败"));

        System.out.println("\n3. 按状态查询（可借阅）：");
        List<Book> availableBooks = bookDAO.getBooksByStatus("可借阅");
        for (Book b : availableBooks) {
            System.out.println(b.getId() + ". " + b.getName());
        }

        System.out.println("\n4. 搜索书籍（测试）：");
        List<Book> searchResults = bookDAO.searchBooks("测试");
        for (Book b : searchResults) {
            System.out.println(b.getId() + ". " + b.getName());
        }

        int testBookId = 0;
        if (!searchResults.isEmpty()) {
            testBookId = searchResults.get(0).getId();
            System.out.println("\n5. 根据ID查询书籍（ID=" + testBookId + "）：");
            Book foundBook = bookDAO.getBookById(testBookId);
            if (foundBook != null) {
                System.out.println("找到书籍：" + foundBook.getName() + " - " + foundBook.getAuthor());
            }

            System.out.println("\n6. 查询书籍完整信息（包含关联表）：");
            Book detailBook = bookDAO.getBookDetailById(testBookId);
            if (detailBook != null) {
                System.out.println("书籍ID: " + detailBook.getId());
                System.out.println("书名: " + detailBook.getName());
                System.out.println("作者: " + detailBook.getAuthor());
                System.out.println("简介: " + detailBook.getIntroduction());
                System.out.println("状态: " + detailBook.getStatus());
                if (detailBook.getCategory() != null) {
                    System.out.println("分类: " + detailBook.getCategory().getCategoryId() + " - " + detailBook.getCategory().getCategoryName());
                } else {
                    System.out.println("分类: 无");
                }
                if (detailBook.getBorrowUser() != null) {
                    System.out.println("借阅人: " + detailBook.getBorrowUser().getUserId() + " - " + detailBook.getBorrowUser().getUsername() + " - " + detailBook.getBorrowUser().getPhone());
                    System.out.println("借阅时间: " + detailBook.getBorrowDate());
                } else {
                    System.out.println("借阅人: 无");
                }
                System.out.println("归还时间: " + detailBook.getReturnDate());
                System.out.println("创建时间: " + detailBook.getCreateTime());
                System.out.println("更新时间: " + detailBook.getUpdateTime());
            }

            System.out.println("\n7. 更新书籍：");
            if (foundBook != null) {
                foundBook.setName("测试书籍-更新版");
                boolean bookUpdated = bookDAO.updateBook(foundBook);
                System.out.println("更新书籍结果: " + (bookUpdated ? "成功" : "失败"));
            }
        }

        System.out.println("\n========== 测试 User 功能 ==========");

        System.out.println("\n1. 查询所有用户：");
        List<User> users = userDAO.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("暂无用户");
        } else {
            for (User u : users) {
                System.out.println(u.getUserId() + ". " + u.getUsername() + " - " + u.getPhone());
            }
        }

        System.out.println("\n2. 添加测试用户：");
        User testUser = new User();
        testUser.setUsername("测试用户");
        testUser.setPhone("13900139000");
        boolean userAdded = userDAO.addUser(testUser);
        System.out.println("添加用户结果: " + (userAdded ? "成功" : "失败"));

        System.out.println("\n3. 根据用户名查询：");
        User foundUser = userDAO.getUserByUsername("测试用户");
        int testUserId = 0;
        if (foundUser != null) {
            testUserId = foundUser.getUserId();
            System.out.println("找到用户：" + foundUser.getUsername() + " - " + foundUser.getPhone());
        }

        System.out.println("\n4. 更新用户：");
        if (foundUser != null) {
            foundUser.setPhone("13900139999");
            boolean userUpdated = userDAO.updateUser(foundUser);
            System.out.println("更新用户结果: " + (userUpdated ? "成功" : "失败"));
        }

        System.out.println("\n========== 测试借阅功能 ==========");

        if (testBookId > 0 && testUserId > 0) {
            System.out.println("\n1. 用户 " + testUserId + " 借阅书籍 " + testBookId + "：");
            boolean borrowed = userDAO.borrowBook(testUserId, testBookId);
            System.out.println("借书结果: " + (borrowed ? "成功" : "失败"));

            System.out.println("\n2. 查询用户 " + testUserId + " 的借阅记录：");
            List<Book> borrowedBooks = userDAO.getBorrowedBooksByUser(testUserId);
            if (borrowedBooks.isEmpty()) {
                System.out.println("暂无借阅记录");
            } else {
                for (Book b : borrowedBooks) {
                    System.out.println(b.getName() + " - 借阅时间: " + b.getBorrowDate());
                }
            }

            System.out.println("\n3. 归还书籍 " + testBookId + "：");
            boolean returned = userDAO.returnBook(testBookId);
            System.out.println("还书结果: " + (returned ? "成功" : "失败"));
        }

        System.out.println("\n========== 测试分类与书籍关联 ==========");

        if (testCategoryId > 0) {
            System.out.println("\n查询分类 " + testCategoryId + " 下的书籍：");
            List<Book> categoryBooks = categoryDAO.getBooksByCategory(testCategoryId);
            if (categoryBooks.isEmpty()) {
                System.out.println("该分类暂无书籍");
            } else {
                for (Book b : categoryBooks) {
                    System.out.println(b.getName());
                }
            }
        }

        System.out.println("\n========== 清理测试数据 ==========");

        if (testBookId > 0) {
            System.out.println("删除测试书籍（ID=" + testBookId + "）：");
            boolean bookDeleted = bookDAO.deleteBook(testBookId);
            System.out.println("删除书籍结果: " + (bookDeleted ? "成功" : "失败"));
        }

        if (testUserId > 0) {
            System.out.println("删除测试用户（ID=" + testUserId + "）：");
            boolean userDeleted = userDAO.deleteUser(testUserId);
            System.out.println("删除用户结果: " + (userDeleted ? "成功" : "失败"));
        }

        if (testCategoryId > 0) {
            System.out.println("删除测试分类（ID=" + testCategoryId + "）：");
            boolean categoryDeleted = categoryDAO.deleteCategory(testCategoryId);
            System.out.println("删除分类结果: " + (categoryDeleted ? "成功" : "失败"));
        }

        System.out.println("\n========== 测试完成 ==========");
    }
}