package com.techpower.airbnb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techpower.airbnb.service.VnPayService;

import jakarta.servlet.http.HttpServletRequest;

@org.springframework.stereotype.Controller
public class VnPayController {

	@Autowired
	VnPayService vnPayService;

	@GetMapping("/hihi")
	public String home() {
		return "index";
	}

	@PostMapping("/submitOrder")
	public String submidOrder(@RequestParam("amount") int orderTotal, @RequestParam("orderInfo") String orderInfo,
			HttpServletRequest request) {
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		System.out.println(baseUrl);
		String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
		System.out.println(vnpayUrl);
		return "redirect:" + vnpayUrl;
	}

	@GetMapping("/vnpay-payment")
	public String GetMapping(HttpServletRequest request, Model model) {
		int paymentStatus = vnPayService.orderReturn(request);
		System.out.println("status: " + paymentStatus);
		String orderInfo = request.getParameter("vnp_OrderInfo");
		String paymentTime = request.getParameter("vnp_PayDate");
		String transactionId = request.getParameter("vnp_TransactionNo");
		String totalPrice = request.getParameter("vnp_Amount");

		model.addAttribute("orderId", orderInfo);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("paymentTime", paymentTime);
		model.addAttribute("transactionId", transactionId);

		return paymentStatus == 1 ? "ordersuccess" : "orderfail";
	}
}
