package tri.le.purchasedata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tri.le.purchasedata.dto.GenericResponse;
import tri.le.purchasedata.entity.DataVoucher;
import tri.le.purchasedata.service.PurchaseDataService;

@RestController
@RequestMapping(path = "/purchase-data")
public class PurchaseDataController {

  @Autowired
  private PurchaseDataService purchaseDataService;

  @PostMapping(path = "/")
  public @ResponseBody
  GenericResponse<String> purchaseDataVoucher() {

    return purchaseDataService.purchaseDataVoucher();
  }

  @GetMapping(path = "/")
  public @ResponseBody
  GenericResponse<Iterable<DataVoucher>> getDataVouchersPurchased() {

    return purchaseDataService.getDataVouchersPurchased();
  }
}
