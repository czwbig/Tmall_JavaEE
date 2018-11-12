package tmall.bean;

import java.util.Date;

public class Review {
    private int id; //对应数据库字段 id
    private String content; //对应数据库字段 content，表示评价内容
    private Date createDate; //对应数据库字段 createDate，表示评价时间
    private User user; //数据库字段 uid，外键约束 user 表的 id，评价和用户是多对一
    private Product product; //数据库字段 pid，外键约束 product 表的 id，评价和产品是多对一

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
