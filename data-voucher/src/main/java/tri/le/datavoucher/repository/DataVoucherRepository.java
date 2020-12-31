package tri.le.datavoucher.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tri.le.datavoucher.entity.DataVoucherEntity;

@Repository
public interface DataVoucherRepository extends CrudRepository<DataVoucherEntity, String> {

}
