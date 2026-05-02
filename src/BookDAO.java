import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public boolean addBook(Book book) {
        String sql = "INSERT INTO book(name, author, category_id, introduction, status, borrow_user_id, borrow_date, return_date, create_time, update_time) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getCategoryId());
            pstmt.setString(4, book.getIntroduction());
            pstmt.setString(5, book.getStatus());
            if (book.getBorrowUserId() != null) {
                pstmt.setInt(6, book.getBorrowUserId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            pstmt.setString(7, book.getBorrowDate());
            pstmt.setString(8, book.getReturnDate());
            pstmt.setString(9, book.getCreateTime());
            pstmt.setString(10, book.getUpdateTime());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(Book book) {
        String sql = "UPDATE book SET name=?, author=?, category_id=?, introduction=?, status=?, borrow_user_id=?, borrow_date=?, return_date=?, update_time=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getCategoryId());
            pstmt.setString(4, book.getIntroduction());
            pstmt.setString(5, book.getStatus());
            if (book.getBorrowUserId() != null) {
                pstmt.setInt(6, book.getBorrowUserId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            pstmt.setString(7, book.getBorrowDate());
            pstmt.setString(8, book.getReturnDate());
            pstmt.setString(9, book.getUpdateTime());
            pstmt.setInt(10, book.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(int id) {
        String sql = "DELETE FROM book WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Book getBookById(int id) {
        String sql = "SELECT * FROM book WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getBooksByStatus(String status) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE status=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE name LIKE ? OR author LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setName(rs.getString("name"));
        book.setAuthor(rs.getString("author"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setIntroduction(rs.getString("introduction"));
        book.setStatus(rs.getString("status"));
        int borrowUserId = rs.getInt("borrow_user_id");
        book.setBorrowUserId(rs.wasNull() ? null : borrowUserId);
        book.setBorrowDate(rs.getString("borrow_date"));
        book.setReturnDate(rs.getString("return_date"));
        book.setCreateTime(rs.getString("create_time"));
        book.setUpdateTime(rs.getString("update_time"));
        return book;
    }

    public Book getBookDetailById(int bookId) {
        String sql = "SELECT b.*, c.category_id, c.category_name, u.user_id, u.username, u.phone " +
                     "FROM book b " +
                     "LEFT JOIN book_category c ON b.category_id = c.category_id " +
                     "LEFT JOIN sys_user u ON b.borrow_user_id = u.user_id " +
                     "WHERE b.id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Book book = mapResultSetToBook(rs);
                if (rs.getInt("category_id") > 0) {
                    BookCategory category = new BookCategory();
                    category.setCategoryId(rs.getInt("category_id"));
                    category.setCategoryName(rs.getString("category_name"));
                    book.setCategory(category);
                }
                if (rs.getInt("user_id") > 0) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPhone(rs.getString("phone"));
                    book.setBorrowUser(user);
                }
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}