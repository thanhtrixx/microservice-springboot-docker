package tri.le.purchasedata.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {@Index(name = "userididx", columnList = "userid")})
public class DataVoucher {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long userId;

  private String voucher;

  @CreationTimestamp
  private LocalDateTime createTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getVoucher() {
    return voucher;
  }

  public void setVoucher(String voucher) {
    this.voucher = voucher;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }
}
