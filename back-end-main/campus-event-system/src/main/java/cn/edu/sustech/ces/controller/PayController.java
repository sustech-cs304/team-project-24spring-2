package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.OrderStatus;
import cn.edu.sustech.ces.enums.PurchaseMethod;
import cn.edu.sustech.ces.service.GlobalSettingService;
import cn.edu.sustech.ces.service.MailService;
import cn.edu.sustech.ces.service.OrderService;
import cn.edu.sustech.ces.service.UserService;
import cn.edu.sustech.ces.service.alipay.AlipayConfig;
import cn.edu.sustech.ces.utils.CESUtils;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pay")
public class PayController {

    private final OrderService orderService;

    private final AlipayConfig alipayConfig;

    private final GlobalSettingService globalSettingService;

    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public PayController(OrderService orderService, AlipayConfig alipayConfig, GlobalSettingService globalSettingService, UserService userService, MailService mailService) {
        this.orderService = orderService;
        this.alipayConfig = alipayConfig;
        this.globalSettingService = globalSettingService;
        this.userService = userService;
        this.mailService = mailService;
    }

    @PostMapping("/pay-order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> payOrder(@RequestParam UUID orderId, @RequestParam PurchaseMethod purchaseMethod, HttpServletResponse response) throws IOException, AlipayApiException {

        User user = CESUtils.getAuthorizedUser();
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return ResponseEntity.badRequest().body("Order not found");
        }
        if (!order.getPayerId().equals(user.getId())) {
            return ResponseEntity.badRequest().body("You are not the payer");
        }
        if (order.getStatus() != OrderStatus.UNPAID) {
            return ResponseEntity.badRequest().body("Order has been paid");
        }
        if (order.getPrice() == 0) {
            completeOrder(user, order, PurchaseMethod.CASH);
            return ResponseEntity.ok("Order paid successfully");
        }
        if (purchaseMethod == PurchaseMethod.ALIPAY) {
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getAlipayGatewayUrl(),
                    alipayConfig.getAppId(),
                    alipayConfig.getAppPrivateKey(),
                    alipayConfig.getFormat(),
                    alipayConfig.getCharset(),
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getSignType());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("out_trade_no", order.getId());
            jsonObject.put("product_code", "FAST_INSTANT_TRADE_PAY");
            jsonObject.put("total_amount", order.getPrice());
            jsonObject.put("subject", order.getName());
            jsonObject.put("body", order.getTicketId());
            long expire_time = order.getOrderCreateTime() + Long.parseLong(globalSettingService.getSetting("order_expire_time")) - 1000 * 2;
            jsonObject.put("time_expire", CESUtils.formatTime(expire_time));

            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setNotifyUrl(alipayConfig.getAlipayNotifyUrl());

            alipayRequest.setBizContent(jsonObject.toString());

            String form = "";
            form = alipayClient.pageExecute(alipayRequest).getBody();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "text/html;charset=" + alipayConfig.getCharset());
            return new ResponseEntity<>(form, headers, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body("Not supported purchase method");
    }

    private Order completeOrder(Order order, PurchaseMethod method) {
        User user = userService.getUserById(order.getPayerId());
        return completeOrder(user, order, method);
    }

    private Order completeOrder(User user, Order order, PurchaseMethod method) {
        order = orderService.payOrder(user, order, method);
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            mailService.sendSimpleMessage(user.getEmail(), "[CES] Thanks for your purchase", "Your order \"" + order.getName() + "\" has been paid successfully!");
        }
        return order;
    }

    @PostMapping("/alipay-notify")
    public void alipayNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            requestParams.forEach((k, v) -> params.put(k, request.getParameter(k)));
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean signVerified = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset());
            if (signVerified) {
                String orderId = params.get("out_trade_no");
                Order order = orderService.getOrder(UUID.fromString(orderId));
                if (order != null) {
                    completeOrder(order, PurchaseMethod.ALIPAY);
                } else {
                    // TODO: maybe we can log error here?
                }
            } else {
                // TODO: maybe we can log error here?
            }
        } else {
            // TODO: maybe we can log error here?
        }
    }

}
