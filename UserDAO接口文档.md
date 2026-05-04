# UserDAO 接口文档

## 创建 DAO

```java
UserDAO userDAO = new UserDAO();
```

---

## 1. 添加用户

```java
boolean addUser(User user)
```

**参数：**
- `user` - User 对象

**示例：**
```java
User user = new User();
user.setUsername("张三");
user.setPhone("13800138001");

boolean success = userDAO.addUser(user);
```

---

## 2. 更新用户

```java
boolean updateUser(User user)
```

**参数：**
- `user` - User 对象（必须包含 userId）

**示例：**
```java
User user = new User();
user.setUserId(1);
user.setUsername("李四");
user.setPhone("13800138002");

boolean success = userDAO.updateUser(user);
```

---

## 3. 删除用户

```java
boolean deleteUser(int userId)
```

**参数：**
- `userId` - 用户ID

**示例：**
```java
userDAO.deleteUser(1);
```

---

## 4. 根据ID查询用户

```java
User getUserById(int userId)
```

**示例：**
```java
User user = userDAO.getUserById(1);
if (user != null) {
    System.out.println(user.getUsername());
}
```

---

## 5. 根据用户名查询

```java
User getUserByUsername(String username)
```

**示例：**
```java
User user = userDAO.getUserByUsername("张三");
```

---

## 6. 查询所有用户

```java
List<User> getAllUsers()
```

**示例：**
```java
List<User> users = userDAO.getAllUsers();
for (User u : users) {
    System.out.println(u.getUsername());
}
```

---

## 7. 查询用户借阅的书籍

```java
List<Book> getBorrowedBooksByUser(int userId)
```

**示例：**
```java
List<Book> books = userDAO.getBorrowedBooksByUser(1);
for (Book b : books) {
    System.out.println(b.getName());
}
```

---

## 8. 借书

```java
boolean borrowBook(int userId, int bookId)
```

**参数：**
- `userId` - 用户ID
- `bookId` - 书籍ID

**说明：** 将书籍状态改为"已借出"，记录借阅人和借阅时间

**示例：**
```java
boolean success = userDAO.borrowBook(1, 1);  // 用户1借书籍1
```

---

## 9. 还书

```java
boolean returnBook(int bookId)
```

**参数：**
- `bookId` - 书籍ID

**说明：** 将书籍状态改为"可借阅"，清空借阅人，记录归还时间

**示例：**
```java
boolean success = userDAO.returnBook(1);  // 归还书籍1
```

---

## User 对象

```java
user.setUsername("用户名");
user.setPhone("13800138001");
```

---

## 完整示例

```java
public class Test_sql {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        // 添加用户
        User user = new User();
        user.setUsername("张三");
        user.setPhone("13800138001");
        userDAO.addUser(user);

        // 查询用户
        User found = userDAO.getUserByUsername("张三");
        System.out.println("找到用户: " + found.getUsername());

        // 查询借阅记录
        List<Book> borrowed = userDAO.getBorrowedBooksByUser(found.getUserId());
        System.out.println("借阅书籍数: " + borrowed.size());

        // 借书
        userDAO.borrowBook(found.getUserId(), 1);

        // 还书
        userDAO.returnBook(1);
    }
}
```
