import java.util.List;
import java.util.Map;

public class UserServiceTest {
    public static void main(String[] args) {
        System.out.println("========== UserService 功能测试 ==========");
        
        UserService userService = new UserService();
        BookService bookService = new BookService();
        
        // ========== 第一部分：用户注册和登录测试 ==========
        System.out.println("\n--- 测试1: 用户注册 ---");
        boolean registerResult = userService.registerUser("测试用户张三", "13800138000");
        System.out.println("注册结果: " + (registerResult ? "成功" : "失败"));
        
        System.out.println("\n--- 测试2: 重复用户名注册（应该失败）---");
        boolean duplicateRegister = userService.registerUser("测试用户张三", "13900139000");
        System.out.println("重复注册结果: " + (duplicateRegister ? "成功" : "失败（符合预期）"));
        
        System.out.println("\n--- 测试3: 手机号格式错误（应该失败）---");
        boolean invalidPhone = userService.registerUser("测试用户李四", "12345");
        System.out.println("无效手机号注册结果: " + (invalidPhone ? "成功" : "失败（符合预期）"));
        
        System.out.println("\n--- 测试4: 用户登录 ---");
        User loggedInUser = userService.loginUser("测试用户张三");
        if (loggedInUser != null) {
            System.out.println("登录成功！用户ID: " + loggedInUser.getUserId() + 
                             ", 用户名: " + loggedInUser.getUsername() + 
                             ", 手机号: " + loggedInUser.getPhone());
        } else {
            System.out.println("登录失败");
        }
        
        // ========== 第二部分：准备测试数据 ==========
        System.out.println("\n--- 测试5: 添加测试分类 ---");
        boolean categoryAdded = bookService.addCategory("测试分类-文学");
        System.out.println("添加分类结果: " + (categoryAdded ? "成功" : "失败"));
        
        System.out.println("\n--- 测试6: 添加测试书籍 ---");
        boolean bookAdded = bookService.addNewBook("Java编程思想", "Bruce Eckel", 1, "Java经典著作");
        System.out.println("添加书籍结果: " + (bookAdded ? "成功" : "失败"));
        
        // 获取刚添加的书籍ID
        List<Book> searchResults = bookService.searchBooks("Java编程思想");
        int testBookId = 0;
        if (!searchResults.isEmpty()) {
            testBookId = searchResults.get(0).getId();
            System.out.println("测试书籍ID: " + testBookId);
        }
        
        // 获取测试用户ID
        User testUser = userService.loginUser("测试用户张三");
        int testUserId = 0;
        if (testUser != null) {
            testUserId = testUser.getUserId();
            System.out.println("测试用户ID: " + testUserId);
        }
        
        // ========== 第三部分：借书功能测试（协作功能）==========
        if (testUserId > 0 && testBookId > 0) {
            System.out.println("\n--- 测试7: 验证书籍可借状态 ---");
            boolean available = bookService.isBookAvailable(testBookId);
            System.out.println("书籍 " + testBookId + " 可借状态: " + available);
            
            System.out.println("\n--- 测试8: 检查用户是否有未还书籍 ---");
            boolean hasUnreturned = userService.hasUnreturnedBooks(testUserId);
            System.out.println("用户 " + testUserId + " 有未还书籍: " + hasUnreturned);
            
            System.out.println("\n--- 测试9: 使用 UserService 借书（协作功能）---");
            boolean borrowResult = userService.borrowBookWithValidation(testUserId, testBookId);
            System.out.println("借书结果: " + (borrowResult ? "成功" : "失败"));
            
            System.out.println("\n--- 测试10: 借书后再次验证状态 ---");
            boolean availableAfterBorrow = bookService.isBookAvailable(testBookId);
            System.out.println("借出后可借状态: " + availableAfterBorrow + " （应该是false）");
            
            System.out.println("\n--- 测试11: 查看用户借阅记录 ---");
            List<Book> borrowedBooks = userService.getUserBorrowHistory(testUserId);
            System.out.println("用户当前借阅书籍数量: " + borrowedBooks.size());
            for (Book book : borrowedBooks) {
                System.out.println("  - 《" + book.getName() + "》 by " + book.getAuthor());
            }
            
            System.out.println("\n--- 测试12: 尝试重复借同一本书（应该失败）---");
            boolean duplicateBorrow = userService.borrowBookWithValidation(testUserId, testBookId);
            System.out.println("重复借书结果: " + (duplicateBorrow ? "成功" : "失败（符合预期）"));
            
            // ========== 第四部分：还书功能测试（协作功能）==========
            System.out.println("\n--- 测试13: 使用 UserService 还书（协作功能）---");
            boolean returnResult = userService.returnBookWithValidation(testBookId);
            System.out.println("还书结果: " + (returnResult ? "成功" : "失败"));
            
            System.out.println("\n--- 测试14: 还书后验证状态 ---");
            boolean availableAfterReturn = bookService.isBookAvailable(testBookId);
            System.out.println("还书后可借状态: " + availableAfterReturn + " （应该是true）");
            
            System.out.println("\n--- 测试15: 还书后检查未还书籍 ---");
            boolean hasUnreturnedAfterReturn = userService.hasUnreturnedBooks(testUserId);
            System.out.println("还书后有未还书籍: " + hasUnreturnedAfterReturn + " （应该是false）");
            
            // ========== 第五部分：续借功能测试 ==========
            System.out.println("\n--- 测试16: 再次借书用于测试续借 ---");
            bookService.borrowBook(testUserId, testBookId);
            
            System.out.println("\n--- 测试17: 续借书籍 ---");
            boolean renewResult = bookService.renewBook(testBookId, 15);
            System.out.println("续借15天结果: " + (renewResult ? "成功" : "失败"));
            
            System.out.println("\n--- 测试18: 归还续借的书籍 ---");
            boolean returnRenewedResult = userService.returnBookWithValidation(testBookId);
            System.out.println("归还续借书籍结果: " + (returnRenewedResult ? "成功" : "失败"));
        }
        
        // ========== 第六部分：用户统计功能测试 ==========
        if (testUserId > 0) {
            System.out.println("\n--- 测试19: 获取用户统计信息 ---");
            Map<String, Object> stats = userService.getUserStatistics(testUserId);
            if (stats != null) {
                User user = (User) stats.get("user");
                System.out.println("用户ID: " + user.getUserId());
                System.out.println("用户名: " + user.getUsername());
                System.out.println("当前借阅数: " + stats.get("currentBorrowedCount"));
                System.out.println("有未还书籍: " + stats.get("hasUnreturned"));
            }
        }
        
        // ========== 第七部分：其他功能测试 ==========
        System.out.println("\n--- 测试20: 获取用户总数 ---");
        int totalUsers = userService.getTotalUserCount();
        System.out.println("注册用户总数: " + totalUsers);
        
        System.out.println("\n--- 测试21: 更新用户信息 ---");
        if (testUser != null) {
            boolean updateResult = userService.updateUserInfo(testUserId, "测试用户张三-改名", "13800138001");
            System.out.println("更新用户信息结果: " + (updateResult ? "成功" : "失败"));
            
            // 验证更新后的信息
            User updatedUser = userService.getUserById(testUserId);
            if (updatedUser != null) {
                System.out.println("更新后用户名: " + updatedUser.getUsername());
                System.out.println("更新后手机号: " + updatedUser.getPhone());
            }
        }
        
        System.out.println("\n--- 测试22: 获取所有用户列表 ---");
        List<User> allUsers = userService.getAllUsers();
        System.out.println("用户总数: " + allUsers.size());
        for (User u : allUsers) {
            System.out.println("  - ID: " + u.getUserId() + ", 姓名: " + u.getUsername() + ", 电话: " + u.getPhone());
        }
        
        // ========== 第八部分：清理测试数据 ==========
        System.out.println("\n========== 清理测试数据 ==========");
        
        if (testBookId > 0) {
            System.out.println("\n删除测试书籍（ID=" + testBookId + "）：");
            boolean bookDeleted = bookService.removeBook(testBookId);
            System.out.println("删除书籍结果: " + (bookDeleted ? "成功" : "失败"));
        }
        
        System.out.println("\n删除测试分类：");
        boolean categoryDeleted = bookService.removeCategory(1);
        System.out.println("删除分类结果: " + (categoryDeleted ? "成功" : "失败"));
        
        if (testUserId > 0) {
            System.out.println("\n删除测试用户（ID=" + testUserId + "）：");
            UserDAO userDAO = new UserDAO();
            boolean userDeleted = userDAO.deleteUser(testUserId);
            System.out.println("删除用户结果: " + (userDeleted ? "成功" : "失败"));
        }
        
        System.out.println("\n========== 测试完成 ==========");
    }
}
