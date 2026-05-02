public class Book {
    private Integer id;
    private String name;
    private String author;
    private Integer categoryId;
    private String introduction;
    private String status;
    private Integer borrowUserId;
    private String borrowDate;
    private String returnDate;
    private String createTime;
    private String updateTime;

    public Book() {
    }

    public Book(Integer id, String name, String author, Integer categoryId, String introduction, String status, 
                Integer borrowUserId, String borrowDate, String returnDate, String createTime, String updateTime) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.categoryId = categoryId;
        this.introduction = introduction;
        this.status = status;
        this.borrowUserId = borrowUserId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBorrowUserId() {
        return borrowUserId;
    }

    public void setBorrowUserId(Integer borrowUserId) {
        this.borrowUserId = borrowUserId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}