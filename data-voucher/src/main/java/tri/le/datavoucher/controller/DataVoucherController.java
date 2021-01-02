package tri.le.datavoucher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tri.le.datavoucher.dto.GenericResponse;
import tri.le.datavoucher.service.DataVoucherService;

@RestController
public class DataVoucherController {

  @Autowired
  private DataVoucherService dataVoucherService;

  @PostMapping(path = "/get-data-voucher")
  public @ResponseBody
  GenericResponse<String> getDataVoucher(@RequestParam("request-id") String requestId) {
    return dataVoucherService.getDataVoucher(requestId);
  }

  @PostMapping(path = "/get-data-voucher-by-request-id")
  public @ResponseBody
  GenericResponse<String> getDataVoucherByRequestId(@RequestParam("request-id") String requestId) {
    return dataVoucherService.getDataVoucherByRequestId(requestId);
  }

}
