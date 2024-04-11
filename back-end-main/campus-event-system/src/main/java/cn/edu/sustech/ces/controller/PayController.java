package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.service.OrderService;
import cn.edu.sustech.ces.service.alipay.AlipayConfig;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pay")
public class PayController {

    private final OrderService orderService;

    private final AlipayConfig alipayConfig;

    @Autowired
    public PayController(OrderService orderService, AlipayConfig alipayConfig) {
        this.orderService = orderService;
        this.alipayConfig = alipayConfig;
    }

    @GetMapping("/test-create-order")
    public String testCreateOrder() {
        Order order = orderService.makeOrder("test", "test", 1.0, UUID.randomUUID());
        return order.getId().toString() + " created.";
    }

    @GetMapping("/alipay")
    public void payOrderByAlipay(String orderId, HttpServletResponse response) throws Exception {
        Order order = orderService.getOrder(UUID.fromString(orderId));
        if (order == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
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
        jsonObject.put("body", order.getDescription());

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setNotifyUrl(alipayConfig.getAlipayNotifyUrl());
//        alipayRequest.setReturnUrl("www.baidu.com");
        alipayRequest.setBizContent(jsonObject.toString());

        String form = "";
        form = alipayClient.pageExecute(alipayRequest).getBody();
        response.setContentType("text/html;charset=" + alipayConfig.getCharset());
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
        return;
    }

}
