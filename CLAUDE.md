# SmartLib - 图书馆管理系统

## 项目概述

纯 Java Swing + MySQL 图书管理系统，支持用户注册登录、图书浏览搜索、借书还书、后台管理等功能。

## 技术栈

- **语言**: Java (JDK 21)
- **UI框架**: Swing
- **数据库**: MySQL (book_java_sys)
- **JDBC驱动**: mysql-connector-j-9.3.0.jar (位于 `lib/`)
- **构建**: 无 Maven/Gradle，手动 javac 编译

## 编译与运行

```bash
# 编译（class文件输出到 build/ 目录）
javac -encoding UTF-8 -d build -cp "lib/mysql-connector-j-9.3.0.jar" src/*.java

# 运行
java -cp "build;lib/mysql-connector-j-9.3.0.jar" Main
```

编译输出目录 `build/` 已加入 `.gitignore`，不会提交到仓库。

数据库配置在 [DBUtil.java](src/DBUtil.java):
- URL: `jdbc:mysql://localhost:3306/book_java_sys`
- 用户: `root` / 密码: `123456`

## 项目结构

```
SmartLib/
  src/                    # 所有源代码（默认包）
  lib/                    # JDBC驱动
  CLAUDE.md               # 本文档
  README.md
  LICENSE
```

所有类位于默认包（无 package 声明），共 25 个 `.java` 文件。

## 分层架构

```
┌─────────────────────────────────────────┐
│  UI Layer (Swing JFrame)                │
│  LoginFrame -> MainFrame -> 各功能Frame   │
├─────────────────────────────────────────┤
│  Service Layer (业务逻辑)                 │
│  UserService / BookService               │
├─────────────────────────────────────────┤
│  DAO Layer (数据访问)                     │
│  UserDAO / BookDAO / BookCategoryDAO     │
├─────────────────────────────────────────┤
│  DB Layer                                │
│  DBUtil.getConnection() -> MySQL         │
└─────────────────────────────────────────┘
```

## UI 页面清单（按启动流程）

| 页面 | 文件 | 功能 | 状态 |
|------|------|------|------|
| **登录** | [LoginFrame.java](src/LoginFrame.java) | 用户名登录，跳转注册 | 完成 |
| **注册** | [RegisterFrame.java](src/RegisterFrame.java) | 用户名+手机号注册 | 完成 |
| **主界面** | [MainFrame.java](src/MainFrame.java) | 菜单栏导航中心 | 完成 |
| **浏览图书** | [BookListFrame.java](src/BookListFrame.java) | 全部图书表格，分类筛选，搜索，借阅/归还 | 完成 |
| **图书详情** | [BookDetailFrame.java](src/BookDetailFrame.java) | 完整信息+借阅/归还/续借操作 | 完成 |
| **我的借阅** | [BorrowHistoryFrame.java](src/BorrowHistoryFrame.java) | 当前借阅图书列表+归还 | 完成 |
| **未还书籍** | [UnreturnedBooksFrame.java](src/UnreturnedBooksFrame.java) | 未还图书查看+状态检查 | 完成 |
| **用户详情** | [UserDetailFrame.java](src/UserDetailFrame.java) | 查看用户信息 | 完成 |
| **编辑信息** | [EditUserInfoFrame.java](src/EditUserInfoFrame.java) | 修改用户名/手机号 | 完成 |
| **用户列表** | [UserListFrame.java](src/UserListFrame.java) | 管理员查看所有用户 | 完成 |
| **后台管理** | [AdminFrame.java](src/AdminFrame.java) | 图书CRUD + 分类管理（标签页） | 完成 |
| **AI助手** | [AiAgentUI.java](src/AiAgentUI.java) | Ollama AI 聊天界面 | 完成 |

## 业务逻辑层 API

### UserService (11个方法)

| 方法 | 说明 | 验证规则 |
|------|------|----------|
| `registerUser(name, phone)` | 用户注册 | 用户名不重复，手机号11位数字 |
| `loginUser(username)` | 用户登录 | 用户名存在即通过 |
| `getUserBorrowHistory(userId)` | 获取当前借阅 | 返回借阅中的图书列表 |
| `hasUnreturnedBooks(userId)` | 是否有未还书 | - |
| `getTotalUserCount()` | 用户总数 | - |
| `updateUserInfo(userId, name, phone)` | 更新用户信息 | 用户名不冲突，手机号格式 |
| `getUserById(userId)` | 单用户查询 | - |
| `getAllUsers()` | 所有用户列表 | - |
| `borrowBookWithValidation(userId, bookId)` | 借书(带校验) | 用户存在+书可借+无未还书 |
| `returnBookWithValidation(bookId)` | 还书(带校验) | 书存在+状态为已借出 |
| `getUserStatistics(userId)` | 用户统计 | 返回用户信息+借阅统计 |

### BookService (12个方法)

| 方法 | 说明 |
|------|------|
| `addNewBook(name, author, catId, intro)` | 添加图书，默认状态"可借阅" |
| `removeBook(bookId)` | 删除图书，仅"可借阅"状态可删 |
| `modifyBookInfo(bookId, ...)` | 更新图书信息 |
| `searchBooks(keyword)` | 按书名/作者模糊搜索 |
| `getBookFullInfo(bookId)` | 完整信息（含分类和借阅人） |
| `borrowBook(userId, bookId)` | 借书 |
| `returnBook(bookId)` | 还书 |
| `renewBook(bookId, days)` | 续借（1-30天） |
| `addCategory(name)` | 添加分类 |
| `removeCategory(catId)` | 删除分类（分类下有书则拒绝） |
| `getCategoryWithBooks(catId)` | 获取分类及其图书 |
| `isBookAvailable(bookId)` | 检查是否可借 |

## 数据库表结构

```sql
-- book 图书表
book (id INT PK AUTO_INCREMENT, name VARCHAR, author VARCHAR,
      category_id INT, introduction TEXT, status VARCHAR,
      borrow_user_id INT, borrow_date DATETIME, return_date DATETIME,
      create_time DATETIME, update_time DATETIME)

-- book_category 分类表
book_category (category_id INT PK AUTO_INCREMENT, category_name VARCHAR)

-- sys_user 用户表
sys_user (user_id INT PK AUTO_INCREMENT, username VARCHAR, phone VARCHAR)
```

图书状态流转: `可借阅` -> (借书) -> `已借出` -> (还书) -> `可借阅`

## 已知限制

1. **无密码系统**: 登录仅靠用户名匹配，无密码验证
2. **无借阅历史**: 还书后借阅记录丢失（数据库只存当前借阅人），无法追溯历史
3. **时间戳为字符串**: 所有日期用 String 存储而非 DATE/DATETIME 类型
4. **平面包结构**: 所有类在默认包，无 package 声明
5. **renewBook 未完全实现**: 续借方法仅更新 updateTime，未真正延长借阅期限（因无 due_date 字段）
6. **单次借一本书**: 用户有未还书时不能借新书

## 编码规范

- 所有 UI 使用**微软雅黑**字体
- 所有注释和界面文本使用**中文**
- Swing 使用系统原生 LookAndFeel
- 窗口关闭行为：主窗口 EXIT_ON_CLOSE，子窗口 DISPOSE_ON_CLOSE
- 按钮命名：中文动词短语（如"借阅此书""归还"）
