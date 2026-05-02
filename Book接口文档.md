# BookDAO 接口文档

## 概述

BookDAO 是书籍数据的访问对象，提供对数据库中书籍表（book）的增删改查操作，以及与分类、用户表的关联查询。

---

## 创建实例

```java
BookDAO bookDAO = new BookDAO();
```

---

## 方法列表

### 1. 添加书籍

```java
boolean addBook(Book book)
```

**参数：**
- `book` - Book 对象（必填字段：name, author）

**返回：**
- `true` - 添加成功
- `false` - 添加失败

**示例：**
```java
Book book = new Book();
book.setName("Java核心技术");
book.setAuthor("Cay Horstmann");
book.setCategoryId(2);
book.setIntroduction("Java经典教材");
book.setStatus("可借阅");

BookDAO dao = new BookDAO();
boolean success = dao.addBook(book);
System.out.println(success ? "添加成功" : "添加失败");
```

---

### 2. 更新书籍

```java
boolean updateBook(Book book)
```

**参数：**
- `book` - Book 对象（必须包含 id）

**返回：**
- `true` - 更新成功
- `false` - 更新失败

**示例：**
```java
Book book = new Book();
book.setId(1);
book.setName("Java核心技术卷I");
book.setAuthor("Cay Horstmann");
book.setCategoryId(2);
book.setIntroduction("更新后的简介");
book.setStatus("可借阅");
book.setUpdateTime("2026-05-02 12:00:00");

BookDAO dao = new BookDAO();
boolean success = dao.updateBook(book);
```

---

### 3. 删除书籍

```java
boolean deleteBook(int id)
```

**参数：**
- `id` - 书籍ID

**返回：**
- `true` - 删除成功
- `false` - 删除失败

**示例：**
```java
BookDAO dao = new BookDAO();
boolean success = dao.deleteBook(1);
```

---

### 4. 根据ID查询书籍

```java
Book getBookById(int id)
```

**参数：**
- `id` - 书籍ID

**返回：**
- `Book` - 书籍对象
- `null` - 未找到

**示例：**
```java
BookDAO dao = new BookDAO();
Book book = dao.getBookById(1);
if (book != null) {
    System.out.println(book.getName());
    System.out.println(book.getAuthor());
} else {
    System.out.println("书籍不存在");
}
```

---

### 5. 查询书籍完整信息（包含关联表）

```java
Book getBookDetailById(int bookId)
```

**说明：**
- 使用 LEFT JOIN 同时查询 book、book_category、sys_user 三个表
- 返回的 Book 对象包含 category 和 borrowUser 关联字段

**参数：**
- `bookId` - 书籍ID

**返回：**
- `Book` - 书籍对象（包含关联信息）
- `null` - 未找到

**示例：**
```java
BookDAO dao = new BookDAO();
Book book = dao.getBookDetailById(1);
if (book != null) {
    System.out.println("书籍ID: " + book.getId());
    System.out.println("书名: " + book.getName());
    System.out.println("作者: " + book.getAuthor());
    System.out.println("状态: " + book.getStatus());
    if (book.getCategory() != null) {
        System.out.println("分类: " + book.getCategory().getCategoryName());
    }
    if (book.getBorrowUser() != null) {
        System.out.println("借阅人: " + book.getBorrowUser().getUsername());
    }
}
```

---

### 6. 查询所有书籍

```java
List<Book> getAllBooks()
```

**参数：**
- 无

**返回：**
- `List<Book>` - 书籍列表（可能为空）

**示例：**
```java
BookDAO dao = new BookDAO();
List<Book> books = dao.getAllBooks();

for (Book book : books) {
    System.out.println(book.getId() + ". " + book.getName());
}
```

---

### 7. 按状态查询书籍

```java
List<Book> getBooksByStatus(String status)
```

**参数：**
- `status` - 书籍状态（"可借阅" / "已借出" / "维护中"）

**返回：**
- `List<Book>` - 符合状态的书籍列表

**示例：**
```java
BookDAO dao = new BookDAO();
List<Book> availableBooks = dao.getBooksByStatus("可借阅");

for (Book book : availableBooks) {
    System.out.println(book.getName());
}
```

---

### 8. 搜索书籍

```java
List<Book> searchBooks(String keyword)
```

**参数：**
- `keyword` - 搜索关键词（书名或作者）

**返回：**
- `List<Book>` - 匹配的书籍列表

**示例：**
```java
BookDAO dao = new BookDAO();
List<Book> results = dao.searchBooks("Java");

for (Book book : results) {
    System.out.println(book.getName() + " - " + book.getAuthor());
}
```

---

## Book 对象结构

```java
Book {
    Integer id;              // 书籍ID（主键）
    String name;             // 书名
    String author;           // 作者
    Integer categoryId;      // 分类ID
    String introduction;     // 简介
    String status;           // 状态（可借阅/已借出/维护中）
    Integer borrowUserId;    // 借阅人ID
    String borrowDate;       // 借阅日期
    String returnDate;       // 归还日期
    String createTime;       // 创建时间
    String updateTime;       // 更新时间
    // 关联字段（由 getBookDetailById 填充）
    BookCategory category;    // 书籍分类对象
    User borrowUser;         // 借阅人对象
}
```

### 状态说明

| 状态值 | 说明 |
|--------|------|
| `"可借阅"` | 书籍可被借阅 |
| `"已借出"` | 已被借出 |
| `"维护中"` | 正在维护，不可借阅 |

### 关联字段说明

- `category` - 书籍分类对象，仅由 `getBookDetailById` 填充
- `borrowUser` - 借阅人对象，仅由 `getBookDetailById` 填充

---

## 注意事项

1. 所有时间字段格式：`"YYYY-MM-DD HH:MM:SS"`
2. `categoryId`、`borrowUserId` 为外键，传入 `null` 表示无关联
3. `addBook` 和 `updateBook` 中的时间字段由应用层处理，数据库不自动生成
4. 使用完记得关闭资源（已通过 try-with-resources 自动处理）
5. `getBookDetailById` 使用 LEFT JOIN，即使没有分类或借阅人也会返回书籍基本信息

---

## 错误处理

所有方法遇到异常时会：
- 打印异常堆栈 `e.printStackTrace()`
- 返回 `false`（boolean方法）或 `null`（对象方法）或空列表（List方法）

如需更详细的错误处理，可在调用处用 try-catch 捕获 `SQLException`。
