package tri.le.datavoucher.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Objects;

@RedisHash(value = "data-voucher")
public class DataVoucherEntity {

  @Id
  private String id;

  private String voucher;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getVoucher() {
    return voucher;
  }

  public void setVoucher(String voucher) {
    this.voucher = voucher;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DataVoucherEntity)) return false;
    DataVoucherEntity that = (DataVoucherEntity) o;
    return Objects.equals(getId(), that.getId()) &&
      Objects.equals(getVoucher(), that.getVoucher());
  }

}
