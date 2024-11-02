// Import Statements
package com.onlineshop.controller;

import com.onlineshop.entity.BillDetail;
import com.onlineshop.entity.BillDetailForAdmin;
import com.onlineshop.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Class Definition
@Controller
@RequestMapping("/manageBill")
public class ManageBillController {

    @Autowired
    private BillService billService;

    // List All Bills
    @GetMapping("/listAllBills")
    public String listAllBills(Model model) {
        List<BillDetailForAdmin> billDetailForAdmins = billService.showBillDetailForAdmin();
        model.addAttribute("billDetailForAdmins", billDetailForAdmins);
        model.addAttribute("manageBill", "Yes");
        return "BillManager";
    }


    // Change Status of a Bill
    @GetMapping("/changeStatus")
    public String changeBillStatus(@RequestParam("billId") int billId,
                                   @RequestParam("newStatus") String newStatus,
                                   @RequestParam("statusInShowDetail") String statusInShowDetail,
                                   Model model) {
        if ("done".equals(statusInShowDetail)) {
            model.addAttribute("changeStatus", "Status of this bill is done, you cannot change it!");
        } else {
            String statusMessage = "";
            switch (newStatus.toLowerCase()) {
                case "wait":
                    billService.updateStatus("wait", billId);
                    statusMessage = "Admin changed status of Bill (ID = " + billId + ") to Wait";
                    break;
                case "process":
                    billService.updateStatus("process", billId);
                    statusMessage = "Admin changed status of Bill (ID = " + billId + ") to Process";
                    break;
                case "done":
                    billService.updateStatus("done", billId);
                    statusMessage = "Admin changed status of Bill (ID = " + billId + ") to Done";
                    break;
            }
            model.addAttribute("changeStatus", statusMessage);
        }
        return "redirect:/manageBill/listAll";
    }

    // Filter Bills by Status
    @GetMapping("/filterStatus")
    public String filterBillsByStatus(@RequestParam("filter") String filter, Model model) {
        List<BillDetailForAdmin> billDetailForAdmins;
        if ("all".equals(filter)) {
            billDetailForAdmins = billService.showBillDetailForAdmin();
        } else {
            billDetailForAdmins = billService.showBillDetailForAdminFilterByStatus(filter);
        }
        model.addAttribute("billDetailForAdmins", billDetailForAdmins);
        return "BillManager";
    }
}
