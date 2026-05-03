import java.util.List;
import java.util.Map;

public class BookServiceTest {
    public static void main(String[] args) {
        System.out.println("========== BookService 功能测试 ==========");
        
        BookService bookService = new BookService();
        
        System.out.println("\n--- 测试1: 添加分类 ---");
        boolean categoryAdded = bookService.addCategory("计算机科学");
        System.out.println("添加分类结果: " + (categoryAdded ? "成功" : "失败"));
        
        System.out.println("\n--- 测试2: 添加书籍 ---");
        boolean bookAdded = bookService.addNewBook("Java编程思想", "Bruce Eckel", 1, "Java经典著作");
        System.out.println("添加书籍结果: " + (bookAdded ? "成功" : "失败"));
        
        System.out.println("\n--- 测试3: 搜索书籍 ---");
        List<Book> searchResults = bookService.searchBooks("Java");
        System.out.println("搜索结果数量: " + searchResults.size());
        for (Book book : searchResults) {
            System.out.println("  - " + book.getName() + " by " + book.getAuthor());
        }
        
        System.out.println("\n--- 测试4: 获取书籍完整信息 ---");
        if (!searchResults.isEmpty()) {
            int bookId = searchResults.get(0).getId();
            Book fullBook = bookService.getBookFullInfo(bookId);
            if (fullBook != null) {
                System.out.println("书籍: " + fullBook.getName());
                System.out.println("作者: " + fullBook.getAuthor());
                System.out.println("状态: " + fullBook.getStatus());
                if (fullBook.getCategory() != null) {
                    System.out.println("分类: " + fullBook.getCategory().getCategoryName());
                }
            }
        }
        
        System.out.println("\n--- 测试5: 验证书籍可借状态 ---");
        if (!searchResults.isEmpty()) {
            int bookId = searchResults.get(0).getId();
            boolean available = bookService.isBookAvailable(bookId);
            System.out.println("书籍 " + bookId + " 可借状态: " + available);
        }
        
        System.out.println("\n--- 测试6: 获取分类及书籍 ---");
        Map<String, Object> categoryWithBooks = bookService.getCategoryWithBooks(1);
        if (categoryWithBooks != null) {
            BookCategory category = (BookCategory) categoryWithBooks.get("category");
            List<Book> books = (List<Book>) categoryWithBooks.get("books");
            System.out.println("分类: " + category.getCategoryName());
            System.out.println("书籍数量: " + categoryWithBooks.get("bookCount"));
        }
        
        System.out.println("\n--- 测试7: 更新书籍信息 ---");
        if (!searchResults.isEmpty()) {
            int bookId = searchResults.get(0).getId();
            boolean updated = bookService.modifyBookInfo(bookId, "Java编程思想(第4版)", "Bruce Eckel", 1, "Java经典著作第4版");
            System.out.println("更新书籍结果: " + (updated ? "成功" : "失败"));
        }
        
        System.out.println("\n--- 测试8: 借书功能 ---");
        if (!searchResults.isEmpty()) {
            int bookId = searchResults.get(0).getId();
            boolean borrowed = bookService.borrowBook(1, bookId);
            System.out.println("借书结果: " + (borrowed ? "成功" : "失败"));
            
            System.out.println("\n--- 测试9: 验证书籍状态变化 ---");
            boolean availableAfterBorrow = bookService.isBookAvailable(bookId);
            System.out.println("借出后可借状态: " + availableAfterBorrow);
            
            System.out.println("\n--- 测试10: 还书功能 ---");
            boolean returned = bookService.returnBook(bookId);
            System.out.println("还书结果: " + (returned ? "成功" : "失败"));
            
            System.out.println("\n--- 测试11: 还书后状态 ---");
            boolean availableAfterReturn = bookService.isBookAvailable(bookId);
            System.out.println("还书后可借状态: " + availableAfterReturn);
            
            System.out.println("\n--- 测试12: 续借功能 ---");
            boolean borrowedAgain = bookService.borrowBook(1, bookId);
            if (borrowedAgain) {
                boolean renewed = bookService.renewBook(bookId, 15);
                System.out.println("续借结果: " + (renewed ? "成功" : "失败"));
                
                System.out.println("\n--- 测试13: 归还续借的书籍 ---");
                boolean returnedRenewed = bookService.returnBook(bookId);
                System.out.println("归还续借书籍结果: " + (returnedRenewed ? "成功" : "失败"));
            }
        }
        
        System.out.println("\n--- 测试14: 删除书籍 ---");
        if (!searchResults.isEmpty()) {
            int bookId = searchResults.get(0).getId();
            boolean deleted = bookService.removeBook(bookId);
            System.out.println("删除书籍结果: " + (deleted ? "成功" : "失败"));
        }
        
        System.out.println("\n--- 测试15: 删除分类 ---");
        boolean categoryDeleted = bookService.removeCategory(1);
        System.out.println("删除分类结果: " + (categoryDeleted ? "成功" : "失败"));
        
        System.out.println("\n========== 测试完成 ==========");
    }
}
