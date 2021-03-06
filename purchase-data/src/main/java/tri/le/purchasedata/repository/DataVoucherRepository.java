package tri.le.purchasedata.repository;

import org.springframework.data.repository.CrudRepository;
import tri.le.purchasedata.entity.DataVoucher;

public interface DataVoucherRepository extends CrudRepository<DataVoucher, Long> {

  Iterable<DataVoucher> findByUserId(Long userId);

}
