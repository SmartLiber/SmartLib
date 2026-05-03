import java.util.List;

public class UserService {
    private UserDAO userDAO;
    private BookService bookService;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * 延迟获取 BookService 实例，避免循环依赖
     */
    private BookService getBookService() {
        if (this.bookService == null) {
            this.bookService = new BookService();
        }
        return this.bookService;
    }

    /**
     * 1. registerUser - 用户注册
     */
    public boolean registerUser(String username, String phone) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        if (userDAO.getUserByUsername(username) != null) {
            return false;
        }

        if (phone == null || !phone.matches("\\d{11}")) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPhone(phone);

        return userDAO.addUser(newUser);
    }

    /**
     * 2. loginUser - 用户登录
     */
    public User loginUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDAO.getUserByUsername(username);
    }

    /**
     * 3. getUserBorrowHistory - 查询用户借阅记录
     */
    public List<Book> getUserBorrowHistory(int userId) {
        return userDAO.getBorrowedBooksByUser(userId);
    }

    /**
     * 4. hasUnreturnedBooks - 验证用户是否有未归还书籍
     */
    public boolean hasUnreturnedBooks(int userId) {
        List<Book> borrowedBooks = userDAO.getBorrowedBooksByUser(userId);
        return borrowedBooks != null && !borrowedBooks.isEmpty();
    }

    /**
     * 5. getTotalUserCount - 获取用户总数
     */
    public int getTotalUserCount() {
        List<User> users = userDAO.getAllUsers();
        return users != null ? users.size() : 0;
    }

    /**
     * 6. updateUserInfo - 更新用户信息
     */
    public boolean updateUserInfo(int userId, String username, String phone) {
        User existingUser = userDAO.getUserById(userId);
        if (existingUser == null) {
            return false;
        }

        User userWithSameName = userDAO.getUserByUsername(username);
        if (userWithSameName != null && userWithSameName.getUserId() != userId) {
            return false;
        }

        if (phone != null && !phone.matches("\\d{11}")) {
            return false;
        }

        existingUser.setUsername(username);
        existingUser.setPhone(phone);

        return userDAO.updateUser(existingUser);
    }

    /**
     * 7. getUserById - 根据ID获取用户
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * 8. getAllUsers - 获取所有用户
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    /**
     * 9. borrowBookWithValidation - 带验证的借书功能（与 BookService 协作）
     */
    public boolean borrowBookWithValidation(int userId, int bookId) {
        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.out.println("用户不存在，ID：" + userId);
            return false;
        }

        if (!getBookService().isBookAvailable(bookId)) {
            System.out.println("书籍不可借阅");
            return false;
        }

        if (hasUnreturnedBooks(userId)) {
            System.out.println("用户有未归还的书籍，请先归还");
            return false;
        }

        return getBookService().borrowBook(userId, bookId);
    }

    /**
     * 10. returnBookWithValidation - 带验证的还书功能（与 BookService 协作）
     */
    public boolean returnBookWithValidation(int bookId) {
        Book book = getBookService().getBookFullInfo(bookId);
        if (book == null) {
            System.out.println("书籍不存在");
            return false;
        }

        boolean result = getBookService().returnBook(bookId);

        if (result) {
            System.out.println("还书成功");
        }

        return result;
    }

    /**
     * 11. getUserStatistics - 获取用户统计信息
     */
    public java.util.Map<String, Object> getUserStatistics(int userId) {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();

        User user = userDAO.getUserById(userId);
        if (user == null) {
            System.out.println("用户不存在，ID：" + userId);
            return null;
        }

        stats.put("user", user);

        List<Book> borrowedBooks = getUserBorrowHistory(userId);
        stats.put("currentBorrowedCount", borrowedBooks != null ? borrowedBooks.size() : 0);

        stats.put("hasUnreturned", hasUnreturnedBooks(userId));

        return stats;
    }
}
