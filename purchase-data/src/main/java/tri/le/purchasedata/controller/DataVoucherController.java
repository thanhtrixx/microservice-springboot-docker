package tri.le.purchasedata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tri.le.purchasedata.entity.DataVoucher;
import tri.le.purchasedata.repository.DataVoucherRepository;

@RestController
@RequestMapping(path = "/data-voucher")
public class DataVoucherController {

  @Autowired
  private DataVoucherRepository dataVoucherRepository;

  @GetMapping(path = "/purchase")
  public @ResponseBody
  Iterable<DataVoucher> getAllUsers() {
    return dataVoucherRepository.findAll();
  }
}
