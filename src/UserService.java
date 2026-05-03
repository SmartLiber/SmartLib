import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * 1. registerUser - 用户注册
     * 功能：注册新用户，验证用户名唯一性
     */
    public boolean registerUser(String username, String phone) {
        // 业务规则：用户名不能为空且不能重复
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        // 检查用户名是否已存在
        if (userDAO.getUserByUsername(username) != null) {
            return false;
        }

        // 业务规则：手机号格式校验（11位数字）
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
     * 功能：用户登录验证
     */
    public User loginUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        // 业务规则：用户名存在即登录成功
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
        // 业务规则：返回列表非空表示有未归还书籍
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
        // 业务规则：用户必须存在
        User existingUser = userDAO.getUserById(userId);
        if (existingUser == null) {
            return false;
        }

        // 业务规则：用户名不能与其他用户重复
        User userWithSameName = userDAO.getUserByUsername(username);
        if (userWithSameName != null && userWithSameName.getUserId() != userId) {
            return false;
        }

        // 手机号格式校验
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

}
