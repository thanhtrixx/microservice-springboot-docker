package tri.le.purchasedata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tri.le.purchasedata.dto.GenericResponse;
import tri.le.purchasedata.entity.DataVoucher;
import tri.le.purchasedata.service.DataVoucherService;

@RestController
@RequestMapping(path = "/purchase-data")
public class PurchaseDataController {

  @Autowired
  private DataVoucherService dataVoucherService;

  @PostMapping(path = "/")
  public @ResponseBody
  GenericResponse<String> purchaseDataVoucher() {

    return dataVoucherService.purchaseDataVoucher();
  }

  @GetMapping(path = "/")
  public @ResponseBody
  GenericResponse<Iterable<DataVoucher>> getDataVouchersPurchased() {

    return dataVoucherService.getDataVouchersPurchased();
  }
}
