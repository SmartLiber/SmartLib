# JDBC (Java Database Connectivity) 教程

## 什么是 JDBC？

JDBC 是 Java 连接数据库的标准 API，用于执行 SQL 语句和操作数据库。

---

## 1. 核心接口和类

| 类/接口 | 用途 |
|---------|------|
| `Connection` | 数据库连接对象 |
| `Statement` | 静态 SQL 语句执行 |
| `PreparedStatement` | 预编译 SQL 语句执行（推荐） |
| `ResultSet` | 查询结果集 |
| `DriverManager` | 加载驱动、获取连接 |

---

## 2. 基本步骤

### 步骤 1：加载驱动（现代 JDBC 不需要手动加载）

```java
// JDBC 4.0+ 会自动注册驱动，不需要这行
// Class.forName("com.mysql.cj.jdbc.Driver");
```

### 步骤 2：获取连接

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

String url = "jdbc:mysql://localhost:3306/数据库名?useSSL=false&serverTimezone=UTC";
String user = "用户名";
String password = "密码";

Connection conn = DriverManager.getConnection(url, user, password);
```

### 步骤 3：执行 SQL

**方式 A：Statement（静态 SQL）

```java
import java.sql.Statement;
import java.sql.ResultSet;

Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM book");
while (rs.next()) {
    System.out.println(rs.getString("name"));
}
```

**方式 B：PreparedStatement（推荐，防止 SQL 注入）

```java
import java.sql.PreparedStatement;
import java.sql.ResultSet;

String sql = "SELECT * FROM book WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, 1);  // 设置第 1 个 ? 的值
ResultSet rs = pstmt.executeQuery();
```

### 步骤 4：处理结果

```java
while (rs.next()) {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    System.out.println(id + ": " + name);
}
```

### 步骤 5：关闭资源

```java
rs.close();
pstmt.close();
conn.close();
```

**推荐使用 try-with-resources（自动关闭）：

```java
try (Connection conn = DriverManager.getConnection(url, user, password);
     PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {
    // 使用 rs
} catch (SQLException e) {
    e.printStackTrace();
}
```

---

## 3. 常用操作

### 查询（SELECT）

```java
String sql = "SELECT * FROM book WHERE status = ?";
try (Connection conn = DBUtil.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, "可借阅");
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
        System.out.println(rs.getString("name"));
    }
}
```

### 插入（INSERT）

```java
String sql = "INSERT INTO book(name, author) VALUES(?, ?)";
try (Connection conn = DBUtil.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, "Java核心技术");
    pstmt.setString(2, "Cay S. Horstmann");
    int rows = pstmt.executeUpdate();  // 返回影响的行数
    System.out.println(rows + " 行插入成功");
}
```

### 更新（UPDATE）

```java
String sql = "UPDATE book SET status = ? WHERE id = ?";
try (Connection conn = DBUtil.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, "已借出");
    pstmt.setInt(2, 1);
    pstmt.executeUpdate();
}
```

### 删除（DELETE）

```java
String sql = "DELETE FROM book WHERE id = ?";
try (Connection conn = DBUtil.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setInt(1, 1);
    pstmt.executeUpdate();
}
```

---

## 4. ResultSet 常用方法

| 方法 | 用途 |
|------|------|
| `rs.next()` | 移动到下一行，返回 boolean |
| `rs.getInt("列名")` | 获取 int 值 |
| `rs.getString("列名")` | 获取 String 值 |
| `rs.getDate("列名")` | 获取 Date 值 |
| `rs.wasNull()` | 检查上一列是否为 NULL |

---

## 5. 完整示例

```java
import java.sql.*;

public class JdbcDemo {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/book_java_sys?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        
        String sql = "SELECT * FROM book";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("书名: " + rs.getString("name"));
                System.out.println("作者: " + rs.getString("author"));
                System.out.println("---");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 6. URL 参数说明

```
jdbc:mysql://localhost:3306/数据库名
    ?useSSL=false           // 不使用 SSL（开发环境）
    &serverTimezone=UTC    // 时区
    &characterEncoding=utf8  // 编码
```

---

## 7. 常见问题

| 错误 | 原因 |
|------|------|
| ClassNotFoundException | 驱动没加 |
| Unknown database 'xxx' | 数据库不存在 |
| Access denied | 用户名或密码错 |
| No suitable driver | URL 格式错或驱动没加载 |
