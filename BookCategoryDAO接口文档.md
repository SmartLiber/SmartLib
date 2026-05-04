# BookCategoryDAO 接口文档

## 创建 DAO

```java
BookCategoryDAO categoryDAO = new BookCategoryDAO();
```

---

## 1. 添加分类

```java
boolean addCategory(BookCategory category)
```

**参数：**
- `category` - BookCategory 对象

**示例：**
```java
BookCategory category = new BookCategory();
category.setCategoryName("科技编程");

boolean success = categoryDAO.addCategory(category);
```

---

## 2. 更新分类

```java
boolean updateCategory(BookCategory category)
```

**参数：**
- `category` - BookCategory 对象（必须包含 categoryId）

**示例：**
```java
BookCategory category = new BookCategory();
category.setCategoryId(1);
category.setCategoryName("计算机编程");

boolean success = categoryDAO.updateCategory(category);
```

---

## 3. 删除分类

```java
boolean deleteCategory(int categoryId)
```

**参数：**
- `categoryId` - 分类ID

**示例：**
```java
categoryDAO.deleteCategory(1);
```

---

## 4. 根据ID查询分类

```java
BookCategory getCategoryById(int categoryId)
```

**示例：**
```java
BookCategory category = categoryDAO.getCategoryById(1);
if (category != null) {
    System.out.println(category.getCategoryName());
}
```

---

## 5. 根据名称查询分类

```java
BookCategory getCategoryByName(String categoryName)
```

**示例：**
```java
BookCategory category = categoryDAO.getCategoryByName("科技编程");
```

---

## 6. 查询所有分类

```java
List<BookCategory> getAllCategories()
```

**示例：**
```java
List<BookCategory> categories = categoryDAO.getAllCategories();
for (BookCategory c : categories) {
    System.out.println(c.getCategoryId() + ". " + c.getCategoryName());
}
```

---

## 7. 查询分类下的书籍

```java
List<Book> getBooksByCategory(int categoryId)
```

**参数：**
- `categoryId` - 分类ID

**示例：**
```java
List<Book> books = categoryDAO.getBooksByCategory(1);
for (Book b : books) {
    System.out.println(b.getName());
}
```

---

## BookCategory 对象

```java
category.setCategoryName("分类名称");
```

---

## 完整示例

```java
public class Test_sql {
    public static void main(String[] args) {
        BookCategoryDAO categoryDAO = new BookCategoryDAO();

        // 添加分类
        BookCategory category = new BookCategory();
        category.setCategoryName("测试分类");
        categoryDAO.addCategory(category);

        // 查询所有分类
        List<BookCategory> categories = categoryDAO.getAllCategories();
        for (BookCategory c : categories) {
            System.out.println(c.getCategoryId() + ". " + c.getCategoryName());
        }

        // 查询分类下的书籍
        BookCategory found = categoryDAO.getCategoryByName("测试分类");
        if (found != null) {
            List<Book> books = categoryDAO.getBooksByCategory(found.getCategoryId());
            System.out.println("该分类下有 " + books.size() + " 本书");
        }

        // 删除测试分类
        if (found != null) {
            categoryDAO.deleteCategory(found.getCategoryId());
        }
    }
}
```
