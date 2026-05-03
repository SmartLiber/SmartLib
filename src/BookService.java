import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookService {
    
    private BookDAO bookDAO = new BookDAO();
    private BookCategoryDAO categoryDAO = new BookCategoryDAO();
    private UserDAO userDAO = new UserDAO();

    /**
     * 1. addNewBook - 添加书籍
     * 功能：添加新书籍
     * 输入：name, author, categoryId, introduction
     * 返回：boolean
     */
    public boolean addNewBook(String name, String author, int categoryId, String introduction) {
        try {
            if (name == null || name.trim().isEmpty()) {
                System.out.println("书名不能为空");
                return false;
            }
            if (author == null || author.trim().isEmpty()) {
                System.out.println("作者不能为空");
                return false;
            }
            
            BookCategory category = categoryDAO.getCategoryById(categoryId);
            if (category == null) {
                System.out.println("分类ID不存在：" + categoryId);
                return false;
            }
            
            Book book = new Book();
            book.setName(name);
            book.setAuthor(author);
            book.setCategoryId(categoryId);
            book.setIntroduction(introduction);
            book.setStatus("可借阅");
            book.setCreateTime(java.time.LocalDateTime.now().toString());
            book.setUpdateTime(java.time.LocalDateTime.now().toString());
            
            boolean result = bookDAO.addBook(book);
            System.out.println("添加书籍 '" + name + "' " + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 2. removeBook - 删除书籍
     * 功能：删除书籍
     * 输入：bookId (int)
     * 返回：boolean
     */
    public boolean removeBook(int bookId) {
        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                System.out.println("书籍不存在，ID：" + bookId);
                return false;
            }
            
            if (!"可借阅".equals(book.getStatus())) {
                System.out.println("只能删除'可借阅'状态的书籍，当前状态：" + book.getStatus());
                return false;
            }
            
            boolean result = bookDAO.deleteBook(bookId);
            System.out.println("删除书籍 '" + book.getName() + "' " + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 3. modifyBookInfo - 更新书籍信息
     * 功能：更新书籍信息
     * 输入：bookId, name, author, categoryId, introduction
     * 返回：boolean
     */
    public boolean modifyBookInfo(int bookId, String name, String author, int categoryId, String introduction) {
        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                System.out.println("书籍不存在，ID：" + bookId);
                return false;
            }
            
            if (!"可借阅".equals(book.getStatus()) && !"维护中".equals(book.getStatus())) {
                System.out.println("只能更新'可借阅'或'维护中'状态的书籍，当前状态：" + book.getStatus());
                return false;
            }
            
            BookCategory category = categoryDAO.getCategoryById(categoryId);
            if (category == null) {
                System.out.println("分类ID不存在：" + categoryId);
                return false;
            }
            
            book.setName(name);
            book.setAuthor(author);
            book.setCategoryId(categoryId);
            book.setIntroduction(introduction);
            book.setUpdateTime(java.time.LocalDateTime.now().toString());
            
            boolean result = bookDAO.updateBook(book);
            System.out.println("更新书籍信息 " + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 4. searchBooks - 搜索书籍
     * 功能：按关键词搜索书籍
     * 输入：keyword (String)
     * 返回：List<Book>
     */
    public List<Book> searchBooks(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                System.out.println("搜索关键词不能为空");
                return new ArrayList<>();
            }
            
            List<Book> results = bookDAO.searchBooks(keyword);
            System.out.println("搜索关键词 '" + keyword + "' 找到 " + results.size() + " 本书");
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 5. getBookFullInfo - 获取书籍完整信息
     * 功能：获取书籍完整信息（含分类和借阅人）
     * 输入：bookId (int)
     * 返回：Book
     */
    public Book getBookFullInfo(int bookId) {
        try {
            Book book = bookDAO.getBookDetailById(bookId);
            if (book == null) {
                System.out.println("书籍不存在，ID：" + bookId);
            }
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 6. borrowBook - 借书
     * 功能：用户借阅书籍
     * 输入：userId (int), bookId (int)
     * 返回：boolean
     */
    public boolean borrowBook(int userId, int bookId) {
        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                System.out.println("书籍不存在，ID：" + bookId);
                return false;
            }
            
            if (!isBookAvailable(bookId)) {
                System.out.println("书籍不可借阅，当前状态：" + book.getStatus());
                return false;
            }
            
            User user = userDAO.getUserById(userId);
            if (user == null) {
                System.out.println("用户不存在，ID：" + userId);
                return false;
            }
            
            boolean result = userDAO.borrowBook(userId, bookId);
            System.out.println("用户 " + userId + " 借阅书籍 " + bookId + " " + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 7. returnBook - 还书
     * 功能：用户归还书籍
     * 输入：bookId (int)
     * 返回：boolean
     */
    public boolean returnBook(int bookId) {
        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                System.out.println("书籍不存在，ID：" + bookId);
                return false;
            }
            
            if (!"已借出".equals(book.getStatus())) {
                System.out.println("只能归还'已借出'状态的书籍，当前状态：" + book.getStatus());
                return false;
            }
            
            boolean result = userDAO.returnBook(bookId);
            System.out.println("归还书籍 " + bookId + " " + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 8. renewBook - 续借书籍
     * 功能：续借书籍（延长借阅时间）
     * 输入：bookId (int), days (int)
     * 返回：boolean
     */
    public boolean renewBook(int bookId, int days) {
        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                System.out.println("书籍不存在，ID：" + bookId);
                return false;
            }
            
            if (!"已借出".equals(book.getStatus())) {
                System.out.println("只能续借'已借出'状态的书籍，当前状态：" + book.getStatus());
                return false;
            }
            
            if (days <= 0 || days > 30) {
                System.out.println("续借天数必须在1-30天之间，当前输入：" + days);
                return false;
            }
            
            book.setUpdateTime(java.time.LocalDateTime.now().toString());
            boolean result = bookDAO.updateBook(book);
            System.out.println("续借书籍 " + bookId + " " + days + " 天 " + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 9. addCategory - 添加分类
     * 功能：添加书籍分类
     * 输入：categoryName (String)
     * 返回：boolean
     */
    public boolean addCategory(String categoryName) {
        try {
            if (categoryName == null || categoryName.trim().isEmpty()) {
                System.out.println("分类名不能为空");
                return false;
            }
            
            BookCategory existingCategory = categoryDAO.getCategoryByName(categoryName);
            if (existingCategory != null) {
                System.out.println("分类已存在：" + categoryName);
                return false;
            }
            
            BookCategory category = new BookCategory();
            category.setCategoryName(categoryName);
            
            boolean result = categoryDAO.addCategory(category);
            System.out.println("添加分类 '" + categoryName + "' " + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 10. removeCategory - 删除分类
     * 功能：删除书籍分类
     * 输入：categoryId (int)
     * 返回：boolean
     */
    public boolean removeCategory(int categoryId) {
        try {
            BookCategory category = categoryDAO.getCategoryById(categoryId);
            if (category == null) {
                System.out.println("分类不存在，ID：" + categoryId);
                return false;
            }
            
            List<Book> booksInCategory = categoryDAO.getBooksByCategory(categoryId);
            if (!booksInCategory.isEmpty()) {
                System.out.println("分类下有 " + booksInCategory.size() + " 本书，不能删除");
                return false;
            }
            
            boolean result = categoryDAO.deleteCategory(categoryId);
            System.out.println("删除分类 '" + category.getCategoryName() + "' " + (result ? "成功" : "失败"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 11. getCategoryWithBooks - 获取分类及书籍
     * 功能：获取指定分类及其下的所有书籍
     * 输入：categoryId (int)
     * 返回：Map<String, Object>
     */
    public Map<String, Object> getCategoryWithBooks(int categoryId) {
        try {
            BookCategory category = categoryDAO.getCategoryById(categoryId);
            if (category == null) {
                System.out.println("分类不存在，ID：" + categoryId);
                return null;
            }
            
            List<Book> books = categoryDAO.getBooksByCategory(categoryId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("category", category);
            result.put("books", books);
            result.put("bookCount", books.size());
            
            System.out.println("获取分类 '" + category.getCategoryName() + "' 及其 " + books.size() + " 本书");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 12. isBookAvailable - 验证书籍可借状态
     * 功能：检查书籍是否可以借阅
     * 输入：bookId (int)
     * 返回：boolean
     */
    public boolean isBookAvailable(int bookId) {
        try {
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                return false;
            }
            
            return "可借阅".equals(book.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
